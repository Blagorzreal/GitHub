package com.example.github.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.github.data.ReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.model.relation.RepoWithOwnerRelation
import com.example.github.ui.navigation.Route
import com.example.github.util.CommonHelper
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
    : BaseApiViewModel<List<RepoData>, List<RepoWithOwnerRelation?>>(RepoModelMapper::repoWithOwnerRelationToRepoDataList) {

    override val tag = "Repos VM"

    val repos = mutableStateListOf<RepoData>()

    private val reposRepository: ReposRepository by lazy {
        reposRepositoryModuleFactory.create(savedStateHandle.get<UserData>(Route.USER_DATA)
            ?: throw CommonHelper.missingVMParameterException(tag, Route.USER_DATA))
    }

    init {
        viewModelScope.launch {
            reposRepository.localRepos.collectLatest {
                AppLogger.log(tag, "Local repos changed: ${it.size}")

                repos.clear()
                repos.addAll(mapper(it))
            }
        }
    }

    fun updateRepos() {
        AppLogger.log(tag, "Update repos")
        fetchData { reposRepository.updateRepos() }
    }

    override fun onData(data: List<RepoData>) {
        if (repos.isEmpty())
            repos.addAll(data.sortedBy { it.id })
    }
}