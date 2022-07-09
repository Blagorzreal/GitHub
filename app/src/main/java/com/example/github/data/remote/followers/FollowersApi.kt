package com.example.github.data.remote.followers

import com.example.github.BuildConfig
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import com.example.github.util.Constants.Companion.USERNAME
import com.example.github.util.Constants.Companion.USERS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
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

    override suspend fun getFollowers(username: String): ResponseResult<List<UserModel>> =
        ApiProvider.requestUnsafe(TAG, { followersApi.followers(username) })

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideFollowersApi(): IFollowersApi = FollowersApi()
}