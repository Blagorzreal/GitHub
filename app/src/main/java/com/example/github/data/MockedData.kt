package com.example.github.data

import com.example.github.model.RepoModel
import com.example.github.model.UserModel
import com.example.github.model.relation.RepoWithOwnerRelation

class MockedData private constructor() {
    companion object {
        val users by lazy {
            mutableListOf(
                UserModel(1, "test", 12, 7, null),
                UserModel(2, "test2", 5, 4, null),
                UserModel(3, "test3", 7, 3, null),
                UserModel(4, "test4", 9, 0, null),
                UserModel(5, "test5", 3, 9, null),
                UserModel(6, "test6", 4, 11, null),
                UserModel(7, "test7", 9, 0, null),
                UserModel(8, "test8", 3, 9, null),
                UserModel(9, "test9", 4, 11, null),
                UserModel(10, "test10", 3, 9, null),
                UserModel(11, "test11", 4, 11, null)
            )
        }

        val repos by lazy {
            mutableListOf(
                RepoModel(1, users[3].id, "repo1"),
                RepoModel(2, users[1].id, "repo2"),
                RepoModel(3, users[0].id, "repo3", true),
                RepoModel(4, users[5].id, "repo4"),
                RepoModel(5, users[6].id, "repo5"),
                RepoModel(6, users[3].id, "repo6"),
                RepoModel(7, users[0].id, "repo7", true),
                RepoModel(8, users[3].id, "repo8")
            )
        }

        val reposWithOwner by lazy {
            mutableListOf(
                RepoWithOwnerRelation(repos[0], users[3]),
                RepoWithOwnerRelation(repos[1], users[1]),
                RepoWithOwnerRelation(repos[3], users[0]),
                RepoWithOwnerRelation(repos[4], users[5]),
                RepoWithOwnerRelation(repos[5], users[8]),
                RepoWithOwnerRelation(repos[6], users[3]),
                RepoWithOwnerRelation(repos[7], users[0]),
            )
        }
    }
}