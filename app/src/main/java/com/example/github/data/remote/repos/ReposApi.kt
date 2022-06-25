package com.example.github.data.remote.repos

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class ReposApi: IReposApi {
    companion object {
        private const val USERNAME = "username"
        private const val TAG = "ReposApi"
    }

    private interface RetrofitReposApi {
        @GET("/users/{$USERNAME}/repos")
        suspend fun getRepos(@Path(USERNAME) username: String): Response<List<RepoModel>>
    }

    private val reposApi: RetrofitReposApi by lazy {
        ApiProvider.retrofit.create(RetrofitReposApi::class.java)
    }

    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> =
        ApiProvider.requestUnsafe(TAG, reposApi.getRepos(username))
}