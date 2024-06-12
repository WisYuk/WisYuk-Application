package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class AddFavResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
