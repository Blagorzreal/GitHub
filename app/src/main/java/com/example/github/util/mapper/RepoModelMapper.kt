package com.example.github.util.mapper

import com.example.github.data.data.RepoData
import com.example.github.model.relation.RepoWithOwnerRelation

class RepoModelMapper private constructor() {
    companion object {
        fun repoWithOwnerRelationToRepoDataList(repos: List<RepoWithOwnerRelation?>) =
            repos.mapNotNull { repoWithOwnerRelationToRepoData(it) }

        private fun repoWithOwnerRelationToRepoData(repoModel: RepoWithOwnerRelation?): RepoData? {
            if (repoModel == null)
                return null

            return RepoData(
                repoModel.repoModel.id,
                repoModel.repoModel.name,
                UserModelMapper.userModelToUserData(repoModel.owner),
                repoModel.repoModel.starred)
        }
    }
}