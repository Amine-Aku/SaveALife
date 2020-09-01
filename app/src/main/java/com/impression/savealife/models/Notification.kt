package com.impression.savealife.models

import java.util.*

class Notification {
    var id: Long? = null
    var date: Date? = null
    var destination: Appuser? = null
    var body: String? = null

    //    Construtors
    constructor(id: Long?, date: Date?, destination: Appuser?, body: String?) {
        this.id = id
        this.date = date
        this.destination = destination
        this.body = body
    }

    constructor(date: Date?, destination: Appuser?, body: String?) {
        this.date = date
        this.destination = destination
        this.body = body
    }

    override fun toString(): String {
        return "Notification(id=$id, date=$date, destination=$destination, body=$body)"
    }


}
