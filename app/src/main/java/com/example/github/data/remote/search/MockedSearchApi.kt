package com.example.github.data.remote.search

import com.example.github.BuildConfig
import com.example.github.data.MockedData
import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MockedSearchApi: ISearchApi {
    override suspend fun search(
        usersPerPage: Int,
        page: Int,
        username: String
    ): ResponseResult<SearchModel> =
        ResponseResult.Success(
            SearchModel(
                5, arrayListOf(
                    MockedData.users[0],
                    MockedData.users[1],
                    MockedData.users[2],
                    MockedData.users[3],
                    MockedData.users[4]
                )
            )
        )

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideSearchApi(): ISearchApi = MockedSearchApi()
}