package com.impression.savealife.models

import android.os.Parcel
import android.os.Parcelable
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng


class Place(var id: Long?, var title: String?, private var lat: Double, private var lng: Double, private var city: String?, var placeName: String?) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getPoint(): Point =  Point.fromLngLat(lng, lat)


    fun getLatLng(): LatLng = LatLng(lat, lng)





    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeString(city)
        parcel.writeString(placeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Place(id=$id, title=$title, lat=$lat, lng=$lng, city=$city, placeName=$placeName)"
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
