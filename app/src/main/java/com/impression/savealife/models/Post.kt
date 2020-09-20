package com.impression.savealife.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Post {

    var user: String = ""
    var patientName: String = ""
//    @JsonFormat(pattern = "dd/MM/yyyy [HH:mm:ss]")
    var date: String? = null
    var city: String = ""
    var donationCenter: Place? = null
    var bloodType: String = ""
    var details: String = ""


    constructor(user: String, patientName: String, date: String?, city: String, donationCenter: Place, bloodType: String, details: String) {
        this.user = user
        this.patientName = patientName
        this.date = date
        this.city = city
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }

    constructor(user: String, patientName: String, city: String, donationCenter: Place, bloodType: String, details: String) {
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

}
