package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class DetailFavResponse(

	@field:SerializedName("data")
	val data: List<DataFavItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataFavItem(

	@field:SerializedName("tourism_description")
	val tourismDescription: String,

	@field:SerializedName("tourism_image")
	val tourismImage: String,

	@field:SerializedName("tour_guide_name")
	val tourGuideName: String,

	@field:SerializedName("tourism_name")
	val tourismName: String,

	@field:SerializedName("ride_name")
	val rideName: String,

	@field:SerializedName("hotel_name")
	val hotelName: String
)
