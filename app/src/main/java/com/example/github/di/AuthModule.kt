package com.example.github.di

import com.example.github.data.remote.auth.AuthApi
import com.example.github.data.remote.auth.IAuthApi
import com.example.github.data.remote.auth.MockedAuthApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Qualifier
annotation class AuthApiQualifier

@Qualifier
annotation class MockedAuthApiQualifier

@InstallIn(ViewModelComponent::class)
@Module
abstract class AuthApiModule {
    @AuthApiQualifier
    @ViewModelScoped
    @Binds
    abstract fun bindAuthApi(api: AuthApi): IAuthApi
}

@InstallIn(ViewModelComponent::class)
@Module
abstract class MockedAuthApiModule {
    @MockedAuthApiQualifier
    @ViewModelScoped
    @Binds
    abstract fun bindMockedAuthApi(api: MockedAuthApi): IAuthApi
}