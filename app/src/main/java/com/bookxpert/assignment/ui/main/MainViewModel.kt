package com.bookxpert.assignment.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bookxpert.assignment.database.User
import com.bookxpert.assignment.network.Resource
import com.bookxpert.assignment.service.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){


    /**
     * This method used for getting data from server
     */
    fun getData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = mainRepository.getData()
            Log.e(TAG, response.toString())
            // Check if the response is successful and has a body
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                emit(Resource.success(data))
                Log.e(TAG, response.toString())
            } else {
                emit(Resource.error(data = null, message = "Unknown error"))
            }

        } catch (e: Exception) {
            Log.e("Error", e.message ?: "Unknown error")
            emit(Resource.error(data = null, message = e.message ?: "An error occurred"))
        }

    }

    /**
     * This method used for insert User data from DB
     */
    fun saveData(user: User) {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            mainRepository.saveData(user)
        }
    }

    /**
     * This method used for delete User data from DB
     */
    fun deleteData(user: User) {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            mainRepository.deleteData(user)
        }
    }

    // Function to get all users
    fun getAllUsers(onResult: (MutableList<User>) -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val users = mainRepository.getAllUsers() // Call the repository method
                onResult(users) // Pass the result back to the caller
            } catch (e: Exception) {
                onError(e) // Handle the error
            }
        }
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        mainRepository.saveBoolean(key, value)
    }

    suspend fun getAllUsers(): MutableList<User> {
        return mainRepository.getAllUsers() // Call the DAO method
    }


    companion object {
        private const val TAG = "MainViewModel"
    }
}