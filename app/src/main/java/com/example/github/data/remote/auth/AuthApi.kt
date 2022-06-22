package com.example.github.data.remote.auth

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
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

    override suspend fun login(username: String): ResponseResult<UserModel> {
        AppLogger.log(TAG, "Login: $username")

        return try {
            authApi.login(username).let {
                if (!it.isSuccessful) {
                    AppLogger.log(TAG, "Unable to login: ${it.code()}", LogType.Warning)
                    return@let ResponseResult.UnsuccessfulResponseError(it.code())
                }

                val result = it.body()
                return@let if (result?.login?.isNotBlank() == true) {
                    AppLogger.log(TAG, "Logged in successfully")
                    ResponseResult.Success(result)
                } else {
                    AppLogger.log(TAG, "Unable to login since missing data", LogType.Warning)
                    ResponseResult.EmptyResponseError
                }
            }
        } catch (ex: Exception) {
            AppLogger.log(TAG, "Unable to login: ${ex.message}", LogType.Error)
            ResponseResult.ExceptionResponseError(ex)
        }
    }
}