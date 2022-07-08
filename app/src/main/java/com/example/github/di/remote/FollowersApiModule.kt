package com.example.github.di.remote

import com.example.github.BuildConfig
import com.example.github.data.remote.followers.IFollowersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object FollowersApiModule {
    @Provides
    fun provideFollowersApi(followersApiMap: Map<String, @JvmSuppressWildcards IFollowersApi>) =
        followersApiMap[BuildConfig.FLAVOR] ?: throw Exception("Unable to get followers api")
}