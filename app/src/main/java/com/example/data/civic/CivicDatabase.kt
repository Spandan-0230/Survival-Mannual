package com.example.data.civic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CivicIssue::class], version = 1, exportSchema = false)
abstract class CivicDatabase : RoomDatabase() {
    abstract fun civicIssueDao(): CivicIssueDao

    companion object {
        @Volatile
        private var Instance: CivicDatabase? = null

        fun getDatabase(context: Context): CivicDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CivicDatabase::class.java, "civic_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
