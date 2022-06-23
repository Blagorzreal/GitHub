package com.example.github.data.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.auth.AuthApi
import com.example.github.data.remote.profile.ProfileApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
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

        val authApi by lazy { AuthApi() }

        val profileApi by lazy { ProfileApi() }
    }
}