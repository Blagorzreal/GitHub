package com.example.github.data.remote.auth

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class AuthApi: IAuthApi {
    companion object {
        private const val TAG = "AuthApi"
        private const val USERNAME = "username"
    }

    private interface AuthApi {
        @GET("/users/{$USERNAME}")
        suspend fun login(@Path(USERNAME) username: String): Response<UserModel>
    }

    private val authApi: AuthApi by lazy {
        ApiProvider.retrofit.create(AuthApi::class.java)
    }

    override suspend fun login(username: String): ResponseResult<UserModel> =
        ApiProvider.requestUnsafe(TAG, authApi.login(username)) {
            (it?.login?.isNotBlank() == true)
        }
}