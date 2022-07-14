package com.example.github.data

import com.example.github.model.RepoModel
import com.example.github.model.UserModel

class MockedData private constructor() {
    companion object {
        val users = mutableListOf(
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

        val repos = mutableListOf<RepoModel>()
    }
}