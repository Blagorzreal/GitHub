package com.example.github.data.local.profile

import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

interface IProfileDao {
    fun getAll(ownerId: Long): Flow<List<RepoModel>>
    suspend fun insertRepos(repos: List<RepoModel>)
    suspend fun deleteAll()
    suspend fun deleteAllByOwner(ownerId: Long)
    suspend fun deleteAllAndInsertNew(repos: List<RepoModel>, ownerId: Long)
}