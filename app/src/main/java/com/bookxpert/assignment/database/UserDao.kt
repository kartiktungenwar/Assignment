package com.bookxpert.assignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): MutableList<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser (user: User)

    @Delete
    suspend fun deleteUser (user: User)

}