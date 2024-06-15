package com.wisyuk.data.extras
import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        tourismId = parcel.readInt(),
        photoUrl = parcel.readString(),
        hotelName = parcel.readString(),
        hotelId = parcel.readInt(),
        hotelPrice = parcel.readInt(),
        rideName = parcel.readString(),
        rideId = parcel.readInt(),
        ridePrice = parcel.readInt(),
        tourGuideName = parcel.readString(),
        tourGuideId = parcel.readInt(),
        tourGuidePrice = parcel.readInt(),
        date = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tourismId)
        parcel.writeString(photoUrl)
        parcel.writeString(hotelName)
        parcel.writeInt(hotelId)
        parcel.writeInt(hotelPrice)
        parcel.writeString(rideName)
        parcel.writeInt(rideId)
        parcel.writeInt(ridePrice)
        parcel.writeString(tourGuideName)
        parcel.writeInt(tourGuideId)
        parcel.writeInt(tourGuidePrice)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingDetails> {
        override fun createFromParcel(parcel: Parcel): BookingDetails {
            return BookingDetails(parcel)
        }

        override fun newArray(size: Int): Array<BookingDetails?> {
            return arrayOfNulls(size)
        }
    }
}
