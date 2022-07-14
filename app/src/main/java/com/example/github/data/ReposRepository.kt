package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.repo.IRepoDao
import com.example.github.data.local.user.IUserDao
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.repos.IReposApi
import com.example.github.model.RepoModel
import com.example.github.model.UserModel
import com.example.github.model.relation.RepoWithOwnerRelation
import com.example.github.util.log.AppLogger
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
open class ReposRepository @AssistedInject constructor(@Assisted protected val userData: UserData) {

    protected open val tag = "Repos repo"

    @AssistedFactory
    interface ReposRepositoryModuleFactory {
        fun create(userData: UserData): ReposRepository
    }

    @Inject lateinit var repoDao: IRepoDao
    @Inject lateinit var userDao: IUserDao
    @Inject lateinit var reposApi: IReposApi

    val localRepos by lazy { repoDao.getAll(userData.id) }

    suspend fun updateRepos(): ResponseResult<List<RepoWithOwnerRelation?>> {
        AppLogger.log(tag, "Update repos")

        val result = reposApi.getRepos(userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(tag, "Insert remote repos to the db")

            val notNullModel = result.model.filterNotNull()
            userDao.insertUsers(notNullModel.map { it.owner }.toSet().toList())

            val userRepoHashMap = hashMapOf<UserModel, List<RepoModel>>()
            notNullModel.forEach {
                if (userRepoHashMap.containsKey(it.owner)) {
                    val repos = userRepoHashMap[it.owner]?.toMutableList()
                    if (repos.isNullOrEmpty())
                        userRepoHashMap[it.owner] = mutableListOf(it.repoModel)
                    else {
                        repos.add(it.repoModel)
                        userRepoHashMap[it.owner] = repos
                    }
                } else
                    userRepoHashMap[it.owner] = mutableListOf(it.repoModel)
            }

            userRepoHashMap.forEach {
                repoDao.deleteAllByOwnerAndInsert(it.value, it.key.id)
            }
        }

        return result
    }
}