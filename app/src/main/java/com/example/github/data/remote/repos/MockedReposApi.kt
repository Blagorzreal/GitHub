package com.example.github.data.remote.repos

import com.example.github.BuildConfig
import com.example.github.data.MockedData
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel
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
    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> =
        ResponseResult.Success(
            listOf(
                MockedData.repos[0],
                MockedData.repos[1],
                MockedData.repos[2],
                MockedData.repos[3],
                MockedData.repos[4]
            )
        )

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideReposApi(): IReposApi = MockedReposApi()
}