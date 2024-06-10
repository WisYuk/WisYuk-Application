package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class TourismResponse(

	@field:SerializedName("data")
	val data: List<ListTourismItem>,

	@field:SerializedName("status")
	val status: String
)

data class ListTourismItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("longitude")
	val longitude: String
)
