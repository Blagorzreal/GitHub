package com.example.github.vm

import androidx.lifecycle.viewModelScope
import com.example.github.data.ReposRepository
import com.example.github.data.data.RepoData
import com.example.github.data.data.UserData
import com.example.github.model.RepoModel
import com.example.github.util.mapper.RepoModelMapper
import com.example.github.util.log.AppLogger
import com.example.github.vm.base.BaseApiViewModel
import com.example.github.vm.base.ProceededDataResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class ReposViewModel(
    tag: String = TAG,
    private val userData: UserData,
    private val reposRepository: ReposRepository = ReposRepository(userData = userData)
): BaseApiViewModel<List<RepoData>, List<RepoModel>>(tag, RepoModelMapper::repoModelListToRepoDataList) {

    companion object {
        private const val TAG = "User VM"
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
        fetchData { reposRepository.updateRepos(userData.username) }
    }

    override fun proceedData(data: List<RepoData>) =
        if (_data.value.isNullOrEmpty())
            ProceededDataResult.SetDataResult(data.sortedBy { it.id })
        else
            ProceededDataResult.IgnoreDataResult
}