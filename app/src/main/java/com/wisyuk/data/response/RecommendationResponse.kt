package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: DataTour,

	@field:SerializedName("status")
	val status: String
)

data class DataTour(

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem>
)

data class Details(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("longitude")
	val longitude: String
)

data class RecommendationsItem(

	@field:SerializedName("details")
	val details: Details,

	@field:SerializedName("id")
	val id: Int
)
