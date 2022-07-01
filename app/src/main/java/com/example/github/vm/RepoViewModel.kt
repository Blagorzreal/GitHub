package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.RepoRepository
import com.example.github.data.data.RepoData
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModel(
    repoData: RepoData,
    private val repoRepository: RepoRepository = RepoRepository(repoData)
): BaseViewModel<Boolean>(TAG, false) {

    companion object {
        private const val TAG = "Repo repo"
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