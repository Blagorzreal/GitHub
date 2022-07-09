package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.user.IUserApi
import com.example.github.util.CommonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UserApiModule {
    @Provides
    fun provideUserApi(userApiMap: Map<String, @JvmSuppressWildcards IUserApi>) =
        userApiMap[BuildConfig.FLAVOR] ?: CommonHelper.throwApiModuleException("user")
}