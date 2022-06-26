package com.example.github.vm.profile

import androidx.lifecycle.viewModelScope
import com.example.github.data.LoginSession
import com.example.github.data.ReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.data.local.DaoProvider
import com.example.github.model.RepoModel
import com.example.github.util.ProfileHelper
import com.example.github.util.log.AppLogger
import com.example.github.vm.BaseApiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userData: UserData,
    private val reposRepository: ReposRepository = ReposRepository(userData)
) : BaseApiViewModel<List<RepoData>, List<RepoModel>>(TAG, ProfileHelper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "ProfileVM"
    }

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    private val _starredRepos: MutableStateFlow<List<RepoData>?> = MutableStateFlow(null)
    val starredRepos: StateFlow<List<RepoData>?> = _starredRepos

    init {
        viewModelScope.launch {
            reposRepository.localRepos.collectLatest { repos ->
                AppLogger.log(tag, "Local repos changed: ${repos.size}")
                _data.value = mapper(repos)
            }
        }

        viewModelScope.launch {
            reposRepository.starredRepos.collectLatest { repos ->
                AppLogger.log(tag, "Starred repos changed: ${repos.size}")
                _starredRepos.value = mapper(repos)
            }
        }

        updateRepos()
    }

    fun updateRepos() {
        AppLogger.log(tag, "Get repos")
        fetchData { reposRepository.updateRepos(userData.username) }
    }

    fun logOut() {
        AppLogger.log(tag, "Log out")

        LoginSession.clean()
        CoroutineScope(dispatcher).launch { DaoProvider.clean() }

        _isLoggedOut.value = true
    }
}