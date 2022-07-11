package com.example.github.data.local.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.model.RepoModel

@Database(entities = [RepoModel::class], version = 1)
abstract class ReposDatabase: RoomDatabase() {
    abstract fun reposDao(): ReposDao
}