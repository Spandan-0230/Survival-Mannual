package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.Calamity
import com.example.ui.translation.translate
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun exportCalamityGuide(context: Context, calamity: Calamity, language: String) {
        exportGuidesInternal(
            context = context,
            calamities = listOf(calamity),
            language = language,
            fileName = "${calamity.name.replace(" ", "_")}_Survival_Guide.pdf",
            subjectTitle = "${calamity.name} Survival Guide"
        )
    }

    fun exportAllGuides(context: Context, calamities: List<Calamity>, language: String) {
        exportGuidesInternal(
            context = context,
            calamities = calamities,
            language = language,
            fileName = "SurviveIN_Complete_Survival_Manual.pdf",
            subjectTitle = "SurviveIN Complete Survival Manual"
        )
    }

    private fun exportGuidesInternal(
        context: Context,
        calamities: List<Calamity>,
        language: String,
        fileName: String,
        subjectTitle: String
    ) {
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 45f
        val maxWidth = pageWidth - 2 * margin

        val bannerPaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            style = Paint.Style.FILL
        }
        val bannerTitlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 20f
            isFakeBoldText = true
        }
        val bannerSubPaint = Paint().apply {
            color = Color.rgb(96, 165, 250)
            textSize = 11f
            isFakeBoldText = true
        }
        val titlePaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 22f
            isFakeBoldText = true
        }
        val headerPaint = Paint().apply {
            color = Color.rgb(185, 28, 28)
            textSize = 15f
            isFakeBoldText = true
        }
        val firstAidHeaderPaint = Paint().apply {
            color = Color.rgb(4, 120, 87)
            textSize = 15f
            isFakeBoldText = true
        }
        val contactHeaderPaint = Paint().apply {
            color = Color.rgb(29, 78, 216)
            textSize = 15f
            isFakeBoldText = true
        }
        val textPaint = Paint().apply {
            color = Color.rgb(30, 41, 59)
            textSize = 11.5f
        }
        val boldTextPaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 12f
            isFakeBoldText = true
        }

        var pageNumber = 1

        calamities.forEach { calamity ->
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber++).create()
            var page = document.startPage(pageInfo)
            var canvas: Canvas = page.canvas

            // Draw top header bar
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 68f, bannerPaint)
            canvas.drawText("SurviveIN — Regional Survival Manual", margin, 32f, bannerTitlePaint)
            canvas.drawText("OFFLINE PRINTABLE EMERGENCY PROTOCOL • v3.0", margin, 52f, bannerSubPaint)

            var currentY = 100f

            fun drawWrappedText(text: String, paint: Paint): Float {
                val words = text.split(" ")
                var line = ""
                var y = currentY
                val initialY = y
                val lineHeight = paint.descent() - paint.ascent() + 4f

                for (word in words) {
                    val testLine = if (line.isEmpty()) word else "$line $word"
                    val width = paint.measureText(testLine)
                    if (width > maxWidth) {
                        canvas.drawText(line, margin, y, paint)
                        line = word
                        y += lineHeight

                        if (y > pageHeight - 55f) {
                            document.finishPage(page)
                            val nextInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber++).create()
                            page = document.startPage(nextInfo)
                            canvas = page.canvas
                            y = 55f
                        }
                    } else {
                        line = testLine
                    }
                }
                if (line.isNotEmpty()) {
                    canvas.drawText(line, margin, y, paint)
                    y += lineHeight
                }
                return y - initialY
            }

            currentY += drawWrappedText(
                "${translate(calamity.name, language)} - ${translate("Survival Guide", language)}",
                titlePaint
            )
            currentY += 14f

            currentY += drawWrappedText(translate("Immediate Actions", language), headerPaint)
            currentY += 8f
            calamity.steps.forEachIndexed { index, step ->
                currentY += drawWrappedText("${index + 1}. ${translate(step, language)}", textPaint)
                currentY += 6f
            }
            currentY += 12f

            currentY += drawWrappedText(translate("First Aid Guide", language), firstAidHeaderPaint)
            currentY += 8f
            calamity.firstAidGuide.forEach { guide ->
                currentY += drawWrappedText("• ${translate(guide, language)}", textPaint)
                currentY += 6f
            }
            currentY += 12f

            currentY += drawWrappedText(translate("Emergency Contacts", language), contactHeaderPaint)
            currentY += 8f
            calamity.emergencyContacts.forEach { contact ->
                currentY += drawWrappedText(contact, textPaint)
                currentY += 5f
            }
            currentY += 6f
            currentY += drawWrappedText(
                "${translate("National Helpline", language)}: ${calamity.helplineNo}",
                boldTextPaint
            )

            document.finishPage(page)
        }

        try {
            // 1. Write to cacheDir for FileProvider sharing/viewing
            val cacheFile = File(context.cacheDir, fileName)
            FileOutputStream(cacheFile).use { out ->
                document.writeTo(out)
            }

            // 2. Also save directly to device Downloads folder so user has a permanent offline PDF
            saveToDownloads(context, cacheFile, fileName)

            document.close()

            Toast.makeText(
                context,
                "PDF saved to Downloads: $fileName",
                Toast.LENGTH_LONG
            ).show()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cacheFile
            )
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subjectTitle)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val chooser = Intent.createChooser(sendIntent, translate("Export PDF Guide", language)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                document.close()
            } catch (_: Exception) {}
            Toast.makeText(context, "Failed to export PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToDownloads(context: Context, sourceFile: File, fileName: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        sourceFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            } else {
                val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                if (downloadsDir != null) {
                    val destFile = File(downloadsDir, fileName)
                    sourceFile.copyTo(destFile, overwrite = true)
                }
            }
        } catch (_: Exception) {
            // Non-fatal fallback: FileProvider share sheet still works
        }
    }
}
