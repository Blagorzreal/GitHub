package com.example.github.data.remote.search

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

class SearchApi: ISearchApi {
    companion object {
        private const val TAG = "SearchApi"
        private const val IN_NAME = "in:name"
    }

    private interface SearchRetrofitApi {
        @GET("search/users?")
        suspend fun search(
            @Query("per_page") perPage: Int = 100,
            @Query("q") text: String): Response<SearchModel>
    }

    private val searchApi: SearchRetrofitApi by lazy {
        ApiProvider.retrofit.create(SearchRetrofitApi::class.java)
    }

    // Note: Url query is auto encoded by retrofit.
    override suspend fun search(text: String): ResponseResult<SearchModel> =
        ApiProvider.requestUnsafe(TAG, searchApi.search(text = "$text $IN_NAME"))
}