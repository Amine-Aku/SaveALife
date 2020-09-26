package com.impression.savealife.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Appuser() : Parcelable {
    //    Getter & Setter
    private var id: Long? = null
    private var username: String? = null
    private var password: String? = null
    private var bloodType: String? = null
    private var lastDonation: Date? = null
    private var city: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        username = parcel.readString()
        password = parcel.readString()
        bloodType = parcel.readString()
        city = parcel.readString()
    }

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private var posts: Collection<Post>? = null
//    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
//    private val notifications: Collection<Notification>? =
//        null
//    @OneToMany(mappedBy = "donator")
//    private val donations: Collection<Donation>? = null

    //    Constructos
    constructor(id: Long?, username: String?, password: String?, bloodType: String?, lastDonation: Date?, city: String?) : this() {
        this.id = id
        this.username = username
        this.password = password
        this.bloodType = bloodType
        this.lastDonation = lastDonation
        this.city = city
    }

    constructor(username: String?, password: String?, bloodType: String?, lastDonation: Date?, city: String?) : this() {
        this.username = username
        this.password = password
        this.bloodType = bloodType
        this.lastDonation = lastDonation
        this.city = city
    }

    override fun toString(): String {
        return "User(id=$id, username=$username, password=$password, bloodType=$bloodType, lastDonation=$lastDonation, city=$city)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(bloodType)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appuser> {
        override fun createFromParcel(parcel: Parcel): Appuser {
            return Appuser(parcel)
        }

        override fun newArray(size: Int): Array<Appuser?> {
            return arrayOfNulls(size)
        }
    }


}
