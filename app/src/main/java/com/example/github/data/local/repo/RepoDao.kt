package com.example.github.data.local.repo

import androidx.room.*
import com.example.github.model.RepoModel
import com.example.github.util.Constants.Companion.NOT_INSERTED_SINCE_EXISTS
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao: IRepoDao {
    @Query("SELECT * FROM RepoModel WHERE owner_id = :ownerId")
    override fun getAll(ownerId: Long): Flow<List<RepoModel>>

    @Query("SELECT * FROM RepoModel WHERE starred = 1")
    override fun getAllStarred(): Flow<List<RepoModel>>

    @Transaction
    override suspend fun insertRepos(repos: List<RepoModel>) {
        repos.forEach {
            if (insertRepo(it) == NOT_INSERTED_SINCE_EXISTS)
                updateName(it.id, it.name)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertRepo(repo: RepoModel): Long

    @Query("UPDATE RepoModel SET name=:name WHERE id = :id")
    override suspend fun updateName(id: Long, name: String)

    @Query("DELETE FROM RepoModel")
    override suspend fun deleteAll()

    @Query("DELETE FROM RepoModel WHERE (owner_id = :ownerId) AND id NOT IN (:ids)")
    override suspend fun deleteAllByOwner(ownerId: Long, ids: List<Long>)

    @Transaction
    override suspend fun deleteAllByOwnerAndInsert(repos: List<RepoModel>, ownerId: Long) {
        deleteAllByOwner(ownerId, repos.map { it.id })
        insertRepos(repos)
    }

    @Query("UPDATE RepoModel SET starred = :starred WHERE id = :id")
    override suspend fun updateStarred(id: Long, starred: Int)
}