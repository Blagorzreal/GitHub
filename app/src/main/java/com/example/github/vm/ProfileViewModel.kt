package com.example.github.vm

import com.example.github.data.LoginSession
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.profile.IProfileApi
import com.example.github.model.RepoModel
import com.example.github.util.ProfileHelper
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    private val userData: UserData,
    private val profileApi: IProfileApi = ApiProvider.profileApi)
    : BaseViewModel<List<RepoData>, List<RepoModel>>(ProfileHelper::repoModelListToRepoDataList, TAG) {

    companion object {
        private const val TAG = "ProfileVM"
    }

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    init {
        getRepos()
    }

    fun getRepos() {
        AppLogger.log(TAG, "Get repos")
        fetchData { profileApi.getRepos(userData.username) }
    }

    fun logOut() {
        LoginSession.clean()
        _isLoggedOut.value = true
    }
}