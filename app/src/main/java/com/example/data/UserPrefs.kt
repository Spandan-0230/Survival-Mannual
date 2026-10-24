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
            // Intentionally not removing language so it persists
            apply()
        }
    }

    fun saveProfile(profile: UserProfile) {
        sharedPreferences.edit().apply {
            putString("NAME", profile.name)
            putString("STATE", profile.state)
            putString("DISTRICT", profile.district)
            putString("BLOOD_TYPE", profile.bloodType)
            putString("MEDICAL_HISTORY", profile.medicalHistory)
            putString("LANGUAGE", profile.language)
            putString("EMERGENCY_CONTACT", profile.emergencyContact)
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
        return !sharedPreferences.getString("NAME", null).isNullOrEmpty()
    }
}
