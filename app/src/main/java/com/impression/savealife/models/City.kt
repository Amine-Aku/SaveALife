package com.impression.savealife.models

import com.mapbox.mapboxsdk.geometry.LatLng

class City(val name: String, private var lat: Double, private var lng: Double) {
    fun getLatlng(): LatLng = LatLng(lat, lng)
    override fun toString(): String {
        return "City(name='$name', latitude=$lat, longitude=$lng)"
    }

}
