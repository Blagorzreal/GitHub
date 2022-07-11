package com.example.github.di.local.database

import android.content.Context
import androidx.room.Room
import com.example.github.BuildConfig
import com.example.github.data.local.MainDatabase
import com.example.github.data.local.repos.IReposDao
import com.example.github.data.local.user.IUserDao
import com.example.github.util.CommonHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val REPOS_TABLE_NAME = "repos"

@InstallIn(SingletonComponent::class)
@Module
object ReposDatabaseModule {
    @Singleton
    @Provides
    fun provideReposDao(reposDaoMap: Map<String, @JvmSuppressWildcards IReposDao>) =
        reposDaoMap[BuildConfig.FLAVOR] ?: throw CommonHelper.throwDatabaseModuleException("repos")

    @Singleton
    @Provides
    fun provideUserDao(userDaoMap: Map<String, @JvmSuppressWildcards IUserDao>) =
        userDaoMap[BuildConfig.FLAVOR] ?: throw CommonHelper.throwDatabaseModuleException("user")

    @Singleton
    @Provides
    fun provideMainDatabase(@ApplicationContext context: Context): MainDatabase {
        return Room.databaseBuilder(context, MainDatabase::class.java, REPOS_TABLE_NAME).build()
    }
}