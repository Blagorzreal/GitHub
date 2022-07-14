package com.example.github.data.local.repo

import com.example.github.model.RepoModel
import com.example.github.model.relation.RepoWithOwnerRelation
import kotlinx.coroutines.flow.Flow

interface IRepoDao {
    fun getAll(ownerId: Long): Flow<List<RepoWithOwnerRelation>>
    fun getAllStarred(): Flow<List<RepoWithOwnerRelation>>
    suspend fun insertRepo(repo: RepoModel): Long
    suspend fun insertRepos(repos: List<RepoModel>)
    suspend fun updateName(id: Long, name: String)
    suspend fun updateStarred(id: Long, starred: Int)
    suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>)
    suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long)
    suspend fun deleteAll()
}