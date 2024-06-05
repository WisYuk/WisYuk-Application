package com.wisyuk.data.repository

import com.wisyuk.data.api.ApiService
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun signUp(name: String, email: String, password: String, promotion: Boolean = false)
        = apiService.signup(name, email, password, promotion)

    suspend fun login(email: String, password: String) = apiService.login(email, password)
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}