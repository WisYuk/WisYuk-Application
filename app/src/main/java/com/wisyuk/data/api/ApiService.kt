package com.wisyuk.data.api

import com.wisyuk.data.response.LoginResponse
import com.wisyuk.data.response.ProfileResponse
import com.wisyuk.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("promotion") promotion: Boolean = false,
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