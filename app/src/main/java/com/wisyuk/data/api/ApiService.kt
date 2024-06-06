package com.wisyuk.data.api

import com.wisyuk.data.response.LoginResponse
import com.wisyuk.data.response.ProfileResponse
import com.wisyuk.data.response.SignUpResponse
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("/signup")
    suspend fun signup(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("promotion") promotion: Int,
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("profile/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
    ) : ProfileResponse
}