package com.example.github.data.remote.profile

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class ProfileApi: IProfileApi {
    companion object {
        private const val USERNAME = "username"
        private const val TAG = "ProfileApi"
    }

    private interface RetrofitProfileApi {
        @GET("/users/{$USERNAME}/repos")
        suspend fun getRepos(@Path(USERNAME) username: String): Response<List<RepoModel>>
    }

    private val profileApi: RetrofitProfileApi by lazy {
        ApiProvider.retrofit.create(RetrofitProfileApi::class.java)
    }

    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> =
        ApiProvider.fetch(TAG, profileApi.getRepos(username))
}