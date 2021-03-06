package com.example.github.data.remote

import com.example.github.BuildConfig
import com.example.github.model.relation.RepoWithOwnerRelation
import com.example.github.util.NetworkManager
import com.example.github.util.RepoModelDeserializer
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
                .addConverterFactory(createGsonConverterFactory())
                .client(okHttpClient)
                .baseUrl(BuildConfig.GITHUB_API_URL)
                .build()
        }

        private fun createGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(RepoWithOwnerRelation::class.java, RepoModelDeserializer())
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        suspend fun <T> requestUnsafe(
            tag: String,
            request: suspend () -> Response<T>,
            validateData: ((data: T?) -> Boolean)? = null): ResponseResult<T> {

            if (!NetworkManager.isInternetAvailable)
                return ResponseResult.NoInternetConnectionAvailable

            val response = request()
            if (!response.isSuccessful) {
                AppLogger.log(tag, "Bad response: ${response.code()}", LogType.Warning)
                return ResponseResult.UnsuccessfulResponseError(response.code())
            }

            val result = response.body() ?: return ResponseResult.NullBodyResponseError

            return if ((validateData == null) || validateData(result)) {
                AppLogger.log(tag, "Successful response")
                ResponseResult.Success(result)
            } else {
                AppLogger.log(tag, "Invalid response", LogType.Warning)
                ResponseResult.InvalidResponseError
            }
        }
    }
}