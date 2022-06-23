package com.example.github.vm.profile

import androidx.lifecycle.viewModelScope
import com.example.github.data.LoginSession
import com.example.github.data.ProfileRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.model.RepoModel
import com.example.github.util.ProfileHelper
import com.example.github.util.log.AppLogger
import com.example.github.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userData: UserData,
    private val profileRepository: ProfileRepository = ProfileRepository()
)
    : BaseViewModel<List<RepoData>, List<RepoModel>>(TAG, ProfileHelper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "ProfileVM"
    }

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    init {
        viewModelScope.launch {
            profileRepository.localRepos.collectLatest { repos ->
                AppLogger.log(tag, "Local repos changed: ${repos.size}")
                _data.value = mapper(repos)
            }
        }

        updateRepos()
    }

    fun updateRepos() {
        AppLogger.log(tag, "Get repos")
        fetchData { profileRepository.updateRepos(userData.username) }
    }

    fun logOut() {
        AppLogger.log(tag, "Log out")
        LoginSession.clean()
        _isLoggedOut.value = true
    }
}