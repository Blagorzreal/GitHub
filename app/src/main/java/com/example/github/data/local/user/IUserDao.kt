package com.example.github.data.local.user

import com.example.github.model.UserModel
import kotlinx.coroutines.flow.Flow

interface IUserDao {
    fun getById(id: Long): Flow<UserModel?>
    suspend fun insertUser(user: UserModel)
    suspend fun deleteAll()
}