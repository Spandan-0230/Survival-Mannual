package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.data.UserPrefs
import com.example.ui.translation.translate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    
    // Read the current language saved from Welcome screen (or existing profile)
    val appLanguage = userPrefs.getLanguage()

    var name by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var emergencyContactError by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    
    var expandedState by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedBloodType by remember { mutableStateOf(false) }

    var isEditMode by remember { mutableStateOf(false) }
    var isValidating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val existingProfile = userPrefs.getProfile()
        if (existingProfile != null) {
            name = existingProfile.name
            state = existingProfile.state
            district = existingProfile.district
            bloodType = existingProfile.bloodType
            medicalHistory = existingProfile.medicalHistory
            emergencyContact = existingProfile.emergencyContact
            isEditMode = true
        }
    }

    val bloodTypes = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    val locationData = remember {
        mapOf(
            "Maharashtra" to listOf("Mumbai", "Pune", "Ratnagiri", "Nagpur", "Thane", "Nashik"),
            "Karnataka" to listOf("Bengaluru", "Mysuru", "Mangaluru", "Hubballi"),
            "Delhi" to listOf("New Delhi", "North Delhi", "South Delhi"),
            "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli"),
            "Gujarat" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot"),
            "West Bengal" to listOf("Kolkata", "Howrah", "Darjeeling", "Siliguri"),
            "Kerala" to listOf("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur"),
            "Rajasthan" to listOf("Jaipur", "Jodhpur", "Udaipur", "Kota")
        )
    }
    val states = locationData.keys.toList()
    val districts = locationData[state] ?: emptyList()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color(0xFF334155),
        focusedBorderColor = Color(0xFF60A5FA),
        unfocusedTextColor = Color(0xFFE2E2E6),
        focusedTextColor = Color.White,
        cursorColor = Color(0xFF60A5FA),
        unfocusedLabelColor = Color(0xFF94A3B8),
        focusedLabelColor = Color(0xFF60A5FA),
        unfocusedContainerColor = Color(0xFF0F1113),
        focusedContainerColor = Color(0xFF0F1113)
    )

    Scaffold(
        containerColor = Color(0xFF0F1113),
        topBar = {
            TopAppBar(
                title = { 
                    Row {
                        Text(translate("Survive", appLanguage), fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 24.sp)
                        Text("IN", fontWeight = FontWeight.SemiBold, color = Color(0xFF60A5FA), fontSize = 24.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = translate("Go Back", appLanguage),
                            tint = Color.White
                        )
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = translate("Regional Response Vault", appLanguage),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = translate("Enter details for quick emergency triage. Data is encrypted and stored locally on your device.", appLanguage),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    lineHeight = 18.sp
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(translate("Full Name", appLanguage)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = expandedState,
                    onExpandedChange = { expandedState = !expandedState },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = state,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(translate("State", appLanguage)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(
                        expanded = expandedState,
                        onDismissRequest = { expandedState = false },
                        modifier = Modifier.background(Color(0xFF1E293B))
                    ) {
                        states.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s, color = Color.White) },
                                onClick = {
                                    state = s
                                    district = ""
                                    expandedState = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedDistrict,
                    onExpandedChange = { expandedDistrict = !expandedDistrict },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(translate("District", appLanguage)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDistrict) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDistrict,
                        onDismissRequest = { expandedDistrict = false },
                        modifier = Modifier.background(Color(0xFF1E293B))
                    ) {
                        if (districts.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(translate("Select State First", appLanguage), color = Color(0xFF94A3B8)) },
                                onClick = { expandedDistrict = false },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        } else {
                            districts.forEach { d ->
                                DropdownMenuItem(
                                    text = { Text(d, color = Color.White) },
                                    onClick = {
                                        district = d
                                        expandedDistrict = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedBloodType,
                onExpandedChange = { expandedBloodType = !expandedBloodType },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = bloodType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(translate("Blood Type", appLanguage)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBloodType) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = expandedBloodType,
                    onDismissRequest = { expandedBloodType = false },
                    modifier = Modifier.background(Color(0xFF1E293B))
                ) {
                    bloodTypes.forEach { bt ->
                        DropdownMenuItem(
                            text = { Text(bt, color = Color.White) },
                            onClick = {
                                bloodType = bt
                                expandedBloodType = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            OutlinedTextField(
                value = emergencyContact,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '+' || it == ' ' || it == '-' }) {
                        emergencyContact = newValue 
                        emergencyContactError = false 
                    }
                },
                label = { Text(translate("Emergency Contact", appLanguage)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                isError = emergencyContactError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            if (emergencyContactError) {
                Text(translate("Please enter a valid 10-digit Indian mobile number.", appLanguage), color = Color(0xFFEF4444), fontSize = 12.sp)
            }
            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                label = { Text(translate("Medical History Summary (Optional)", appLanguage)) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            if (showError) {
                Text(translate("Please fill out all required fields.", appLanguage), color = Color(0xFFEF4444), fontSize = 12.sp)
            }

            Button(
                onClick = {
                    val cleanedPhone = emergencyContact.replace(Regex("[\\s-]"), "")
                    val isValidPhone = cleanedPhone.isBlank() || cleanedPhone.matches(Regex("^(?:\\+91|91|0)?[6-9]\\d{9}$"))
                    if (name.isBlank() || state.isBlank() || district.isBlank() || bloodType.isBlank()) {
                        showError = true
                    } else if (!isValidPhone) {
                        emergencyContactError = true
                    } else {
                        coroutineScope.launch {
                            isValidating = true
                            delay(800) // Simulating network/validation delay for perceived performance
                            val finalPhone = if (cleanedPhone.isNotBlank()) {
                                when {
                                    cleanedPhone.startsWith("+91") -> cleanedPhone
                                    cleanedPhone.startsWith("91") && cleanedPhone.length == 12 -> "+$cleanedPhone"
                                    cleanedPhone.startsWith("0") && cleanedPhone.length == 11 -> "+91${cleanedPhone.substring(1)}"
                                    cleanedPhone.length == 10 -> "+91$cleanedPhone"
                                    else -> cleanedPhone
                                }
                            } else ""
                            userPrefs.saveProfile(
                                UserProfile(name, state, district, bloodType, medicalHistory, appLanguage, finalPhone)
                            )
                            isValidating = false
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    disabledContainerColor = Color(0xFF2563EB).copy(alpha = 0.5f)
                ),
                enabled = !isValidating
            ) {
                if (isValidating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    val buttonText = if (isEditMode) translate("Update Profile", appLanguage) else translate("Save Profile & Continue", appLanguage)
                    Text(buttonText, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
