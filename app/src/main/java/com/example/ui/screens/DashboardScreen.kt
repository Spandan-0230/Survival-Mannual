package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Calamity
import com.example.data.CalamityRepository
import com.example.data.UserPrefs
import com.example.ui.translation.translate
import com.example.util.PdfExporter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToCalamity: (String) -> Unit,
    onEditProfile: () -> Unit,
    onNavigateToCivicLogger: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val profile = userPrefs.getProfile()
    val appLanguage = userPrefs.getLanguage()
    val calamities = CalamityRepository.calamities

    var showProfileMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0F1113),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(translate("Survive", appLanguage), fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 24.sp)
                            Text("IN", fontWeight = FontWeight.SemiBold, color = Color(0xFF60A5FA), fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF1E293B),
                                border = BorderStroke(1.dp, Color(0xFF334155))
                            ) {
                                Text(
                                    text = "v3.0",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF60A5FA),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(translate("REGIONAL RESPONSE HUB", appLanguage), fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { PdfExporter.exportAllGuides(context, calamities, appLanguage) },
                        modifier = Modifier.testTag("dashboard_export_all_pdf_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = translate("Export PDF Guide", appLanguage),
                            tint = Color(0xFFEF4444)
                        )
                    }
                    Box {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(44.dp)
                                .background(Color(0xFF2563EB).copy(alpha = 0.2f), CircleShape)
                                .padding(1.dp)
                                .background(Color(0xFF0F1113), CircleShape)
                                .clickable { showProfileMenu = true }
                                .testTag("profile_avatar_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile?.name?.take(2)?.uppercase() ?: "AK",
                                color = Color(0xFF60A5FA),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        DropdownMenu(
                            expanded = showProfileMenu,
                            onDismissRequest = { showProfileMenu = false },
                            modifier = Modifier.background(Color(0xFF1E293B))
                        ) {
                            DropdownMenuItem(
                                text = { Text(translate("Edit Profile", appLanguage), color = Color.White) },
                                onClick = {
                                    showProfileMenu = false
                                    onEditProfile()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(translate("Export PDF Guide", appLanguage), color = Color(0xFF60A5FA)) },
                                onClick = {
                                    showProfileMenu = false
                                    PdfExporter.exportAllGuides(context, calamities, appLanguage)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(translate("Logout", appLanguage), color = Color(0xFFEF4444)) },
                                onClick = {
                                    showProfileMenu = false
                                    onLogout()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F1113)
                )
            )
        },
        floatingActionButton = {
            val haptic = LocalHapticFeedback.current
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFDC2626), CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (profile != null && profile.emergencyContact.isNotBlank()) {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("smsto:" + profile.emergencyContact)
                                        putExtra("sms_body", translate("EMERGENCY! I need immediate assistance.", appLanguage))
                                    }
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, translate("Please set an emergency contact in your profile.", appLanguage), Toast.LENGTH_SHORT).show()
                                }
                            },
                            onTap = {
                                Toast.makeText(context, translate("Long press to send Emergency SMS", appLanguage), Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Sos, contentDescription = "SOS", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (profile != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A).copy(alpha = 0.85f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF10B981), CircleShape))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(translate("IDENTITY ENCRYPTED", appLanguage), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981), letterSpacing = 1.sp)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF450A0A),
                                    border = BorderStroke(1.dp, Color(0xFF7F1D1D).copy(alpha = 0.5f))
                                ) {
                                    Text("${translate("Blood", appLanguage)}: ${profile.bloodType}", fontSize = 10.sp, color = Color(0xFFF87171), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF1E293B),
                                    border = BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier.clickable { onEditProfile() }
                                ) {
                                    Text(
                                        text = translate("Edit Profile", appLanguage),
                                        fontSize = 10.sp,
                                        color = Color(0xFF60A5FA),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(profile.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium, color = Color.White)
                        Text(
                            text = "${profile.state} • ${profile.district}" + if (profile.emergencyContact.isNotBlank()) " • SOS: ${profile.emergencyContact}" else "",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )

                        val historyItems = profile.medicalHistory.split(",").filter { it.isNotBlank() }
                        if (historyItems.isNotEmpty()) {
                            Row(modifier = Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                historyItems.take(2).forEach { item ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF1E293B)
                                    ) {
                                        Text(item.trim(), fontSize = 10.sp, color = Color(0xFFCBD5E1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Civic Issue Logger Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clickable { onNavigateToCivicLogger() },
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1B1F))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF3B82F6).copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ReportProblem, contentDescription = "Logger", tint = Color(0xFF60A5FA))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(translate("Civic Issue Logger", appLanguage), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        Text(translate("Draft formal complaints & log geotags", appLanguage), fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", tint = Color(0xFF64748B))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(translate("Active Regional Threats", appLanguage), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFCBD5E1))
                OutlinedButton(
                    onClick = { PdfExporter.exportAllGuides(context, calamities, appLanguage) },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1C1B1F),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("export_all_pdf_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = translate("Export PDF Guide", appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(calamities) { calamity ->
                    CalamityCard(
                        calamity = calamity,
                        onClick = { onNavigateToCalamity(calamity.id) },
                        onExportPdf = { PdfExporter.exportCalamityGuide(context, calamity, appLanguage) },
                        appLanguage = appLanguage
                    )
                }
            }
        }
    }
}

