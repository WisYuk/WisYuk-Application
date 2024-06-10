package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class PreferencesResponse(

	@field:SerializedName("data")
	val data: List<PreferencesItem>,

	@field:SerializedName("status")
	val status: String
)

data class PreferencesItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
