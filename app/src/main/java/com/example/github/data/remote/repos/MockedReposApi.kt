package com.example.github.data.remote.repos

import com.example.github.data.MockedData
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel

class MockedReposApi: IReposApi {
    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> =
        ResponseResult.Success(
            listOf(
                MockedData.repos[0],
                MockedData.repos[1],
                MockedData.repos[2],
                MockedData.repos[3],
                MockedData.repos[4]
            )
        )
}