package com.wisyuk.data.repository

import com.wisyuk.data.api.ApiService
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.pref.UserPreference
import com.wisyuk.data.response.SignUpResponse
import com.wisyuk.utils.Utils.toInt
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun signUp(name: String, email: String, password: String, promotion: Boolean = false): SignUpResponse {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordRequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        return apiService.signup(nameRequestBody, emailRequestBody, passwordRequestBody, promotion.toInt())
    }

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