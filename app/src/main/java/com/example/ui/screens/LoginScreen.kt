package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPrefs
import com.example.data.UserProfile
import com.example.ui.translation.translate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val appLanguage = userPrefs.getLanguage()

    var name by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    // Store the 10-digit Indian mobile number (without +91 prefix)
    var tenDigitPhone by remember { mutableStateOf("") }
    var emergencyContactError by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

    var expandedState by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedBloodType by remember { mutableStateOf(false) }

    var isEditMode by remember { mutableStateOf(false) }
    var isValidatingSubmit by remember { mutableStateOf(false) }
    var isValidatingPhoneField by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun extractTenDigits(raw: String): String {
        val digitsOnly = raw.filter { it.isDigit() }
        return when {
            digitsOnly.startsWith("91") && digitsOnly.length > 10 -> digitsOnly.substring(2).take(10)
            digitsOnly.startsWith("0") && digitsOnly.length > 10 -> digitsOnly.substring(1).take(10)
            else -> digitsOnly.take(10)
        }
    }

    LaunchedEffect(Unit) {
        val existingProfile = userPrefs.getProfile()
        if (existingProfile != null) {
            name = existingProfile.name
            state = existingProfile.state
            district = existingProfile.district
            bloodType = existingProfile.bloodType
            medicalHistory = existingProfile.medicalHistory
            tenDigitPhone = extractTenDigits(existingProfile.emergencyContact)
            if (tenDigitPhone.matches(Regex("^[6-9]\\d{9}$"))) {
                isPhoneVerified = true
            }
            isEditMode = true
        }
    }

    // Trigger live validation indicator when user enters 10 digits
    LaunchedEffect(tenDigitPhone) {
        if (tenDigitPhone.length == 10) {
            isValidatingPhoneField = true
            isPhoneVerified = false
            delay(450)
            isValidatingPhoneField = false
            if (tenDigitPhone.matches(Regex("^[6-9]\\d{9}$"))) {
                isPhoneVerified = true
                emergencyContactError = null
            } else {
                isPhoneVerified = false
                emergencyContactError = "Indian mobile numbers must start with 6, 7, 8, or 9."
            }
        } else {
            isValidatingPhoneField = false
            isPhoneVerified = false
            if (tenDigitPhone.isNotEmpty() && tenDigitPhone.first() !in '6'..'9') {
                emergencyContactError = "Indian mobile numbers must start with 6, 7, 8, or 9."
            } else {
                emergencyContactError = null
            }
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
        focusedContainerColor = Color(0xFF0F1113),
        errorBorderColor = Color(0xFFEF4444),
        errorLabelColor = Color(0xFFEF4444),
        errorContainerColor = Color(0xFF0F1113),
        errorTextColor = Color.White
    )

    Scaffold(
        containerColor = Color(0xFF0F1113),
        topBar = {
            TopAppBar(
                title = {
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
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
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
                onValueChange = { name = it; showError = false },
                label = { Text(translate("Full Name", appLanguage)) },
                modifier = Modifier.fillMaxWidth().testTag("name_input"),
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
                                    showError = false
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
                                        showError = false
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
                                showError = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            // Indian Emergency Contact (+91) Input with live CircularProgressIndicator validation
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = tenDigitPhone,
                    onValueChange = { newValue ->
                        tenDigitPhone = extractTenDigits(newValue)
                        showError = false
                    },
                    label = { Text(translate("Emergency Contact", appLanguage) + " (India +91)") },
                    placeholder = { Text("9876543210", color = Color(0xFF475569)) },
                    leadingIcon = {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E293B),
                            border = BorderStroke(1.dp, Color(0xFF334155)),
                            modifier = Modifier.padding(start = 10.dp, end = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = Color(0xFF60A5FA),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+91",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        when {
                            isValidatingPhoneField || isValidatingSubmit -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp).testTag("phone_validation_progress"),
                                    color = Color(0xFF60A5FA),
                                    strokeWidth = 2.dp
                                )
                            }
                            isPhoneVerified -> {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Indian Number",
                                    tint = Color(0xFF10B981)
                                )
                            }
                            emergencyContactError != null -> {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Invalid Indian Number",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("emergency_contact_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = emergencyContactError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusText = when {
                        isValidatingPhoneField || isValidatingSubmit -> "Validating Indian (+91) mobile number..."
                        emergencyContactError != null -> translate(emergencyContactError!!, appLanguage)
                        isPhoneVerified -> "✓ Valid Indian mobile number (+91 $tenDigitPhone)"
                        else -> "10-digit Indian mobile starting with 6, 7, 8, or 9"
                    }
                    val statusColor = when {
                        isValidatingPhoneField || isValidatingSubmit -> Color(0xFF60A5FA)
                        emergencyContactError != null -> Color(0xFFEF4444)
                        isPhoneVerified -> Color(0xFF10B981)
                        else -> Color(0xFF94A3B8)
                    }
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${tenDigitPhone.length}/10",
                        color = if (tenDigitPhone.length == 10) Color(0xFF10B981) else Color(0xFF64748B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                label = { Text(translate("Medical History Summary (Optional)", appLanguage)) },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            if (showError) {
                Text(
                    text = translate("Please fill out all required fields.", appLanguage),
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    val isValidPhone = tenDigitPhone.matches(Regex("^[6-9]\\d{9}$"))
                    if (name.isBlank() || state.isBlank() || district.isBlank() || bloodType.isBlank()) {
                        showError = true
                    } else if (!isValidPhone) {
                        emergencyContactError = translate("Please enter a valid 10-digit Indian mobile number.", appLanguage)
                    } else {
                        coroutineScope.launch {
                            isValidatingSubmit = true
                            emergencyContactError = null
                            delay(900) // Trigger circular progress indicator while validating Indian phone number
                            val finalPhone = "+91$tenDigitPhone"
                            userPrefs.saveProfile(
                                UserProfile(name.trim(), state, district, bloodType, medicalHistory.trim(), appLanguage, finalPhone)
                            )
                            isValidatingSubmit = false
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(56.dp)
                    .testTag("submit_profile_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    disabledContainerColor = Color(0xFF1E3A8A)
                ),
                enabled = !isValidatingSubmit
            ) {
                if (isValidatingSubmit) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp).testTag("login_circular_progress"),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Validating +91 Contact...",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    val buttonText = if (isEditMode) translate("Update Profile", appLanguage) else translate("Save Profile & Continue", appLanguage)
                    Text(buttonText, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
