package com.example.github.data.remote.followers

import com.example.github.BuildConfig
import com.example.github.data.remote.ResponseResult
import com.example.github.model.UserModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton
import kotlin.random.Random

@InstallIn(SingletonComponent::class)
@Module
class MockedFollowersAPi: IFollowersApi {
    override suspend fun getFollowers(username: String): ResponseResult<List<UserModel>> {
        return ResponseResult.Success(
            listOf(UserModel(Random.nextLong(), "new1", 12, 5, null),
                UserModel(Random.nextLong(), "new2", 9, 5, null),
                UserModel(Random.nextLong(), "new3", 8, 3, null),
                UserModel(Random.nextLong(), "new4", 2, 5, null),
                UserModel(Random.nextLong(), "new5", 12, 1, null)))
    }

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideFollowersApi(): IFollowersApi = MockedFollowersAPi()
}