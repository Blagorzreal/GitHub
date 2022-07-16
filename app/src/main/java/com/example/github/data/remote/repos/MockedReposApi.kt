package com.example.github.data.remote.repos

import com.example.github.BuildConfig
import com.example.github.data.MockedData
import com.example.github.data.remote.ResponseResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MockedReposApi: IReposApi {
    override suspend fun getRepos(username: String) =
        ResponseResult.Success(
            listOf(
                MockedData.reposWithOwner[0],
                MockedData.reposWithOwner[1],
                MockedData.reposWithOwner[2],
                MockedData.reposWithOwner[3],
                MockedData.reposWithOwner[4]
            )
        )

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideReposApi(): IReposApi = MockedReposApi()
}