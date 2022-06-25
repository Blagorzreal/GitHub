package com.example.github.vm.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.data.RepoRepository
import com.example.github.data.data.RepoData
import com.example.github.util.log.AppLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryViewModel(
    private val repoData: RepoData,
    private val repoRepository: RepoRepository = RepoRepository(repoData),
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    companion object {
        private const val TAG = "Repo repository"
    }

    private val _starred = MutableStateFlow(repoData.starred)
    val starred: StateFlow<Boolean> = _starred

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun updateStarred() {
        AppLogger.log(TAG, "updateStarred")

        viewModelScope.launch {
            val result = withContext(coroutineScope) {
                repoRepository.updateStarred()
            }

            if (result)
                _starred.value = repoData.starred
            else
                _error.value = true
        }
    }
}