package com.example.github.data.local.profile

import androidx.room.*
import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao: IProfileDao {
    @Query("SELECT * FROM RepoModel")
    override fun getAll(): Flow<List<RepoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertRepos(repos: List<RepoModel>)

    @Query("DELETE FROM RepoModel")
    override suspend fun deleteAll()

    @Transaction
    override suspend fun deleteAllAndInsertNew(repos: List<RepoModel>) {
        deleteAll()
        insertRepos(repos)
    }
}