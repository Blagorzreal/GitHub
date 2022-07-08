package com.example.github.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.RepoRepository
import com.example.github.data.data.RepoData
import com.example.github.ui.navigation.Route
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repoRepositoryModuleFactory: RepoRepository.RepoRepositoryModuleFactory
): BaseViewModel<Boolean>(false) {

    override val tag = "Repo repo"

    private val repoRepository: RepoRepository by lazy {
        repoRepositoryModuleFactory.create(savedStateHandle.get<RepoData>(Route.REPO_DATA) ?: throw Exception())
    }

    val starred: StateFlow<Boolean> = repoRepository.starred

    fun updateStarred() {
        AppLogger.log(tag, "Update starred")

        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(dispatcher) {
                repoRepository.updateStarred()
            }

            _isLoading.value = false

            if (!result)
                _error.value = true
        }
    }
}