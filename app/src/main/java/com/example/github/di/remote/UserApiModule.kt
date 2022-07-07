package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.user.IUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UserApiModule {
    @Provides
    fun provideUserApi(userApiMap: Map<String, @JvmSuppressWildcards IUserApi>) =
        userApiMap[BuildConfig.FLAVOR] ?: throw Exception("Unable to get user api")
}