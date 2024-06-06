package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("preferences")
	val preferences: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)
