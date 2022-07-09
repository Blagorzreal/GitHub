package com.example.github.data.remote.auth

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

@Module
@InstallIn(SingletonComponent::class)
class AuthApi: IAuthApi {
    companion object {
        private const val TAG = "AuthApi"
    }

    private interface AuthApiRetrofit {
        @GET("/$USERS/{$USERNAME}")
        suspend fun login(@Path(USERNAME) username: String): Response<UserModel>
    }

    private val authApi: AuthApiRetrofit by lazy {
        ApiProvider.retrofit.create(AuthApiRetrofit::class.java)
    }

    override suspend fun login(username: String): ResponseResult<UserModel> =
        ApiProvider.requestUnsafe(TAG, { authApi.login(username) }) {
            (it?.login?.isNotBlank() == true)
        }

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideAuthApi(): IAuthApi = AuthApi()
}