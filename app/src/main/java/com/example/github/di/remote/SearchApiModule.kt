package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.search.ISearchApi
import com.example.github.util.CommonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object SearchApiModule {
    @Provides
    fun provideSearchApi(searchApiMap: Map<String, @JvmSuppressWildcards ISearchApi>) =
        searchApiMap[BuildConfig.FLAVOR] ?: throw CommonHelper.throwApiModuleException("search")
}