package com.example.github.di

import android.content.Context
import androidx.room.Room
import com.example.github.BuildConfig
import com.example.github.data.local.user.IUserDao
import com.example.github.data.local.user.UserDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_TABLE_NAME = "user"

@InstallIn(SingletonComponent::class)
@Module
object UserDatabaseModule {
    @Singleton
    @Provides
    fun provideUserDao(userDaoMap: Map<String, @JvmSuppressWildcards IUserDao>) =
        userDaoMap[BuildConfig.FLAVOR] ?: throw Exception("Unable to get user dao")

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): UserDataBase {
        return Room.databaseBuilder(context, UserDataBase::class.java, USER_TABLE_NAME).build()
    }
}