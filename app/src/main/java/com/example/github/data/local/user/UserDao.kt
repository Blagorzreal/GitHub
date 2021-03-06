package com.example.github.data.local.user

import androidx.room.*
import com.example.github.model.UserModel
import com.example.github.model.relation.UserWithFollowersRef
import com.example.github.model.relation.UserWithFollowersRelation
import com.example.github.util.Constants.Companion.NOT_INSERTED_SINCE_EXISTS
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao: IUserDao {
    @Query("SELECT * FROM UserModel WHERE login LIKE :usernameCriteria ORDER BY login ASC LIMIT :limit OFFSET :offset")
    override suspend fun searchByUsername(usernameCriteria: String, offset: Int, limit: Int): List<UserModel>

    @Query("SELECT COUNT(id) FROM UserModel WHERE login LIKE :usernameCriteria")
    override suspend fun getColumnCountByUsername(usernameCriteria: String): Int

    @Query("SELECT * FROM UserModel WHERE id = :id")
    override suspend fun getById(id: Long): UserModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertUser(user: UserModel): Long

    @Transaction
    override suspend fun insertUsers(users: List<UserModel>, updateAdditionalData: Boolean) {
        users.forEach {
            if (insertUser(it) == NOT_INSERTED_SINCE_EXISTS) {
                if (updateAdditionalData) {
                    if ((it.followers != null) && (it.following != null))
                        updateUserFollowersFollowing(it.id, it.followers, it.following)
                } else
                    updateUsernameAvatar(it.id, it.login, it.avatarUrl)
            }
        }
    }

    @Query("UPDATE UserModel SET followers=:followers, following=:following WHERE id = :id")
    override suspend fun updateUserFollowersFollowing(id: Long, followers: Long, following: Long)

    @Query("UPDATE UserModel SET login=:name, avatarUrl=:avatarUrl WHERE id = :id")
    override suspend fun updateUsernameAvatar(id: Long, name: String, avatarUrl: String?)

    @Query("DELETE FROM UserModel")
    override suspend fun deleteAll()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertFollowers(userWithFollowersRef: UserWithFollowersRef)

    @Transaction
    @Query("DELETE FROM UserWithFollowersRef WHERE id1 = :id")
    override suspend fun deleteFollowers(id: Long)

    @Transaction
    override suspend fun insertUsersAsFollowers(id: Long, followers: List<UserModel>) {
        deleteFollowers(id)
        insertUsers(followers)
        followers.forEach { insertFollowers(UserWithFollowersRef(id, it.id)) }
    }

    @Transaction
    @Query("SELECT * FROM UserModel WHERE id = :id")
    override fun getFollowers(id: Long): Flow<UserWithFollowersRelation>
}