package com.impression.savealife.models

import java.util.*

class Appuser {
    //    Getter & Setter
    private var id: Long? = null
    private var username: String? = null
    private var password: String? = null
    private var bloodType: String? = null
    private var lastDonation: Date? = null

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private var posts: Collection<Post>? = null
//    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
//    private val notifications: Collection<Notification>? =
//        null
//    @OneToMany(mappedBy = "donator")
//    private val donations: Collection<Donation>? = null

    //    Constructos
    constructor(id: Long?, username: String?, password: String?, bloodType: String?, lastDonation: Date?) {
        this.id = id
        this.username = username
        this.password = password
        this.bloodType = bloodType
        this.lastDonation = lastDonation
    }

    constructor(username: String?, password: String?, bloodType: String?, lastDonation: Date?) {
        this.username = username
        this.password = password
        this.bloodType = bloodType
        this.lastDonation = lastDonation
    }

    override fun toString(): String {
        return "User(id=$id, username=$username, password=$password, bloodType=$bloodType, lastDonation=$lastDonation)"
    }


}
