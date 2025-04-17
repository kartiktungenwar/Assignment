package com.bookxpert.assignment.preferencesDataStore

import kotlinx.coroutines.flow.Flow

interface DataStorePreference {
    suspend fun saveInt(key: String, value: Int)
    suspend fun saveFloat(key: String, value: Float)
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun saveString(key: String, value: String)

    fun getInt(key: String): Flow<Int?>
    fun getFloat(key: String): Flow<Float?>
    fun getBoolean(key: String): Flow<Boolean?>
    fun getString(key: String): Flow<String?>

    suspend fun remove(key: String)
}