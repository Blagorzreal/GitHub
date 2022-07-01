package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.StarredReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StarredReposViewModel(
    userData: UserData,
    private val starredReposRepository: StarredReposRepository = StarredReposRepository(userData)
) : ReposViewModel(TAG, userData, starredReposRepository) {

    companion object {
        private const val TAG = "Starred repos VM"
    }

    private val _starredRepos: MutableStateFlow<Set<RepoData>> = MutableStateFlow(emptySet())
    val starredRepos: StateFlow<Set<RepoData>?> = _starredRepos

    init {
        viewModelScope.launch {
            starredReposRepository.starredRepos.collectLatest {
                AppLogger.log(tag, "Starred repos changed: ${it.size}")
                _starredRepos.value = mapper(it)
            }
        }
    }
}