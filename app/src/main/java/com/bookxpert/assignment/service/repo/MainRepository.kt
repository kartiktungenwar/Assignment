package com.bookxpert.assignment.service.repo


import com.bookxpert.assignment.database.User
import com.bookxpert.assignment.database.UserDao
import com.bookxpert.assignment.network.ApiService
import com.bookxpert.assignment.preferencesDataStore.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val dataStore: PreferencesDataStore, private val retrofitApi: ApiService,private val userDao: UserDao) {

    suspend fun saveString(key: String, value: String) {
        dataStore.saveString(key, value)
    }

    fun getString(key: String): Flow<String?> {
        return dataStore.getString(key)
    }

    suspend fun saveInt(key: String, value: Int) {
        dataStore.saveInt(key, value)
    }

    fun getInt(key: String): Flow<Int?> {
        return dataStore.getInt(key)
    }

    suspend fun saveInt(key: String, value: Float) {
        dataStore.saveFloat(key, value)
    }

    fun getFloat(key: String): Flow<Float?> {
        return dataStore.getFloat(key)
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.saveBoolean(key, value)
    }

    fun getBoolean(key: String): Flow<Boolean?> {
        return dataStore.getBoolean(key)
    }

    /**
     * This method used for getting data from server
     */
    suspend fun getData() = retrofitApi.getLoanBalances()

    // Function to get all users
    suspend fun getAllUsers(): MutableList<User> {
        return userDao.getAllUsers() // Call the DAO method
    }

    // Function to insert User
    suspend fun saveData(user: User) {
            userDao.insertUser(user)
    }

    // Function to insert User
    suspend fun deleteData(user: User) : Boolean {
        val rowsDeleted = userDao.deleteUser (user)
        return rowsDeleted > 0 // Return true if at least one row was deleted
    }
}