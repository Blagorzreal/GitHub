package com.example.github.vm.repo

import androidx.lifecycle.viewModelScope
import com.example.github.data.RepoRepository
import com.example.github.data.data.RepoData
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModelData(
    repoData: RepoData,
    private val repoRepository: RepoRepository = RepoRepository(repoData)
): BaseViewModel<Boolean>(TAG, false) {

    companion object {
        private const val TAG = "Repo repo"
    }

    private val _repoData = MutableStateFlow(repoRepository.repoData.value)
    val repoData: StateFlow<RepoData> = _repoData

    init {
        viewModelScope.launch {
            repoRepository.repoData.collectLatest {
                AppLogger.log(tag, "Starred repo changed: $it")
                _repoData.value = it
            }
        }
    }

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