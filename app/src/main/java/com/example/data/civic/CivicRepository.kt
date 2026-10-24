package com.example.data.civic

import kotlinx.coroutines.flow.Flow

class CivicRepository(private val civicIssueDao: CivicIssueDao) {
    val allIssues: Flow<List<CivicIssue>> = civicIssueDao.getAllIssues()

    suspend fun insert(issue: CivicIssue) = civicIssueDao.insertIssue(issue)

    suspend fun deleteById(id: Int) = civicIssueDao.deleteIssueById(id)
}
