package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.repos.IReposApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object ReposApiModule {
    @Provides
    fun provideReposApi(reposApiMap: Map<String, @JvmSuppressWildcards IReposApi>) =
        reposApiMap[BuildConfig.FLAVOR] ?: throw Exception("Unable to get repos api")
}