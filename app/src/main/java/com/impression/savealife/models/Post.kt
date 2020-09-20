package com.impression.savealife.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Post {

    var user: String = ""
    var patientName: String = ""
    var date: String? = null
    var city: String = ""
    var donationCenter: String = ""
    var bloodType: String = ""
    var details: String = ""


    constructor(user: String, patientName: String, date: String?, city: String, donationCenter: String, bloodType: String, details: String) {
        this.user = user
        this.patientName = patientName
        this.date = date
        this.city = city
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }



    override fun toString(): String {
        return "Post(user='$user', patientName='$patientName', date=$date, city='$city', donationCenter='$donationCenter', bloodType='$bloodType', details='$details')"
    }

}
