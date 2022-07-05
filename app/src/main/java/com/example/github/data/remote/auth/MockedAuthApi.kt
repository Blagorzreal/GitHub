package com.example.github.data.remote.auth

import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MockedAuthApi: IAuthApi {
    override suspend fun login(username: String): ResponseResult<UserModel> =
        ResponseResult.Success(UserModel(0, username, 8, 6, null))

    @Provides
    @Singleton
    @IntoMap
    @StringKey("mocked")
    fun provideMockedAuthApi(): IAuthApi = MockedAuthApi()
}