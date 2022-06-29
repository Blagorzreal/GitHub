package com.example.github.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.model.RepoModel
import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRef

@Database(entities = [RepoModel::class, UserModel::class, UserWithFollowersRef::class], version = 1)
abstract class UserDB: RoomDatabase() {
    abstract fun userDao(): UserDao
}