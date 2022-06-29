package com.example.github.data.local.repos

import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

interface IReposDao {
    fun getAll(ownerId: Long): Flow<List<RepoModel>>
    fun getAllStarred(): Flow<List<RepoModel>>
    suspend fun insertRepo(repo: RepoModel): Long
    suspend fun insertRepos(repos: List<RepoModel>)
    suspend fun updateName(id: Long, name: String)
    suspend fun updateStarred(id: Long, starred: Int)
    suspend fun deleteAllNotStarredByOwner(ownerId: Long, ids: List<Long>)
    suspend fun deleteAllAndInsertNew(repos: List<RepoModel>, ownerId: Long)
    suspend fun deleteAll()
}