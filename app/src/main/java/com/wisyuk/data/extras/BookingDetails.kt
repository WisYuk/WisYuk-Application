package com.wisyuk.data.extras

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingDetails(
    val tourismId: Int,
    val photoUrl: String?,
    val hotelName: String?,
    val hotelId: Int,
    val hotelPrice: Int,
    val rideName: String?,
    val rideId: Int,
    val ridePrice: Int,
    val tourGuideName: String?,
    val tourGuideId: Int,
    val tourGuidePrice: Int,
    val date: String?
) : Parcelable