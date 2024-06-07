package com.wisyuk.data.repository

import com.wisyuk.data.api.ApiService
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.pref.UserPreference
import com.wisyuk.data.response.SignUpResponse
import com.wisyuk.data.response.UpdateProfileResponse
import com.wisyuk.utils.Utils.toInt
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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

    suspend fun getProfile(userId: Int) = apiService.getProfile(userId)

    suspend fun updateProfile(userId: Int, image: File? = null, name: String, preferences: List<String> ): UpdateProfileResponse {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestImageFile = image?.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            image?.name,
            (requestImageFile ?: " ") as RequestBody
        )

        return apiService.updateProfile(userId, nameRequestBody, null, preferences, multipartBody)
    }

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