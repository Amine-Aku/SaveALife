package com.impression.savealife.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.math.floor

class Appuser() : Parcelable {
    //    Getter & Setter
    var id: Long? = null
    var username: String? = null
    var password: String? = null
    var bloodType: String? = null
    var lastDonation: String? = null
    var city: String? = null
    var active: Boolean? = null
    var hasDonated: Boolean? = null

    init {
        hasDonated = false
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        username = parcel.readString()
        password = parcel.readString()
        bloodType = parcel.readString()
        lastDonation = parcel.readString()
        city = parcel.readString()
        active = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        hasDonated = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    //    Constructos
    constructor(username: String?, password: String?, city: String?, bloodType: String?) : this() {
        this.username = username
        this.password = password
        this.bloodType = bloodType
        this.city = city
    }

    constructor(map: Map<String, Objects>) : this() {
        this.id = floor(map["id"] as Double).toLong()
        this.username = map["sub"].toString()
        this.bloodType = map["bloodType"].toString()
        this.city = map["city"].toString()
        this.lastDonation = map["lastDonation"].toString()
        this.active = map["active"] as Boolean
        this.hasDonated = map["hasDonated"] as Boolean?
        if(hasDonated == null) hasDonated = false
    }

    constructor(id: Long, username: String?, city: String?, bloodType: String?, lastDonation: String?, active: Boolean, hasDonated: Boolean) : this() {
        this.id = id
        this.username = username
        this.city = city
        this.bloodType = bloodType
        this.lastDonation = lastDonation
        this.active = active
        this.hasDonated = hasDonated
    }




    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(bloodType)
        parcel.writeString(lastDonation)
        parcel.writeString(city)
        parcel.writeValue(active)
        parcel.writeValue(hasDonated)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Appuser(id=$id, username=$username, password=$password, bloodType=$bloodType, lastDonation=$lastDonation, city=$city, active=$active, hasDonated=$hasDonated)"
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
