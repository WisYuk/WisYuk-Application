package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class PaymentReceiptResponse(

	@field:SerializedName("Receipt")
	val data: Receipt,

	@field:SerializedName("status")
	val status: String
)

data class Receipt(

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("hotel_price")
	val hotelPrice: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("guide_name")
	val guideName: String? = null,

	@field:SerializedName("payment_total")
	val paymentTotal: Int? = null,

	@field:SerializedName("payment_method_name")
	val paymentMethodName: String? = null,

	@field:SerializedName("ride_price")
	val ridePrice: Int? = null,

	@field:SerializedName("payment_methods_id")
	val paymentMethodsId: Int? = null,

	@field:SerializedName("hotel_name")
	val hotelName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("tour_guide_price")
	val tourGuidePrice: Int? = null,

	@field:SerializedName("users_id")
	val usersId: Int? = null,

	@field:SerializedName("ride_name")
	val rideName: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
