package com.example.github.data.local.user

import androidx.room.*
import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRef
import com.example.github.model.relation.UserWithFollowersRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao: IUserDao {
    @Query("SELECT * FROM UserModel WHERE login LIKE :usernameCriteria ORDER BY login ASC LIMIT :limit")
    override suspend fun searchByUsername(usernameCriteria: String, limit: Int): List<UserModel>

    @Query("SELECT COUNT(owner_id) FROM UserModel WHERE login LIKE :usernameCriteria")
    override suspend fun searchColumnCountByUsername(usernameCriteria: String): Int

    @Query("SELECT * FROM UserModel WHERE owner_id = :id")
    override suspend fun getById(id: Long): UserModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertUser(user: UserModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertUsers(users: List<UserModel>)

    @Query("DELETE FROM UserModel")
    override suspend fun deleteAll()

    @Transaction
    override suspend fun insertUsersAndSearchByUsername(
        users: List<UserModel>,
        usernameCriteria: String,
        limit: Int
    ): List<UserModel> {
        insertUsers(users)
        return searchByUsername(usernameCriteria, limit)
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertFollowers(userWithFollowersRef: UserWithFollowersRef)

    @Transaction
    override suspend fun insertUsersAsFollowers(id: Long, followers: List<UserModel>) {
        insertUsers(followers)
        followers.forEach { insertFollowers(UserWithFollowersRef(id, it.id)) }
    }

    @Transaction
    @Query("SELECT * FROM UserModel WHERE owner_id = :ownerId")
    override fun getFollowers(ownerId: Long): Flow<UserWithFollowersRelation>
}