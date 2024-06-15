package com.wisyuk.data.response

data class PaymentMethodResponse(
	val data: List<PaymentMethodItem>? = null,
	val status: String? = null
)

data class PaymentMethodItem(
	val name: String,
	val createdAt: String,
	val id: Int
)

