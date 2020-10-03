package com.impression.savealife.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class Post() : Parcelable {

    private var poster: Appuser? = null
    var patientName: String? = ""
    var date: String? = null
    var city: String? = ""
    var donationCenter: Place? = null
    var bloodType: String? = ""
    var details: String? = ""

    constructor(parcel: Parcel) : this() {
        poster = parcel.readParcelable(Appuser::class.java.classLoader)
        patientName = parcel.readString()
        date = parcel.readString()
        city = parcel.readString()
        donationCenter = parcel.readParcelable(Place::class.java.classLoader)
        bloodType = parcel.readString()
        details = parcel.readString()
    }


    constructor(poster: Appuser?, patientName: String, city: String, donationCenter: Place?, bloodType: String, details: String) : this() {
        this.poster = poster
        this.patientName = patientName
        this.city = city
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }



    override fun toString(): String {
        return "Post(poster='$poster', patientName='$patientName', date=$date, city='$city', donationCenter='$donationCenter', bloodType='$bloodType', details='$details')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(poster, flags)
        parcel.writeString(patientName)
        parcel.writeString(date)
        parcel.writeString(city)
        parcel.writeParcelable(donationCenter, flags)
        parcel.writeString(bloodType)
        parcel.writeString(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}
