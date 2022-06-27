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
    }

    private interface SearchRetrofitApi {
        @GET("search/users?")
        suspend fun search(@Query("q") username: String): Response<SearchModel>
    }

    private val searchApi: SearchRetrofitApi by lazy {
        ApiProvider.retrofit.create(SearchRetrofitApi::class.java)
    }

    override suspend fun search(username: String): ResponseResult<SearchModel> =
        ApiProvider.requestUnsafe(TAG, searchApi.search(username))
}