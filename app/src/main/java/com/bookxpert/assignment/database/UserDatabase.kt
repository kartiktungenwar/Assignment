package com.bookxpert.assignment.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User ::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}