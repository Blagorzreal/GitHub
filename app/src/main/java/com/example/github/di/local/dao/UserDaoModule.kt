package com.example.github.di.local.dao

import com.example.github.BuildConfig
import com.example.github.data.local.MainDatabase
import com.example.github.data.local.user.IUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDaoModule {
    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_PRODUCTION)
    fun provideUserDao(mainDatabase: MainDatabase): IUserDao = mainDatabase.userDao()
}