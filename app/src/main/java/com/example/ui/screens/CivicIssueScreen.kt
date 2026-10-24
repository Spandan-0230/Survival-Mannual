package com.example.ui.screens

import android.content.Context

import android.content.Intent

import android.net.Uri

import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import com.example.ui.translation.translate

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalClipboardManager

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.AnnotatedString

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage

import com.example.data.civic.CivicIssue

import com.example.data.UserPrefs

import java.text.SimpleDateFormat

import java.util.Date

import java.util.Locale

import androidx.compose.foundation.gestures.detectTransformGestures

import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.window.Dialog

import androidx.compose.ui.window.DialogProperties
import java.io.File
import androidx.core.content.FileProvider
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CivicIssueScreen(
    onNavigateBack: () -> Unit,
    viewModel: CivicIssueViewModel = viewModel()
) {
    val issues by viewModel.allIssues.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var fullScreenImageUri by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val userName = userPrefs.getProfile()?.name ?: "Resident" 
    val appLanguage = userPrefs.getLanguage()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(translate("Civic Issue Logger", appLanguage), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF3B82F6),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Issue")
            }
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        if (issues.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocationCity, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(translate("No issues logged yet.", appLanguage), color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(issues) { issue ->
                    CivicIssueCard(
                        issue = issue,
                        onDelete = { viewModel.deleteIssue(issue.id) },
                        onImageClick = { fullScreenImageUri = it }
                    )
                }
            }
        }
        if (fullScreenImageUri != null) {
            Dialog(
                onDismissRequest = { fullScreenImageUri = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                var scale by remember { mutableFloatStateOf(1f) }
                var offset by remember { mutableStateOf(Offset.Zero) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offset = if (scale > 1f) offset + pan else Offset.Zero
                            }
                        }
                ) {
                    AsyncImage(
                        model = fullScreenImageUri,
                        contentDescription = "Full Screen Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            ),
                        contentScale = ContentScale.Fit
                    )
                    IconButton(
                        onClick = { fullScreenImageUri = null },
                        modifier = Modifier.align(Alignment.TopEnd).padding(32.dp).background(Color.Black.copy(alpha=0.6f), androidx.compose.foundation.shape.CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            }
        }
        if (showAddDialog) {
            AddIssueBottomSheet(
                userName = userName,
                onDismiss = { showAddDialog = false },
                onSave = { category, desc, loc, imgUri, draft ->
                    viewModel.addIssue(category, desc, loc, imgUri, draft)
                    showAddDialog = false
                }
            )
        }
    }
}
@Composable
fun CivicIssueCard(issue: CivicIssue, onDelete: () -> Unit, onImageClick: (String) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val appLanguage = userPrefs.getLanguage()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(32.dp).background(Color(0xFF3B82F6).copy(alpha=0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Report, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = issue.category,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha=0.7f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = issue.location,
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            if (issue.imageUri != null) {
                AsyncImage(
                    model = issue.imageUri,
                    contentDescription = "Issue Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onImageClick(issue.imageUri) },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = issue.description,
                fontSize = 14.sp,
                color = Color(0xFFCBD5E1),
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            if (expanded && !issue.draftText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(translate("Formal Complaint Draft", appLanguage), fontSize = 14.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                            Row {
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(issue.draftText))
                                    Toast.makeText(context, translate("Copied to clipboard", appLanguage), Toast.LENGTH_SHORT).show()
                                }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                                IconButton(onClick = {
                                    shareIssue(context, issue.draftText, issue.imageUri)
                                }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = issue.draftText, fontSize = 13.sp, color = Color(0xFFE2E8F0), lineHeight = 18.sp)
                    }
                }
            }
            if (!expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(translate("Tap to view full details & draft", appLanguage), fontSize = 12.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Medium)
            }
        }
    }
}
fun shareIssue(context: Context, text: String, imageUriStr: String?) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_SUBJECT, "Civic Issue Complaint")
        putExtra(Intent.EXTRA_TEXT, text)
        if (imageUriStr != null) {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUriStr))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            type = "text/plain"
        }
    }
    context.startActivity(Intent.createChooser(intent, "Share via (PDF Maker / Email)"))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIssueBottomSheet(userName: String, onDismiss: () -> Unit, onSave: (String, String, String, String?, String?) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val categories = listOf("Pothole", "Broken Streetlight", "Water Leakage", "Garbage Dump", "Fallen Tree", "Other")
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val appLanguage = userPrefs.getLanguage()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { 
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            imageUri = it 
        }
    }
    
    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri = cameraUri.value
        }
    }
    
    val sdf = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val currentDate = remember { sdf.format(Date()) }
    // Real-time live draft generation
    val draftText by remember(category, location, description) {
        derivedStateOf {
            val cat = if (category.isNotBlank()) category else "[Select Category]"
            val loc = if (location.isNotBlank()) location else "[Enter Location]"
            val desc = if (description.isNotBlank()) description else "[Provide details of the issue here]"
            """
To The Municipal Commissioner,
Date: $currentDate
Subject: Urgent Complaint regarding $cat at $loc
Dear Sir/Madam,
I am writing to bring to your immediate attention a severe civic issue in our area. Specifically, there is a $cat located at $loc.
Details:
$desc
This is causing significant inconvenience and poses a risk to the residents and commuters. I kindly request you to inspect the site and initiate necessary repair/clearing work at the earliest.
Thank you for your prompt attention to this matter.
Sincerely,
$userName
            """.trimIndent()
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1E293B)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(translate("Log Civic Issue", appLanguage), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    readOnly = false,
                    label = { Text(translate("Category", appLanguage)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFF0F172A))
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(translate(selectionOption, appLanguage), color = Color.White) },
                            onClick = {
                                category = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text(translate("Location / Geotag", appLanguage)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(translate("Description", appLanguage)) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { launcher.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (imageUri == null) translate("Gallery", appLanguage) else translate("Change", appLanguage), fontSize = 14.sp)
                }
                
                Button(
                    onClick = { 
                        val uri = createTempImageUri(context)
                        cameraUri.value = uri
                        cameraLauncher.launch(uri)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(translate("Camera", appLanguage), fontSize = 14.sp)
                }
            }
            var previewFullScreen by remember { mutableStateOf(false) }
            if (previewFullScreen && imageUri != null) {
                Dialog(
                    onDismissRequest = { previewFullScreen = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    var scale by remember { mutableFloatStateOf(1f) }
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(1f, 5f)
                                    offset = if (scale > 1f) offset + pan else Offset.Zero
                                }
                            }
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Full Screen Preview",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offset.x,
                                    translationY = offset.y
                                ),
                            contentScale = ContentScale.Fit
                        )
                        IconButton(
                            onClick = { previewFullScreen = false },
                            modifier = Modifier.align(Alignment.TopEnd).padding(32.dp).background(Color.Black.copy(alpha=0.6f), androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }
            }
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { previewFullScreen = true },
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            // Live Preview of Draft
            Text(translate("Live Draft Preview", appLanguage), fontSize = 14.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.SemiBold)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = draftText,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (category.isNotBlank() && location.isNotBlank() && description.isNotBlank()) {
                        onSave(category, description, location, imageUri?.toString(), draftText)
                    } else {
                        Toast.makeText(context, translate("Please fill in all details", appLanguage), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                enabled = category.isNotBlank() && location.isNotBlank() && description.isNotBlank()
            ) {
                Text(translate("Save Issue", appLanguage), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile("civic_issue_", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}
