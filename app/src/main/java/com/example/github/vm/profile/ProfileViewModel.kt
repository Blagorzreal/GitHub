package com.example.github.vm.profile

import androidx.lifecycle.viewModelScope
import com.example.github.data.LoginSession
import com.example.github.data.ProfileRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.model.RepoModel
import com.example.github.util.ProfileHelper
import com.example.github.util.log.AppLogger
import com.example.github.vm.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val userData: UserData,
    private val profileRepository: ProfileRepository = ProfileRepository(userData)
) : BaseViewModel<List<RepoData>, List<RepoModel>>(TAG, ProfileHelper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "ProfileVM"
    }

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    private val _starredRepos: MutableStateFlow<List<RepoData>?> = MutableStateFlow(null)
    val starredRepos: StateFlow<List<RepoData>?> = _starredRepos

    private val _updateStarredFailed = MutableStateFlow<RepoData?>(null)
    val updateStarredFailed: StateFlow<RepoData?> = _updateStarredFailed

    init {
        viewModelScope.launch {
            profileRepository.localRepos.collectLatest { repos ->
                AppLogger.log(tag, "Local repos changed: ${repos.size}")
                _data.value = mapper(repos)
            }
        }

        viewModelScope.launch {
            profileRepository.starredRepos.collectLatest { repos ->
                AppLogger.log(tag, "Starred repos changed: ${repos.size}")
                _starredRepos.value = mapper(repos)
            }
        }

        updateRepos()
    }

    fun updateStarred(repoData: RepoData, starred: Boolean) {
        AppLogger.log(tag, "updateStarred")

        viewModelScope.launch {
            val result = withContext(coroutineScope) {
                profileRepository.updateStarred(repoData, starred)
            }

            if (result)
                repoData.starred = starred
            else
                _updateStarredFailed.value = repoData
        }
    }

    fun updateRepos() {
        AppLogger.log(tag, "Get repos")
        fetchData { profileRepository.updateRepos(userData.username) }
    }

    fun logOut() {
        AppLogger.log(tag, "Log out")

        LoginSession.clean()
        CoroutineScope(coroutineScope).launch { DaoProvider.clean() }

        _isLoggedOut.value = true
    }
}