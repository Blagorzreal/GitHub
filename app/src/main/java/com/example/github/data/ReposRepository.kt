package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.remote.ResponseResult
import com.example.github.data.remote.repos.IReposApi
import com.example.github.model.RepoModel
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

    protected open val tag  = "Repos repo"

    @AssistedFactory
    interface ReposRepositoryModuleFactory {
        fun create(userData: UserData): ReposRepository
    }

    @Inject lateinit var reposDao: IReposDao
    @Inject lateinit var reposApi: IReposApi

    val localRepos by lazy { reposDao.getAll(userData.id) }

    suspend fun updateRepos(): ResponseResult<List<RepoModel>> {
        AppLogger.log(tag, "Update repos")

        val result = reposApi.getRepos(userData.username)
        if (result is ResponseResult.Success) {
            AppLogger.log(tag, "Insert remote repos to the db")
            reposDao.deleteAllByOwnerAndInsert(result.model, userData.id)
        }

        return result
    }
}