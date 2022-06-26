package com.example.github.vm.repos.starred

import androidx.lifecycle.viewModelScope
import com.example.github.data.StarredReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.util.log.AppLogger
import com.example.github.vm.repos.ReposViewModelData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StarredReposViewModelData(
    userData: UserData,
    private val starredReposRepository: StarredReposRepository = StarredReposRepository(userData)
) : ReposViewModelData(TAG, userData, starredReposRepository) {

    companion object {
        private const val TAG = "Starred repos VM"
    }

    private val _starredRepos: MutableStateFlow<List<RepoData>?> = MutableStateFlow(null)
    val starredRepos: StateFlow<List<RepoData>?> = _starredRepos

    init {
        viewModelScope.launch {
            starredReposRepository.starredRepos.collectLatest { repos ->
                AppLogger.log(tag, "Starred repos changed: ${repos.size}")
                _starredRepos.value = mapper(repos)
            }
        }
    }
}