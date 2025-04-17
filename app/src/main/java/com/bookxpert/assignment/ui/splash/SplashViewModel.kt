package com.bookxpert.assignment.ui.splash

import androidx.lifecycle.ViewModel
import com.bookxpert.assignment.service.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getBoolean(userKey: String): Flow<Boolean?> {
        return mainRepository.getBoolean(userKey)
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        mainRepository.saveBoolean(key, value)
    }
}