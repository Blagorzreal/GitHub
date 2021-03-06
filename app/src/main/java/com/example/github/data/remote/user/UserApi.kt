package com.example.github.data.remote.user

import com.example.github.BuildConfig
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
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
class UserApi: IUserApi {
    companion object {
        private const val USERNAME = "username"
        private const val TAG = "UserApi"
    }

    private interface UserApiRetrofit {
        @GET("/users/{$USERNAME}")
        suspend fun getUser(@Path(USERNAME) username: String): Response<UserModel>
    }

    private val userApi: UserApiRetrofit by lazy {
        ApiProvider.retrofit.create(UserApiRetrofit::class.java)
    }

    override suspend fun getUser(username: String): ResponseResult<UserModel> =
        ApiProvider.requestUnsafe(TAG, { userApi.getUser(username) })

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideUserApi(): IUserApi = UserApi()
}