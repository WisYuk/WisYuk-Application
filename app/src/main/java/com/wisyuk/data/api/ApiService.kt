package com.wisyuk.data.api

import androidx.datastore.preferences.core.Preferences
import com.wisyuk.data.response.AddPaidPlanResponse
import com.wisyuk.data.response.LoginResponse
import com.wisyuk.data.response.PreferencesResponse
import com.wisyuk.data.response.ProfileResponse
import com.wisyuk.data.response.SignUpResponse
import com.wisyuk.data.response.TourismResponse
import com.wisyuk.data.response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("promotion") promotion: Int,
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("profile/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Int,
    ) : ProfileResponse

    @Multipart
    @PUT("profile/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Int,
        @Part("name") name: RequestBody? = null,
        @Part("password") password: RequestBody? = null,
        @Part("preferences") preferences: List<Int>? = null,
        @Part image: MultipartBody.Part? = null
    ) : UpdateProfileResponse

    @Multipart
    @POST("add-paid-plan")
    suspend fun addPaidPlan(
        //userID, tourismID, hotelID, rideID, tourGuideID, go_date, status, paymentMethodID
        @Part("userID") userID: RequestBody,
        @Part("tourismID") tourismID: RequestBody,
        @Part("hotelID") hotelID: RequestBody,
        @Part("rideID") rideID: RequestBody,
        @Part("tourGuideID") tourGuideID: RequestBody,
        @Part("go_date") goDate: RequestBody,
        @Part("status") status: RequestBody,
        @Part("paymentMethodID") paymentMethodID: RequestBody,
    ): AddPaidPlanResponse

    @GET("view-favourite-plan/{userID}")
    suspend fun getFavorite(
        @Path("userId") userId: Int,
    ) : TourismResponse

    @GET("preferences")
    suspend fun getPreferences(): PreferencesResponse

}