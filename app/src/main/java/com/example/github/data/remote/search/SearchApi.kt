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
        private const val IN_LOGIN = "in:login"
    }

    private interface SearchApiRetrofit {
        @GET("search/users?")
        suspend fun search(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int,
            @Query("q") username: String
        ): Response<SearchModel>
    }

    private val searchApi: SearchApiRetrofit by lazy {
        ApiProvider.retrofit.create(SearchApiRetrofit::class.java)
    }

    // Note: Url query is auto encoded by retrofit.
    override suspend fun search(
        usersPerPage: Int,
        page: Int,
        username: String
    ): ResponseResult<SearchModel> = ApiProvider.requestUnsafe(TAG, searchApi.search(usersPerPage, page, "$username $IN_LOGIN"))
}