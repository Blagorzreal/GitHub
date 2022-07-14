package com.example.github.data.local.user

import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRelation
import com.example.github.model.relation.UserWithFollowersRef
import kotlinx.coroutines.flow.Flow

interface IUserDao {
    suspend fun searchByUsername(usernameCriteria: String, offset: Int, limit: Int): List<UserModel>
    suspend fun getColumnCountByUsername(usernameCriteria: String): Int
    suspend fun getById(id: Long): UserModel?
    suspend fun insertUser(user: UserModel): Long
    suspend fun insertUsers(users: List<UserModel>, updateAdditionalData: Boolean = false)
    suspend fun updateUsernameAvatar(id: Long, name: String, avatarUrl: String?)
    suspend fun updateUserFollowersFollowing(id: Long, followers: Long, following: Long)
    suspend fun deleteAll()
    suspend fun insertFollowers(userWithFollowersRef: UserWithFollowersRef)
    suspend fun insertUsersAsFollowers(id: Long, followers: List<UserModel>)
    fun getFollowers(id: Long): Flow<UserWithFollowersRelation>
}