package com.example.github.data.local.repo

import com.example.github.BuildConfig
import com.example.github.data.MockedData.Companion.repos
import com.example.github.data.MockedData.Companion.reposWithOwner
import com.example.github.model.RepoModel
import com.example.github.model.relation.RepoWithOwnerRelation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockedRepoDao: IRepoDao {

    private val starredRepos = MutableStateFlow(reposWithOwner.filter { it.repoModel.starred })

    override fun getAll(ownerId: Long): Flow<List<RepoWithOwnerRelation>> =
        flow { emit(reposWithOwner.filter { it.owner.id == ownerId }) }

    override fun getAllStarred(): Flow<List<RepoWithOwnerRelation>> = starredRepos

    override suspend fun insertRepo(repo: RepoModel): Long = -1

    override suspend fun insertRepos(repos: List<RepoModel>) {
    }

    override suspend fun updateName(id: Long, name: String) {
    }

    override suspend fun updateStarred(id: Long, starred: Int) {
        val index = reposWithOwner.indexOfFirst { it.repoModel.id == id }
        if (index >= 0) {
            reposWithOwner[index] =
                reposWithOwner[index].copy(
                    repoModel = reposWithOwner[index].repoModel.copy(
                        starred = starred == 1
                    )
                )
        }

        starredRepos.emit(reposWithOwner.filter { it.repoModel.starred })
    }

    override suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>) {
    }

    override suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long) {
    }

    override suspend fun deleteAll() {
        repos.clear()
        reposWithOwner.clear()
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideRepoDao(): IRepoDao = MockedRepoDao()
}