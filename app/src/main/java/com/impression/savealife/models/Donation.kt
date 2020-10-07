package com.impression.savealife.models

import java.util.*

class Donation {
    //    Getters & Setters
    var id: Long? = null
    var date: String? = null
    var donatorId: Long? = null
    var patientPost: Post? = null
    var details: String? = null

    //    Constructors
    constructor(id: Long?, date: String?, donatorId: Long?, patientPost: Post?, details: String?) {
        this.id = id
        this.date = date
        this.donatorId = donatorId
        this.patientPost = patientPost
        this.details = details
    }

    constructor(date: String?, donator: Appuser?, patientPost: Post?, details: String?) {
        this.date = date
        this.donatorId = donatorId
        this.patientPost = patientPost
        this.details = details
    }

    override fun toString(): String {
        return "donation{id=$id, date=$date, donatorID=$donatorId, patientPost='$patientPost', details='$details'}"
    }
}
