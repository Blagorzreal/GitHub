package com.example.github.di

import com.example.github.BuildConfig
import com.example.github.data.remote.auth.IAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object AuthApiModule {
    @Provides
    fun provideAuthApi(authApiMap: Map<String, @JvmSuppressWildcards IAuthApi>) =
        authApiMap[BuildConfig.FLAVOR] ?: throw Exception("Unable to get auth api")
}