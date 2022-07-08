package com.example.github.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.ReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.model.RepoModel
import com.example.github.ui.navigation.Route
import com.example.github.util.mapper.RepoModelMapper
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ReposViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    reposRepositoryModuleFactory: ReposRepository.ReposRepositoryModuleFactory)
    : BaseApiViewModel<List<RepoData>, List<RepoModel>>(TAG, RepoModelMapper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "User VM"
    }

    private val reposRepository: ReposRepository by lazy {
        reposRepositoryModuleFactory.create(savedStateHandle.get<UserData>(Route.USER_DATA) ?: throw Exception())
    }

    init {
        viewModelScope.launch {
            reposRepository.localRepos.collectLatest {
                AppLogger.log(tag, "Local repos changed: ${it.size}")
                _data.value = mapper(it)
            }
        }

        updateRepos()
    }

    fun updateRepos() {
        AppLogger.log(tag, "Update repos")
        fetchData { reposRepository.updateRepos() }
    }

    override fun onData(data: List<RepoData>) {
        if (_data.value.isNullOrEmpty())
            _data.value = data.sortedBy { it.id }
    }
}