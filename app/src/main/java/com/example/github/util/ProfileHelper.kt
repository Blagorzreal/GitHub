package com.example.github.util

import com.example.github.data.data.RepoData
import com.example.github.model.RepoModel

class ProfileHelper private constructor() {
    companion object {
        fun repoModelListToRepoDataList(repos: List<RepoModel>) = repos.map { repoModelToRepoData(it) }

        private fun repoModelToRepoData(repoModel: RepoModel): RepoData {
            return RepoData(
                repoModel.id,
                repoModel.name,
                LoginHelper.userModelToUserData(repoModel.owner),
                repoModel.starred)
        }
    }
}