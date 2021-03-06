package com.example.github.data.remote.search

import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel

interface ISearchApi {
    suspend fun search(usersPerPage: Int, page: Int, username: String): ResponseResult<SearchModel>
}