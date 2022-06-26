package com.example.github.vm.user

import androidx.lifecycle.viewModelScope
import com.example.github.data.UserReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.model.RepoModel
import com.example.github.util.ProfileHelper
import com.example.github.util.log.AppLogger
import com.example.github.vm.BaseApiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class UserViewModel(
    override val tag: String = TAG,
    private val userData: UserData,
    private val reposRepository: UserReposRepository = UserReposRepository(owner = userData)
): BaseApiViewModel<List<RepoData>, List<RepoModel>>(tag, ProfileHelper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "User VM"
    }

    init {
        viewModelScope.launch {
            reposRepository.localRepos.collectLatest { repos ->
                AppLogger.log(tag, "Local repos changed: ${repos.size}")
                _data.value = mapper(repos)
            }
        }

        updateRepos()
    }

    fun updateRepos() {
        AppLogger.log(tag, "Get repos")
        fetchData { reposRepository.updateRepos(userData.username) }
    }
}