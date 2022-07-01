package com.example.github.data.local.repos

import com.example.github.data.MockedData.Companion.repos
import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockedReposDao: IReposDao {
    override fun getAll(ownerId: Long): Flow<List<RepoModel>> = flow { emit(repos) }

    override fun getAllStarred(): Flow<List<RepoModel>> =
        flow { emit(repos.filter { it.starred }) }

    override suspend fun insertRepo(repo: RepoModel): Long = -1

    override suspend fun insertRepos(repos: List<RepoModel>) {
    }

    override suspend fun updateName(id: Long, name: String) {
    }

    override suspend fun updateStarred(id: Long, starred: Int) {
    }

    override suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>) {
    }

    override suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long) {
    }

    override suspend fun deleteAll() {
        repos.clear()
    }
}