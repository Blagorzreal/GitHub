package com.example.github.data.local.user

import com.example.github.BuildConfig
import com.example.github.data.MockedData.Companion.users
import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRef
import com.example.github.model.relation.UserWithFollowersRelation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockedUserDao: IUserDao {
    private val followers = mutableListOf(
        UserModel(7, "test7", 12, 7, null),
        UserModel(8, "test8", 5, 4, null),
        UserModel(9, "test9", 7, 3, null),
        UserModel(10, "test10", 9, 0, null),
        UserModel(11, "test11", 3, 9, null),
        UserModel(12, "test12", 4, 11, null)
    )

    override suspend fun searchByUsername(usernameCriteria: String, offset: Int, limit: Int): List<UserModel> =
        emptyList()

    override suspend fun getColumnCountByUsername(usernameCriteria: String): Int = -1

    override suspend fun getById(id: Long): UserModel? = users.firstOrNull { it.id == id }

    override suspend fun insertUser(user: UserModel): Long = 0

    override suspend fun insertUsers(users: List<UserModel>, updateAdditionalData: Boolean) { }

    override suspend fun updateUsernameAvatar(id: Long, name: String, avatarUrl: String?) { }

    override suspend fun updateUserFollowersFollowing(id: Long, followers: Long, following: Long) { }

    override suspend fun deleteAll() {
        users.clear()
    }

    override suspend fun insertFollowers(userWithFollowersRef: UserWithFollowersRef) {
    }

    override suspend fun insertUsersAsFollowers(id: Long, followers: List<UserModel>) {
    }

    override suspend fun deleteFollowers(id: Long) {
    }

    override fun getFollowers(id: Long): Flow<UserWithFollowersRelation> =
        flow { emit(UserWithFollowersRelation(users[0], followers)) }

    @Provides
    @Singleton
    @IntoMap
    @StringKey(BuildConfig.FLAVOR_MOCKED)
    fun provideUserDao(): IUserDao = MockedUserDao()
}