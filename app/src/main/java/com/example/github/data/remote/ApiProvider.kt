package com.example.github.data.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.auth.AuthApi
import com.example.github.data.remote.profile.ProfileApi
import com.example.github.util.log.AppLogger
import com.example.github.util.log.LogType
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiProvider private constructor() {
    companion object {
        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder().build()
        }

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(okHttpClient)
                .baseUrl(BuildConfig.GITHUB_API_URL)
                .build()
        }

        fun <T> fetch(
            tag: String,
            response: Response<T>,
            validateData: ((data: T?) -> Boolean)? = null): ResponseResult<T> {

            try {
                if (!response.isSuccessful) {
                    AppLogger.log(tag, "Unable to fetch: ${response.code()}", LogType.Warning)
                    return ResponseResult.UnsuccessfulResponseError(response.code())
                }

                val result = response.body() ?: return ResponseResult.NullBodyResponseError

                return if ((validateData == null) || validateData(result)) {
                    AppLogger.log(tag, "Fetch successfully")
                    return ResponseResult.Success(result)
                } else {
                    AppLogger.log(tag, "Unable to fetch since invalid data", LogType.Warning)
                    ResponseResult.InvalidResponseError
                }
            } catch (ex: Exception) {
                AppLogger.log(tag, "Unable to fetch: ${ex.message}", LogType.Error)
                return ResponseResult.ExceptionResponseError(ex)
            }
        }

        val authApi by lazy { AuthApi() }

        val profileApi by lazy { ProfileApi() }
    }
}