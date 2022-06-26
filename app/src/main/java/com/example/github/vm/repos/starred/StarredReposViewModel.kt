package com.example.github.vm.repos.starred

import androidx.lifecycle.viewModelScope
import com.example.github.data.LoginSession
import com.example.github.data.ProfileRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.util.log.AppLogger
import com.example.github.vm.repos.ReposViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StarredReposViewModel(
    userData: UserData,
    private val profileRepository: ProfileRepository = ProfileRepository(userData)
) : ReposViewModel(TAG, userData, profileRepository) {

    companion object {
        private const val TAG = "Profile VM"
    }

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    private val _starredRepos: MutableStateFlow<List<RepoData>?> = MutableStateFlow(null)
    val starredRepos: StateFlow<List<RepoData>?> = _starredRepos

    init {
        viewModelScope.launch {
            profileRepository.starredRepos.collectLatest { repos ->
                AppLogger.log(tag, "Starred repos changed: ${repos.size}")
                _starredRepos.value = mapper(repos)
            }
        }
    }

    fun logOut() {
        AppLogger.log(tag, "Log out")

        LoginSession.clean()
        CoroutineScope(dispatcher).launch { DaoProvider.clean() }

        _isLoggedOut.value = true
    }
}