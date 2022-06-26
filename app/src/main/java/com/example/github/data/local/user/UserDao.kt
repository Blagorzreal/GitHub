package com.example.github.data.local.user

import androidx.room.*
import com.example.github.model.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao: IUserDao {
    @Query("SELECT * FROM UserModel WHERE owner_id = :id")
    override fun getById(id: Long): Flow<UserModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertUser(user: UserModel)

    @Query("DELETE FROM UserModel")
    override suspend fun deleteAll()
}