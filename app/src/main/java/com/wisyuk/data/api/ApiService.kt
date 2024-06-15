package com.wisyuk.data.api

import com.wisyuk.data.response.AddFavResponse
import com.wisyuk.data.response.AddPaidPlanResponse
import com.wisyuk.data.response.AddPreferencesResponse
import com.wisyuk.data.response.DetailPlanResponse
import com.wisyuk.data.response.DetailTourismResponse
import com.wisyuk.data.response.LoginResponse
import com.wisyuk.data.response.PaymentMethodResponse
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
import retrofit2.http.Query

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

    @FormUrlEncoded
    @POST("add-paid-plan")
    suspend fun addPaidPlan(
        @Field("userID") userID: Int,
        @Field("tourismID") tourismID: Int,
        @Field("hotelID") hotelID: Int,
        @Field("rideID") rideID: Int,
        @Field("tourGuideID") tourGuideID: Int,
        @Field("go_date") goDate: String,
        @Field("status") status: Int,
        @Field("paymentMethodID") paymentMethodID: Int,
    ): AddPaidPlanResponse

    @FormUrlEncoded
    @POST("add-favourite-plan")
    suspend fun addFavouritePlan(
        @Field("userID") userID: Int,
        @Field("tourismID") tourismID: Int,
        @Field("hotelID") hotelID: Int,
        @Field("rideID") rideID: Int,
        @Field("tourGuideID") tourGuideID: Int,
        @Field("go_date") goDate: String
    ) : AddFavResponse

    @GET("/view-paid-plan/{userID}")
    suspend fun getPaidPlans(
        @Path("userID") userID: Int,
    ) : TourismResponse

    @GET("view-favourite-plan/{userID}")
    suspend fun getFavorite(
        @Path("userID") userID: Int,
    ) : TourismResponse

    @GET("view-detail-paid-plan/{userID}/{tourismID}/{goAt}")
    suspend fun getDetailPaidPlan(
        @Path("userID") userID: Int,
        @Path("tourismID") tourismID: Int,
        @Path("goAt") goAt: String
    ) : DetailPlanResponse

    @GET("view-detail-favourite-plan/{userID}/{tourismID}/{goAt}")
    suspend fun getDetailFavoritePlan(
        @Path("userID") userID: Int,
        @Path("tourismID") tourismID: Int,
        @Path("goAt") goAt: String
    ) : DetailPlanResponse

    @GET("preferences")
    suspend fun getPreferences(): PreferencesResponse

    @FormUrlEncoded
    @POST("user-preferences")
    suspend fun postPreferences(
        @Field("userID") userID: Int,
        @Field("preferences") preferences: List<Int>
    ) : AddPreferencesResponse

    @GET("tourisms")
    suspend fun getTourisms() : TourismResponse

    @GET("search-tourism")
    suspend fun getTourisms(
        @Query("tourismName") tourismName: String
    ) : TourismResponse

    @GET("tourism-detail/{tourismID}")
    suspend fun getDetailTourism(
        @Path("tourismID") tourismID: Int
    ) : DetailTourismResponse

    @GET("payment-methods")
    suspend fun getPaymentMethod(): PaymentMethodResponse
}