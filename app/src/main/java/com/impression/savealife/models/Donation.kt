package com.impression.savealife.models

import java.util.*

class Donation {
    //    Getters & Setters
    private var id: Long? = null
    private var date: Date? = null
    private var donator: Appuser? = null
    private var patientName: String? = null
    private var details: String? = null

    //    Constructors
    constructor(id: Long?, date: Date?, donator: Appuser?, patientName: String?, details: String?) {
        this.id = id
        this.date = date
        this.donator = donator
        this.patientName = patientName
        this.details = details
    }

    constructor(date: Date?, donator: Appuser?, patientName: String?, details: String?) {
        this.date = date
        this.donator = donator
        this.patientName = patientName
        this.details = details
    }

    override fun toString(): String {
        return "donation{id=$id, date=$date, donator=$donator, patientName='$patientName', details='$details'}"
    }
}
