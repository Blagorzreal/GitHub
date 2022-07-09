package com.example.github.data.remote.repos

import com.example.github.BuildConfig
import com.example.github.data.remote.ApiProvider
import com.example.github.data.remote.ResponseResult
import com.example.github.model.RepoModel
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
class ReposApi: IReposApi {
    companion object {
        private const val TAG = "ReposApi"
    }

    private interface ReposApiRetrofit {
        @GET("/$USERS/{$USERNAME}/repos")
        suspend fun getRepos(@Path(USERNAME) username: String): Response<List<RepoModel>>
    }

    private val reposApi: ReposApiRetrofit by lazy {
        ApiProvider.retrofit.create(ReposApiRetrofit::class.java)
    }

    override suspend fun getRepos(username: String): ResponseResult<List<RepoModel>> =
        ApiProvider.requestUnsafe(TAG, { reposApi.getRepos(username) })

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideAuthApi(): IReposApi = ReposApi()
}