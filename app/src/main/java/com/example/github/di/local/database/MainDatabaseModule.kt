package com.example.github.di.local.database

import android.content.Context
import androidx.room.Room
import com.example.github.BuildConfig
import com.example.github.data.local.MainDatabase
import com.example.github.data.local.repo.IRepoDao
import com.example.github.data.local.user.IUserDao
import com.example.github.util.CommonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val MAIN_TABLE_NAME = "main"

@InstallIn(SingletonComponent::class)
@Module
object RepoDatabaseModule {
    @Singleton
    @Provides
    fun provideRepoDao(repoDaoMap: Map<String, @JvmSuppressWildcards IRepoDao>) =
        repoDaoMap[BuildConfig.FLAVOR] ?: throw CommonHelper.throwDatabaseModuleException("repo")

    @Singleton
    @Provides
    fun provideUserDao(userDaoMap: Map<String, @JvmSuppressWildcards IUserDao>) =
        userDaoMap[BuildConfig.FLAVOR] ?: throw CommonHelper.throwDatabaseModuleException("user")

    @Singleton
    @Provides
    fun provideMainDatabase(@ApplicationContext context: Context): MainDatabase {
        return Room.databaseBuilder(context, MainDatabase::class.java, MAIN_TABLE_NAME).build()
    }
}