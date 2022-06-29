package com.example.github.data.remote.search

import com.example.github.data.MockedData
import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel

class MockedSearchApi: ISearchApi {
    override suspend fun search(
        usersPerPage: Int,
        page: Int,
        username: String
    ): ResponseResult<SearchModel> =
        ResponseResult.Success(
            SearchModel(
                5, arrayListOf(
                    MockedData.users[0],
                    MockedData.users[1],
                    MockedData.users[2],
                    MockedData.users[3],
                    MockedData.users[4]
                )
            )
        )
}