@Composable
fun CalamityCard(
    calamity: Calamity,
    onClick: () -> Unit,
    onExportPdf: () -> Unit,
    appLanguage: String
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF1E293B)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1B1F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            var finalIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
            var finalEmoji: String? = null
            val finalBg: Color
            val finalTint: Color

            when (calamity.id) {
                "earthquake" -> {
                    finalBg = Color(0xFF451A03).copy(alpha = 0.3f)
                    finalTint = Color(0xFFF59E0B)
                    finalEmoji = "🫨"
                }
                "landslide" -> {
                    finalBg = Color(0xFF451A03).copy(alpha = 0.3f)
                    finalTint = Color(0xFFD97706)
                    finalEmoji = "⛰️"
                }
                "wildfire" -> {
                    finalBg = Color(0xFF450A0A).copy(alpha = 0.3f)
                    finalTint = Color(0xFFEF4444)
                    finalEmoji = "🔥"
                }
                "avalanche" -> {
                    finalBg = Color(0xFF1E293B).copy(alpha = 0.3f)
                    finalTint = Color(0xFF93C5FD)
                    finalEmoji = "❄️"
                }
                "volcanic_eruption" -> {
                    finalBg = Color(0xFF431407).copy(alpha = 0.3f)
                    finalTint = Color(0xFFF97316)
                    finalEmoji = "🌋"
                }
                else -> {
                    val (bg, tint, ic) = when (calamity.iconName) {
                        "warning" -> Triple(Color(0xFF451A03).copy(alpha = 0.3f), Color(0xFFF59E0B), Icons.Default.Warning)
                        "water" -> Triple(Color(0xFF172554).copy(alpha = 0.3f), Color(0xFF3B82F6), Icons.Default.WaterDrop)
                        "cyclone" -> Triple(Color(0xFF431407).copy(alpha = 0.3f), Color(0xFFF97316), Icons.Default.Storm)
                        "sunny" -> Triple(Color(0xFF450A0A).copy(alpha = 0.3f), Color(0xFFEF4444), Icons.Default.WbSunny)
                        else -> Triple(Color(0xFF1E293B), Color(0xFF94A3B8), Icons.Default.Info)
                    }
                    finalBg = bg
                    finalTint = tint
                    finalIcon = ic
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(finalBg, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (finalEmoji != null) {
                        Text(text = finalEmoji, fontSize = 20.sp)
                    } else if (finalIcon != null) {
                        Icon(finalIcon, contentDescription = null, tint = finalTint)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F1113),
                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                    modifier = Modifier
                        .clickable(onClick = onExportPdf)
                        .testTag("export_pdf_${calamity.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = translate("Export PDF", appLanguage),
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PDF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(translate(calamity.name, appLanguage), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)

            val statusText = if (calamity.iconName == "warning") "High Risk Zone" else if (calamity.iconName == "water") "Monsoon Protocol" else "Action Required"
            Text(
                text = translate(statusText, appLanguage),
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
