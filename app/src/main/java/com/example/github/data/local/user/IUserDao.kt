package com.example.github.data.local.user

import com.example.github.model.UserModel

interface IUserDao {
    fun searchByUsername(usernameCriteria: String, limit: Int): List<UserModel>
    fun getById(id: Long): UserModel?
    suspend fun insertUser(user: UserModel)
    suspend fun insertUsers(users: List<UserModel>)
    suspend fun deleteAll()
    suspend fun insertUsersAndSearchByUsername(users: List<UserModel>, usernameCriteria: String, limit: Int): List<UserModel>
}