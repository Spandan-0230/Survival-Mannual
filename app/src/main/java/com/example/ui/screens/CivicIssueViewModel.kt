package com.example.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.civic.CivicDatabase
import com.example.data.civic.CivicIssue
import com.example.data.civic.CivicRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CivicIssueViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CivicRepository
    val allIssues: StateFlow<List<CivicIssue>>

    init {
        val civicDao = CivicDatabase.getDatabase(application).civicIssueDao()
        repository = CivicRepository(civicDao)
        allIssues = repository.allIssues.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addIssue(category: String, description: String, location: String, imageUri: String?, draftText: String?) {
        viewModelScope.launch {
            repository.insert(
                CivicIssue(
                    category = category,
                    description = description,
                    location = location,
                    imageUri = imageUri,
                    draftText = draftText
                )
            )
        }
    }

    fun deleteIssue(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
