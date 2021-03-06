package com.example.github.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.data.local.repo.RepoDao
import com.example.github.data.local.user.UserDao
import com.example.github.model.RepoModel
import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRef

@Database(entities = [RepoModel::class, UserModel::class, UserWithFollowersRef::class], version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract fun repoDao(): RepoDao
    abstract fun userDao(): UserDao
}