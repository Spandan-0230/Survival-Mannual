package com.example.data.civic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "civic_issues")
data class CivicIssue(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val description: String,
    val location: String,
    val imageUri: String?,
    val draftText: String?,
    val timestamp: Long = System.currentTimeMillis()
)
