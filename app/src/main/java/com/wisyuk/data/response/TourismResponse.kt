package com.wisyuk.data.response

import com.google.gson.annotations.SerializedName

data class TourismResponse (
    @field:SerializedName("listTourism")
    val listTourism: List<ListTourismItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


data class ListTourismItem(

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String,
)