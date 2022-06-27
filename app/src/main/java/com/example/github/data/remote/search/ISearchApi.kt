package com.example.github.data.remote.search

import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel

interface ISearchApi {
    suspend fun search(username: String): ResponseResult<SearchModel>
}