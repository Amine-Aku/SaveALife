package com.impression.savealife.models

import java.util.*

class Notification {
    var id: Long? = null
    var date: String? = null
    var title: String = ""
    var body: String = ""
    var topic: String? = null
    var token: String? = null

    constructor(title: String, body: String, topic: String) {
        this.title = title
        this.body = body
        this.topic = topic
    }

    //    Construtors


    override fun toString(): String {
        return "Notification(id=$id, date=$date, title='$title', body='$body', topic='$topic', token='$token')"
    }


}
