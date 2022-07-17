package com.example.github.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.StarredReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.ui.navigation.Route
import com.example.github.util.CommonHelper
import com.example.github.util.log.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredReposViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    starredReposRepositoryModuleFactory: StarredReposRepository.StarredReposRepositoryModuleFactory
): ReposViewModel(savedStateHandle, starredReposRepositoryModuleFactory) {

    override val tag = "Starred repos VM"

    private val starredReposRepository: StarredReposRepository by lazy {
        starredReposRepositoryModuleFactory.create(savedStateHandle.get<UserData>(Route.USER_DATA)
            ?: throw CommonHelper.missingVMParameterException(tag, Route.USER_DATA))
    }

    val starredRepos = mutableStateListOf<RepoData>()

    init {
        viewModelScope.launch {
            starredReposRepository.starredRepos.collectLatest {
                AppLogger.log(tag, "Starred repos changed: ${it.size}")
                starredRepos.clear()
                starredRepos.addAll(mapper(it))
            }
        }
    }
}