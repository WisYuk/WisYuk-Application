package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class PaymentReceiptResponse(

	@field:SerializedName("data")
	val data: Receipt,

	@field:SerializedName("status")
	val status: String
)

data class Receipt(

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("hotel_price")
	val hotelPrice: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("guide_name")
	val guideName: String,

	@field:SerializedName("payment_total")
	val paymentTotal: Int,

	@field:SerializedName("payment_method_name")
	val paymentMethodName: String,

	@field:SerializedName("ride_price")
	val ridePrice: Int,

	@field:SerializedName("payment_methods_id")
	val paymentMethodsId: Int,

	@field:SerializedName("hotel_name")
	val hotelName: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("tour_guide_price")
	val tourGuidePrice: Int,

	@field:SerializedName("users_id")
	val usersId: Int,

	@field:SerializedName("ride_name")
	val rideName: String,

	@field:SerializedName("status")
	val status: Int
)
