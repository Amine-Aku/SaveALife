package com.impression.savealife.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Post {

    var id: Long? = null
    var user: String = ""
    var patientName: String = ""
    var date: String? = null
    var hospital: String = ""
    var donationCenter: String = ""
    var bloodType: String = ""
    var details: String = ""

    constructor(id: Long?, user: String, patientName: String, date: String?, hospital: String, donationCenter: String, bloodType: String, details: String) {
        this.id = id
        this.user = user
        this.patientName = patientName
        this.date = date
        this.hospital = hospital
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }

    constructor(user: String, patientName: String, date: String?, hospital: String, donationCenter: String, bloodType: String, details: String) {
        this.user = user
        this.patientName = patientName
        this.date = date
        this.hospital = hospital
        this.donationCenter = donationCenter
        this.bloodType = bloodType
        this.details = details
    }


    init {

    }

    override fun toString(): String {
        return "Post(id=$id, user='$user', patientName='$patientName', date=$date, hospital='$hospital', donationCenter='$donationCenter', bloodType='$bloodType', details='$details')"
    }

}

object ListPosts{
    @RequiresApi(Build.VERSION_CODES.O)
    var list = arrayListOf<Post>(
        Post("test", "test", "test", "test","test","test","test")
    )
}
