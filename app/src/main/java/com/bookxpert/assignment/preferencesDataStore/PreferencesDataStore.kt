package com.bookxpert.assignment.preferencesDataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataStore@Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStorePreference {

    override suspend fun saveInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        val preferencesKey = floatPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun saveString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override fun getInt(key: String): Flow<Int?> {
        val preferencesKey = intPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey]
        }
    }

    override fun getFloat(key: String): Flow<Float?> {
        val preferencesKey = floatPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey]
        }
    }

    override fun getBoolean(key: String): Flow<Boolean?> {
        val preferencesKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey]
        }
    }

    override fun getString(key: String): Flow<String?> {
        val preferencesKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferencesKey]
        }
    }

    override suspend fun remove(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }
}