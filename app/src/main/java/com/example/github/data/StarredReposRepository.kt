package com.example.github.data

import com.example.github.data.data.UserData
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.remote.repos.IReposApi
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class StarredReposRepository @AssistedInject constructor(
    @Assisted userData: UserData,
    reposDao: IReposDao,
    reposApi: IReposApi
): ReposRepository(
    userData,
    reposDao,
    reposApi
) {
    @AssistedFactory
    interface StarredReposRepositoryModuleFactory: ReposRepositoryModuleFactory {
        override fun create(userData: UserData): StarredReposRepository
    }

    companion object {
        private const val TAG = "Starred repos repo"
    }

    val starredRepos = reposDao.getAllStarred()
}