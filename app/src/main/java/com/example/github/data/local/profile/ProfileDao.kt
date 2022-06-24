package com.example.github.data.local.profile

import androidx.room.*
import com.example.github.model.RepoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao: IProfileDao {
    @Query("SELECT * FROM RepoModel WHERE owner_id = :ownerId")
    override fun getAll(ownerId: Long): Flow<List<RepoModel>>

    @Query("SELECT * FROM RepoModel WHERE starred = 1")
    override fun getAllStarred(): Flow<List<RepoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertRepos(repos: List<RepoModel>)

    @Query("DELETE FROM RepoModel")
    override suspend fun deleteAll()

    @Query("DELETE FROM RepoModel WHERE owner_id = :ownerId")
    override suspend fun deleteAllByOwner(ownerId: Long)

    @Transaction
    override suspend fun deleteAllAndInsertNew(repos: List<RepoModel>, ownerId: Long) {
        deleteAllByOwner(ownerId)
        insertRepos(repos)
    }
}