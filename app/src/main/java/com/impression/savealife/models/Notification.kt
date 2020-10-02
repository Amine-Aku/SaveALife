package com.impression.savealife.models

import java.util.*

class Notification() {
    var id: Long? = null
    var date: String? = null
    var title: String = ""
    var body: String = ""
    var topic: String? = null
    var token: String? = null
    var userId: String? = null

    //    Construtors

    constructor(title: String, body: String, topic: String, userId: String? = null): this() {
        this.title = title
        this.body = body
        this.topic = topic
        this.userId = userId
    }

    override fun toString(): String {
        return "Notification(id=$id, date=$date, title='$title', body='$body', topic=$topic, token=$token, data=$userId)"
    }







}
