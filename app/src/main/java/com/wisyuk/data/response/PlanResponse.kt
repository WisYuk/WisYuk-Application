package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class PlanResponse(

	@field:SerializedName("data")
	val data: List<PlanTourismItem>,

	@field:SerializedName("status")
	val status: String
)

data class PlanTourismItem(

	@field:SerializedName("go_at")
	val goAt: String,

	@field:SerializedName("name")
	val name: String
)
