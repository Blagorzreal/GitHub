package com.example.github.util.mapper

import com.example.github.data.data.RepoData
import com.example.github.model.RepoModel

class RepoModelMapper private constructor() {
    companion object {
        fun repoModelListToRepoDataSet(repos: List<RepoModel>) = repos.map { repoModelToRepoData(it) }.toSet()

        private fun repoModelToRepoData(repoModel: RepoModel): RepoData {
            return RepoData(
                repoModel.id,
                repoModel.name,
                UserModelMapper.userModelToUserData(repoModel.owner),
                repoModel.starred)
        }
    }
}