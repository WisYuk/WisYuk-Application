package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class FavTourismResponse(

	@field:SerializedName("data")
	val data: List<FavTourismItem>,

	@field:SerializedName("status")
	val status: String
)

data class FavTourismItem(

	@field:SerializedName("go_at")
	val goAt: String,

	@field:SerializedName("name")
	val name: String
)
