package com.example.github.data.local.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.model.RepoModel
import com.example.github.model.UserModel

@Database(entities = [RepoModel::class, UserModel::class], version = 1)
abstract class ReposDB: RoomDatabase() {
    abstract fun reposDao(): ReposDao
}