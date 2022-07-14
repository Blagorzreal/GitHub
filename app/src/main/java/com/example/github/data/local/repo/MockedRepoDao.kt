package com.example.github.data.local.repo

import com.example.github.BuildConfig
import com.example.github.data.MockedData.Companion.repos
import com.example.github.model.RepoModel
import com.example.github.model.relation.RepoWithOwnerRelation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockedRepoDao: IRepoDao {
    override fun getAll(ownerId: Long): Flow<List<RepoWithOwnerRelation>> {
        TODO("Not yet implemented")
    }

    override fun getAllStarred(): Flow<List<RepoWithOwnerRelation>> =
        TODO("Not yet implemented")

    override suspend fun insertRepo(repo: RepoModel): Long = -1

    override suspend fun insertRepos(repos: List<RepoModel>) {
    }

    override suspend fun updateName(id: Long, name: String) {
    }

    override suspend fun updateStarred(id: Long, starred: Int) {
    }

    override suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long) {
        TODO("Not yet implemented")
    }

    /*override suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>) {
    }*/

    /*override suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long) {
    }*/

    override suspend fun deleteAll() {
        repos.clear()
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideRepoDao(): IRepoDao = MockedRepoDao()
}