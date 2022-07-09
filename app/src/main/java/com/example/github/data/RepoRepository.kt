package com.example.github.data

import com.example.github.data.data.RepoData
import com.example.github.data.local.repos.IReposDao
import com.example.github.util.log.AppLogger
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class RepoRepository @AssistedInject constructor (@Assisted val repoData: RepoData) {

    companion object {
        private const val TAG = "Repo repo"
    }

    @AssistedFactory
    interface RepoRepositoryModuleFactory {
        fun create(repoData: RepoData): RepoRepository
    }

    @Inject lateinit var reposDao: IReposDao

    private val _starred: MutableStateFlow<Boolean> = MutableStateFlow(repoData.starred)
    val starred: StateFlow<Boolean> = _starred

    suspend fun updateStarred(): Boolean {
        val repoDataId = repoData.id
        val starred = !_starred.value

        AppLogger.log(TAG, "Prepare for repo update with $repoDataId to starred=$starred")

        return try {
            reposDao.updateStarred(repoDataId, if (starred) 1 else 0)

            repoData.starred = starred
            _starred.value = starred

            AppLogger.log(TAG, "Updated repo $repoDataId")
            true
        } catch (error: Exception) {
            AppLogger.log(TAG, "Unable to update repo $repoDataId: ${error.message}")
            false
        }
    }
}