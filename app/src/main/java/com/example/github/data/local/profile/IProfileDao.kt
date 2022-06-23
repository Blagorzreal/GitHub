package com.example.github.data.local.profile

import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

interface IProfileDao {
    fun getAll(): Flow<List<RepoModel>>
    suspend fun insertRepos(repos: List<RepoModel>)
    suspend fun deleteAll()
    suspend fun deleteAllAndInsertNew(repos: List<RepoModel>)
}