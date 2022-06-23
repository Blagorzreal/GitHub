package com.example.github.data.remote.profile

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
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

    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> {
        AppLogger.log(TAG, "Fetch repos")

        return try {
            profileApi.getRepos(username).let {
                if (!it.isSuccessful) {
                    AppLogger.log(TAG, "Unable to fetch repos: ${it.code()}", LogType.Warning)
                    return@let ResponseResult.UnsuccessfulResponseError(it.code())
                }

                val repos = it.body()
                if (repos != null) {
                    AppLogger.log(TAG, "Fetched repos successfully: ${repos.size}")
                    ResponseResult.Success(repos)
                } else {
                    AppLogger.log(TAG, "Null repos", LogType.Warning)
                    ResponseResult.NullBodyResponseError
                }
            }
        } catch (ex: Exception) {
            AppLogger.log(TAG, "Unable to fetch repos: ${ex.message}", LogType.Error)
            ResponseResult.ExceptionResponseError(ex)
        }
    }
}