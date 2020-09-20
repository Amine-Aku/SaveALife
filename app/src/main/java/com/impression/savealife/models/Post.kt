package com.impression.savealife.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Post() : Parcelable {

    var user: String? = ""
    var patientName: String? = ""
//    @JsonFormat(pattern = "dd/MM/yyyy [HH:mm:ss]")
    var date: String? = null
    var city: String? = ""
    var donationCenter: Place? = null
    var bloodType: String? = ""
    var details: String? = ""

    constructor(parcel: Parcel) : this() {
        user = parcel.readString()
        patientName = parcel.readString()
        date = parcel.readString()
        city = parcel.readString()
        donationCenter = parcel.readParcelable(Place::class.java.classLoader)
        bloodType = parcel.readString()
        details = parcel.readString()
    }


    constructor(user: String, patientName: String, city: String, donationCenter: Place, bloodType: String, details: String) : this() {
        this.user = user
        this.patientName = patientName
        this.city = city
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }



    override fun toString(): String {
        return "Post(user='$user', patientName='$patientName', date=$date, city='$city', donationCenter='$donationCenter', bloodType='$bloodType', details='$details')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user)
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
