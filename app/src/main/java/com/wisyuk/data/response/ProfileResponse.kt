package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("preferences")
	val preferences: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
