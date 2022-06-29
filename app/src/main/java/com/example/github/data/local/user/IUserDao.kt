package com.example.github.data.local.user

import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRelation
import com.example.github.model.relation.UserWithFollowersRef
import kotlinx.coroutines.flow.Flow

interface IUserDao {
    fun searchByUsername(usernameCriteria: String, limit: Int): List<UserModel>
    fun getById(id: Long): UserModel?
    suspend fun insertUser(user: UserModel)
    suspend fun insertUsers(users: List<UserModel>)
    suspend fun deleteAll()
    suspend fun insertUsersAndSearchByUsername(users: List<UserModel>, usernameCriteria: String, limit: Int): List<UserModel>
    suspend fun insertFollowers(userWithFollowersRef: UserWithFollowersRef)
    suspend fun insertUsersAsFollowers(id: Long, followers: List<UserModel>)
    fun getFollowers(ownerId: Long): Flow<UserWithFollowersRelation>
}