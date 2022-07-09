package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.auth.IAuthApi
import com.example.github.util.CommonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object AuthApiModule {
    @Provides
    fun provideAuthApi(authApiMap: Map<String, @JvmSuppressWildcards IAuthApi>) =
        authApiMap[BuildConfig.FLAVOR] ?: CommonHelper.throwApiModuleException("auth")
}