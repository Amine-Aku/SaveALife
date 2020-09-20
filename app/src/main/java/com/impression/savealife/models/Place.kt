package com.impression.savealife.models

import android.os.Parcel
import android.os.Parcelable
import com.mapbox.geojson.Point


class Place(private var title: String?, private var lat: Double, private var lng: Double, private var city: String?, var placeName: String?)
    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    private fun getPoint(): Point{
        return Point.fromLngLat(lng, lat)
    }

    override fun toString(): String {
        return "Place(title='$title', lat=$lat, lng=$lng, city='$city', placeName='$placeName')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeString(city)
        parcel.writeString(placeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }

}
