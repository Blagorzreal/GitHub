package com.example.github.data.remote.followers

import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import com.example.github.util.Constants.Companion.USERNAME
import com.example.github.util.Constants.Companion.USERS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class FollowersApi: IFollowersApi {
    companion object {
        private const val TAG = "Followers api"
    }

    private interface FollowersApiRetrofit {
        @GET("/$USERS/{$USERNAME}/followers")
        suspend fun followers(@Path(USERNAME) username: String): Response<List<UserModel>>
    }

    private val followersApi: FollowersApiRetrofit by lazy {
        ApiProvider.retrofit.create(FollowersApiRetrofit::class.java)
    }

    override suspend fun followers(username: String): ResponseResult<List<UserModel>> =
        ApiProvider.requestUnsafe(TAG, followersApi.followers(username))
}