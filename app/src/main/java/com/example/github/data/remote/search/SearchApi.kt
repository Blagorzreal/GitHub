package com.example.github.data.remote.search

import com.example.github.BuildConfig
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.SearchModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchApi: ISearchApi {
    companion object {
        private const val TAG = "SearchApi"
        private const val IN_LOGIN = "in:login"
    }

    private interface SearchApiRetrofit {
        @GET("search/users?")
        suspend fun search(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int,
            @Query("q") username: String
        ): Response<SearchModel>
    }

    private val searchApi: SearchApiRetrofit by lazy {
        ApiProvider.retrofit.create(SearchApiRetrofit::class.java)
    }

    // Note: Url query is auto encoded by retrofit.
    override suspend fun search(
        usersPerPage: Int,
        page: Int,
        username: String
    ): ResponseResult<SearchModel> = ApiProvider.requestUnsafe(
        TAG,
        { searchApi.search(usersPerPage, page, "$username $IN_LOGIN") }
    )

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideSearchApi(): ISearchApi = SearchApi()
}