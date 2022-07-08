package com.example.github.data

import com.example.github.data.data.UserData
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class StarredReposRepository @AssistedInject constructor(@Assisted userData: UserData)
    : ReposRepository(userData) {

    @AssistedFactory
    interface StarredReposRepositoryModuleFactory: ReposRepositoryModuleFactory {
        override fun create(userData: UserData): StarredReposRepository
    }

    override val tag = "Starred repos repo"

    val starredRepos by lazy { reposDao.getAllStarred() }
}