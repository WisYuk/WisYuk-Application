package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class DetailTourismResponse(

	@field:SerializedName("dataTourGuides")
	val dataTourGuides: List<DataTourGuidesItem>,

	@field:SerializedName("dataRides")
	val dataRides: List<DataRidesItem>,

	@field:SerializedName("dataHotels")
	val dataHotels: List<DataHotelsItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataTourGuidesItem(

	@field:SerializedName("image")
	val image: Any,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("tourism_id")
	val tourismId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("tour_guides_id")
	val tourGuidesId: Int
)

data class DataHotelsItem(

	@field:SerializedName("image")
	val image: Any,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("min_price")
	val minPrice: Int,

	@field:SerializedName("max_price")
	val maxPrice: Int,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("hotels_id")
	val hotelsId: Int,

	@field:SerializedName("tourism_id")
	val tourismId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("longitude")
	val longitude: String
)

data class DataRidesItem(

	@field:SerializedName("image")
	val image: Any,

	@field:SerializedName("rides_id")
	val ridesId: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("tourism_id")
	val tourismId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int
)
