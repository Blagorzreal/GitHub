package com.example.github.data.local.profile

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.model.RepoModel
import com.example.github.model.UserModel

@Database(entities = [RepoModel::class, UserModel::class], version = 1)
abstract class ProfileDB: RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}