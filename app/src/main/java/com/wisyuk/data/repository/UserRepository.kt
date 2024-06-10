package com.wisyuk.data.repository

import com.wisyuk.data.api.ApiService
import com.wisyuk.data.pref.UserModel
import com.wisyuk.data.pref.UserPreference
import com.wisyuk.data.response.AddPaidPlanResponse
import com.wisyuk.data.response.SignUpResponse
import com.wisyuk.data.response.UpdateProfileResponse
import com.wisyuk.utils.Utils.toInt
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        promotion: Boolean = false
    ): SignUpResponse {
        return apiService.signup(
            name,
            email,
            password,
            promotion.toInt()
        )
    }

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun getProfile(userId: Int) = apiService.getProfile(userId)

    suspend fun updateProfile(
        userId: Int,
        image: File? = null,
        name: String,
        preferences: List<Int>
    ): UpdateProfileResponse {
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        if (image != null) {
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImageFile
            )
            return apiService.updateProfile(
                userId,
                nameRequestBody,
                null,
                preferences,
                multipartBody
            )
        }

        return apiService.updateProfile(userId, nameRequestBody, null, preferences, null)
    }

    suspend fun addPaidPlan(
        userID: String,
        tourismID: String,
        hotelID: String,
        rideID: String,
        tourGuideID: String,
        go_date: String,
        status: String,
        paymentMethodID: String
    ): AddPaidPlanResponse {
        val userIDRequestBody = userID.toRequestBody("text/plain".toMediaTypeOrNull())
        val tourismIDRequestBody = tourismID.toRequestBody("text/plain".toMediaTypeOrNull())
        val hotelIDRequestBody = hotelID.toRequestBody("text/plain".toMediaTypeOrNull())
        val rideIDRequestBody = rideID.toRequestBody("text/plain".toMediaTypeOrNull())
        val tourGuideIDRequestBody = tourGuideID.toRequestBody("text/plain".toMediaTypeOrNull())
        val goDateRequestBody = go_date.toRequestBody("text/plain".toMediaTypeOrNull())
        val statusRequestBody = status.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val paymentMethodIDRequestBody =
            paymentMethodID.toRequestBody("text/plain".toMediaTypeOrNull())

        return apiService.addPaidPlan(
            userIDRequestBody,
            tourismIDRequestBody,
            hotelIDRequestBody,
            rideIDRequestBody,
            tourGuideIDRequestBody,
            goDateRequestBody,
            statusRequestBody,
            paymentMethodIDRequestBody
        )
    }

    suspend fun getFavorite(userID: Int) = apiService.getFavorite(userID)

    suspend fun getPreferences() = apiService.getPreferences()

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