package com.example.github.data.remote.user

import com.example.github.BuildConfig
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockedUserApi: IUserApi {
    override suspend fun getUser(username: String): ResponseResult<UserModel> =
        ResponseResult.Success(UserModel(58, username, 12, 6, null))

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideUserApi(): IUserApi = MockedUserApi()
}