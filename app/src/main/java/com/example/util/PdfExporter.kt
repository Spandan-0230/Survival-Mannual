package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.Calamity
import com.example.ui.translation.translate
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PdfExporter {
    fun exportCalamityGuide(context: Context, calamity: Calamity, language: String) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isFakeBoldText = true
        }
        val headerPaint = Paint().apply {
            color = Color.rgb(200, 0, 0)
            textSize = 18f
            isFakeBoldText = true
        }
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
        }

        var currentY = 50f
        val margin = 50f
        val maxWidth = 595f - 2 * margin

        fun drawText(text: String, paint: Paint, yOffset: Float = 0f): Float {
            val words = text.split(" ")
            var line = ""
            var y = currentY + yOffset
            val initialY = y
            
            for (word in words) {
                val testLine = if (line.isEmpty()) word else "$line $word"
                val width = paint.measureText(testLine)
                if (width > maxWidth) {
                    canvas.drawText(line, margin, y, paint)
                    line = word
                    y += paint.descent() - paint.ascent() + 2f
                    
                    if (y > 800f) {
                        document.finishPage(page)
                        page = document.startPage(pageInfo)
                        canvas = page.canvas
                        y = 50f
                    }
                } else {
                    line = testLine
                }
            }
            if (line.isNotEmpty()) {
                canvas.drawText(line, margin, y, paint)
                y += paint.descent() - paint.ascent() + 2f
            }
            return y - initialY
        }

        currentY += drawText("${translate(calamity.name, language)} - ${translate("Survival Guide", language)}", titlePaint)
        currentY += 20f

        currentY += drawText(translate("Immediate Actions", language), headerPaint)
        currentY += 10f
        calamity.steps.forEachIndexed { index, step ->
            currentY += drawText("${index + 1}. ${translate(step, language)}", textPaint)
            currentY += 5f
        }
        currentY += 15f

        currentY += drawText(translate("First Aid Guide", language), headerPaint)
        currentY += 10f
        calamity.firstAidGuide.forEach { guide ->
            currentY += drawText("• ${translate(guide, language)}", textPaint)
            currentY += 5f
        }
        currentY += 15f

        currentY += drawText(translate("Emergency Contacts", language), headerPaint)
        currentY += 10f
        calamity.emergencyContacts.forEach { contact ->
            currentY += drawText(contact, textPaint)
            currentY += 5f
        }
        currentY += 5f
        currentY += drawText("${translate("National Helpline", language)}: ${calamity.helplineNo}", textPaint)

        document.finishPage(page)

        try {
            val file = File(context.cacheDir, "${calamity.name.replace(" ", "_")}_Survival_Guide.pdf")
            document.writeTo(FileOutputStream(file))
            document.close()

            val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "${calamity.name} Survival Guide")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, translate("Export PDF Guide", language)))
        } catch (e: Exception) {
            e.printStackTrace()
            document.close()
            Toast.makeText(context, "Failed to export PDF", Toast.LENGTH_SHORT).show()
        }
    }
}
