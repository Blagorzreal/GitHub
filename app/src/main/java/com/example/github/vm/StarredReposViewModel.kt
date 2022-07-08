package com.example.github.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.StarredReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.util.log.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredReposViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    starredReposRepositoryModuleFactory: StarredReposRepository.StarredReposRepositoryModuleFactory
): ReposViewModel(savedStateHandle, starredReposRepositoryModuleFactory) {
    companion object {
        private const val TAG = "Starred repos VM"
    }

    private val starredReposRepository: StarredReposRepository by lazy {
        starredReposRepositoryModuleFactory.create(savedStateHandle.get<UserData>(Route.USER_DATA) ?: throw Exception())
    }

    private val _starredRepos: MutableStateFlow<List<RepoData>> = MutableStateFlow(emptyList())
    val starredRepos: StateFlow<List<RepoData>> = _starredRepos

    init {
        viewModelScope.launch {
            starredReposRepository.starredRepos.collectLatest {
                AppLogger.log(tag, "Starred repos changed: ${it.size}")
                _starredRepos.value = mapper(it)
            }
        }
    }
}