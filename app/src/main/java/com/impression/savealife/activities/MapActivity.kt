package com.impression.savealife.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.impression.savealife.R
import com.impression.savealife.api.MapboxToken
import com.impression.savealife.models.Place
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {

    private val TAG = "MapActivity"

    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private var permissionsManager = PermissionsManager(this)
    private lateinit var localizationPlugin: LocalizationPlugin

    private lateinit var donationCenter: Place


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, MapboxToken.access_token)
        setContentView(R.layout.activity_map)

        intent?.let {
            donationCenter = it.extras!!.getParcelable<Place>("center")!!
            title = it.extras!!.getString("patientName")
        }

        mapView = findViewById(R.id.patient_map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS){style->
            matchMapLanguageWithDevice(mapView, mapboxMap, style)
            findViewById<FloatingActionButton>(R.id.location_button).setOnClickListener {
                enableLocationComponent(style)
            }
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style){
        // Check if permissions are enabled and if not request
        if(PermissionsManager.areLocationPermissionsGranted(this)){

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .elevation(5f)
                .accuracyAlpha(.6f)
                .accuracyColor(ContextCompat.getColor(this, R.color.mapbox_blue))
                .pulseEnabled(true)
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            // Get an instance of the LocationComponent and then adjust its settings
            mapBoxMap.locationComponent.apply {
                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

                // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
        }
        else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }

    }

    private fun matchMapLanguageWithDevice(v: MapView, m: MapboxMap, s: Style) {
        localizationPlugin = LocalizationPlugin(v, m, s)
        localizationPlugin.matchMapLanguageWithDeviceDefault(true)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Log.d(TAG, "onExplanationNeeded: Explanation ......")
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            Log.d(TAG, "onPermissionResult: User location permission granted")
            enableLocationComponent(mapBoxMap.style!!)
        }
        else {
            Log.i(TAG, "onPermissionResult: User location permission not granted")
            Toast.makeText(this, "User location permission not granted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

}
