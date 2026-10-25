package com.example.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

data class UserProfile(
    val name: String,
    val state: String,
    val district: String,
    val bloodType: String,
    val medicalHistory: String,
    val language: String,
    val emergencyContact: String
)

class UserPrefs(context: Context) {
    companion object {
        private const val CURRENT_SCHEMA_VERSION = 3
        private val INDIAN_PHONE_REGEX = Regex("^\\+91[6-9]\\d{9}$")

        fun isValidIndianMobile(rawPhone: String): Boolean {
            val cleaned = rawPhone.replace(Regex("[\\s-]"), "")
            val tenDigit = when {
                cleaned.startsWith("+91") && cleaned.length == 13 -> cleaned.substring(3)
                cleaned.startsWith("91") && cleaned.length == 12 -> cleaned.substring(2)
                cleaned.startsWith("0") && cleaned.length == 11 -> cleaned.substring(1)
                else -> cleaned
            }
            return tenDigit.matches(Regex("^[6-9]\\d{9}$"))
        }

        fun formatToPlus91(rawPhone: String): String {
            val cleaned = rawPhone.replace(Regex("[\\s-]"), "")
            val tenDigit = when {
                cleaned.startsWith("+91") && cleaned.length == 13 -> cleaned.substring(3)
                cleaned.startsWith("91") && cleaned.length == 12 -> cleaned.substring(2)
                cleaned.startsWith("0") && cleaned.length == 11 -> cleaned.substring(1)
                else -> cleaned
            }
            return if (tenDigit.matches(Regex("^[6-9]\\d{9}$"))) "+91$tenDigit" else cleaned
        }
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_health_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveLanguage(language: String) {
        sharedPreferences.edit().putString("LANGUAGE", language).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("LANGUAGE", "English") ?: "English"
    }

    fun clearProfile() {
        sharedPreferences.edit().apply {
            remove("NAME")
            remove("STATE")
            remove("DISTRICT")
            remove("BLOOD_TYPE")
            remove("MEDICAL_HISTORY")
            remove("EMERGENCY_CONTACT")
            remove("SCHEMA_VERSION")
            apply()
        }
    }

    fun saveProfile(profile: UserProfile) {
        val formattedContact = formatToPlus91(profile.emergencyContact)
        sharedPreferences.edit().apply {
            putString("NAME", profile.name)
            putString("STATE", profile.state)
            putString("DISTRICT", profile.district)
            putString("BLOOD_TYPE", profile.bloodType)
            putString("MEDICAL_HISTORY", profile.medicalHistory)
            putString("LANGUAGE", profile.language)
            putString("EMERGENCY_CONTACT", formattedContact)
            putInt("SCHEMA_VERSION", CURRENT_SCHEMA_VERSION)
            apply()
        }
    }

    fun getProfile(): UserProfile? {
        val name = sharedPreferences.getString("NAME", null)
        if (name.isNullOrEmpty()) return null

        return UserProfile(
            name = name,
            state = sharedPreferences.getString("STATE", "") ?: "",
            district = sharedPreferences.getString("DISTRICT", "") ?: "",
            bloodType = sharedPreferences.getString("BLOOD_TYPE", "") ?: "",
            medicalHistory = sharedPreferences.getString("MEDICAL_HISTORY", "") ?: "",
            language = sharedPreferences.getString("LANGUAGE", "English") ?: "English",
            emergencyContact = sharedPreferences.getString("EMERGENCY_CONTACT", "") ?: ""
        )
    }

    fun isLoggedIn(): Boolean {
        val name = sharedPreferences.getString("NAME", null)
        val contact = sharedPreferences.getString("EMERGENCY_CONTACT", null) ?: ""
        val schemaVersion = sharedPreferences.getInt("SCHEMA_VERSION", 1)
        return !name.isNullOrEmpty() &&
            schemaVersion >= CURRENT_SCHEMA_VERSION &&
            INDIAN_PHONE_REGEX.matches(contact)
    }
}
