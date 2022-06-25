package com.example.github.data.local.repos

import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

interface IReposDao {
    fun getAll(ownerId: Long): Flow<List<RepoModel>>
    fun getAllStarred(): Flow<List<RepoModel>>
    suspend fun insertRepos(repos: List<RepoModel>)
    suspend fun deleteAll()
    suspend fun deleteAllByOwner(ownerId: Long)
    suspend fun deleteAllAndInsertNew(repos: List<RepoModel>, ownerId: Long)
    suspend fun update(repoModelId: Long, starred: Int)
}