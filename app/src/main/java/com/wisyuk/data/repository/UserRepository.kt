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
){

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
        image: File?,
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
        userID: Int,
        tourismID: Int,
        hotelID: Int,
        rideID: Int,
        tourGuideID: Int,
        go_date: String,
        status: Int,
        paymentMethodID: Int
    ): AddPaidPlanResponse {
        return apiService.addPaidPlan(
            userID,
            tourismID,
            hotelID,
            rideID,
            tourGuideID,
            go_date,
            status,
            paymentMethodID
        )
    }

    suspend fun addFavouritePlan(
        userID: Int,
        tourismID: Int,
        hotelID: Int,
        rideID: Int,
        tourGuideID: Int,
        goDate: String
    ) = apiService.addFavouritePlan(userID, tourismID, hotelID, rideID, tourGuideID, goDate)

    suspend fun getPaidPlans(userID: Int) = apiService.getPaidPlans(userID)

    suspend fun getFavorite(userID: Int) = apiService.getFavorite(userID)

    suspend fun getDetailPaidPlan(userID: Int, tourismID: Int, goAt: String)
        = apiService.getDetailPaidPlan(userID, tourismID, goAt)

    suspend fun getDetaiLFavoritePlan(userID: Int, tourismID: Int, goAt: String)
        = apiService.getDetailFavoritePlan(userID, tourismID, goAt)
    suspend fun getPreferences() = apiService.getPreferences()

    suspend fun postPreferences(userID: Int, preferences: List<Int>) = apiService.postPreferences(userID, preferences)

    suspend fun getTourisms() = apiService.getTourisms()

    suspend fun getTourisms(goAt: String, userID: Int) = apiService.viewRecommendation(goAt, userID)

    suspend fun getTourisms(query: String) = apiService.getTourisms(query)

    suspend fun getDetailTourism(tourismID: Int) = apiService.getDetailTourism(tourismID)

    suspend fun getPaymentMethod() = apiService.getPaymentMethod()

    suspend fun deleteFavouritePlan(userID: Int, tourismID: Int, goAt: String) = apiService.deleteFavouritePlan(userID, tourismID, goAt)
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