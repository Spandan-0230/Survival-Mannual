package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.util.PdfExporter
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import com.example.data.CalamityRepository
import com.example.data.UserPrefs
import com.example.ui.translation.translate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalamityDetailScreen(
    calamityId: String,
    onNavigateBack: () -> Unit
) {
    val calamity = CalamityRepository.calamities.find { it.id == calamityId }
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val appLanguage = userPrefs.getLanguage()

    if (calamity == null) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1113)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Calamity not found", color = Color.White)
            Button(onClick = onNavigateBack) { Text("Go Back") }
        }
        return
    }

    Scaffold(
        containerColor = Color(0xFF0F1113),
        topBar = {
            TopAppBar(
                title = { Text(translate(calamity.name, appLanguage), fontWeight = FontWeight.SemiBold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { PdfExporter.exportCalamityGuide(context, calamity, appLanguage) }) {
                        Icon(Icons.Default.Share, contentDescription = translate("Export PDF", appLanguage), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F1113)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (calamity.infographics.isNotEmpty()) {
                SectionHeader(title = translate("Visual Survival Guide", appLanguage), icon = Icons.Default.Warning, color = Color(0xFFF59E0B))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(calamity.infographics) { imageUrl ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(300.dp, 200.dp),
                            border = BorderStroke(1.dp, Color(0xFF1E293B))
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Infographic",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFF1E293B))
            }

            SectionHeader(title = translate("Immediate Actions", appLanguage), icon = Icons.Default.Warning, color = Color(0xFFEF4444)) // red-500
            calamity.steps.forEachIndexed { index, step ->
                StepItem(stepNumber = index + 1, text = translate(step, appLanguage))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = Color(0xFF1E293B))

            SectionHeader(title = translate("First Aid Guide", appLanguage), icon = Icons.Default.MedicalServices, color = Color(0xFF10B981)) // emerald-500
            calamity.firstAidGuide.forEach { guide ->
                BulletItem(text = translate(guide, appLanguage))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = Color(0xFF1E293B))

            SectionHeader(title = translate("Emergency Contacts", appLanguage), icon = Icons.Default.Call, color = Color(0xFF60A5FA)) // blue-400
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1B1F))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    calamity.emergencyContacts.forEach { contact ->
                        Text(contact, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Text("${translate("National Helpline", appLanguage)}: ${calamity.helplineNo}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFCBD5E1))
    }
}

@Composable
fun StepItem(stepNumber: Int, text: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(28.dp).background(Color(0xFF1E293B), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(stepNumber.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, color = Color(0xFFE2E2E6), lineHeight = 22.sp)
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, color = Color(0xFFE2E2E6), lineHeight = 22.sp)
    }
}
