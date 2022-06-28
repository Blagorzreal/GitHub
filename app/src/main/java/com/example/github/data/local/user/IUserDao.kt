package com.example.github.data.local.user

import com.example.github.model.UserModel
import kotlinx.coroutines.flow.Flow

interface IUserDao {
    fun searchByUsername(query: String): List<UserModel>
    fun getById(id: Long): Flow<UserModel?>
    suspend fun insertUser(user: UserModel)
    suspend fun insertUsers(users: List<UserModel>)
    suspend fun deleteAll()
    suspend fun insertUsersAndSearchByUsername(users: List<UserModel>, query: String): List<UserModel>
}