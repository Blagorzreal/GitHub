package com.example.github.data.remote.followers

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import kotlin.random.Random

class MockedFollowersAPi: IFollowersApi {
    override suspend fun followers(username: String): ResponseResult<List<UserModel>> {
        return ResponseResult.Success(
            listOf(UserModel(Random.nextLong(), "new1", 12, 5, null),
                UserModel(Random.nextLong(), "new2", 9, 5, null),
                UserModel(Random.nextLong(), "new3", 8, 3, null),
                UserModel(Random.nextLong(), "new4", 2, 5, null),
                UserModel(Random.nextLong(), "new5", 12, 1, null)))
    }
}