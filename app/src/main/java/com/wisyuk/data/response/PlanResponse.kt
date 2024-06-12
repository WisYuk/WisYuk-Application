package com.wisyuk.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlanResponse(

	@field:SerializedName("data")
	val data: List<PlanTourismItem>,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class PlanTourismItem(

	@field:SerializedName("go_at")
	val goAt: String,

	@field:SerializedName("name")
	val name: String
) : Parcelable
