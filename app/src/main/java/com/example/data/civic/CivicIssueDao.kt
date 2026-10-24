package com.example.data.civic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CivicIssueDao {
    @Query("SELECT * FROM civic_issues ORDER BY timestamp DESC")
    fun getAllIssues(): Flow<List<CivicIssue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssue(issue: CivicIssue)

    @Query("DELETE FROM civic_issues WHERE id = :id")
    suspend fun deleteIssueById(id: Int)
}
