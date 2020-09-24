package com.impression.savealife.activities

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.impression.savealife.R
import com.impression.savealife.api.MapboxToken
import com.impression.savealife.models.Place
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {

    private val TAG = "MapActivity"

    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private var permissionsManager = PermissionsManager(this)
    private lateinit var localizationPlugin: LocalizationPlugin
    private lateinit var navigation: MapboxNavigation
    private var navigationMapRoute: NavigationMapRoute? = null

    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private val symbolIconId = "symbolIconId"

    private lateinit var donationCenter: Place
    private var currentRoute: DirectionsRoute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, MapboxToken.access_token)
        setContentView(R.layout.activity_map)
        navigation = MapboxNavigation(applicationContext, MapboxToken.access_token)

        intent?.let {
            donationCenter = it.extras!!.getParcelable<Place>("center")!!
            title = it.extras!!.getString("patientName")
        }

        mapView = findViewById(R.id.patient_map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
//        init map and buttons
        this.mapBoxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS){style->
            matchMapLanguageWithDevice(mapView, mapboxMap, style)
            getDestinationLocation(style)
            val launchNavigationAssistantButton = findViewById<FloatingActionButton>(R.id.launch_navigation_assistant_button)
            findViewById<FloatingActionButton>(R.id.device_location_button).setOnClickListener {
                getDeviceLocation(style)
            }
            findViewById<FloatingActionButton>(R.id.center_location_button).setOnClickListener {
                animateCamemra(donationCenter.getLatLng(), 1000)
            }
            findViewById<FloatingActionButton>(R.id.center_direction_button).setOnClickListener {
                drawCenterDirection(style)
                launchNavigationAssistantButton.visibility = View.VISIBLE
            }
            launchNavigationAssistantButton.setOnClickListener {
                launchNavigationAssistant(currentRoute!!)
            }

        }
    }


    // Get Destination
    private fun getDestinationLocation(style: Style) {
        setupSymbolLayer(style)
        animateCamemra(donationCenter.getLatLng(), 4000)

    }

    private fun getCarmenFeatureFromPlace(place: Place): CarmenFeature{
        return CarmenFeature.builder().text(place.placeName!!.substringBefore(','))
            .geometry(donationCenter.getPoint())
            .placeName(place.placeName)
            .id("donation_center_id")
            .properties(JsonObject())
            .build();
    }

    private fun setupSymbolLayer(style: Style){
        style.apply{
            addImage(symbolIconId, getDrawable(R.drawable.ic_location_marker)!!)
            addSource(GeoJsonSource(geojsonSourceLayerId))
            addLayer(
                SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId)
                    .withProperties(
                        PropertyFactory.iconImage(symbolIconId),
                        PropertyFactory.iconOffset(arrayOf(0f, -23.333f))
//An offset is added so the bottom of the marker icon gets fixed to the coordinate, rather than the middle of the icon
                    // ratio : (0f, -8f) for 24dp vector icon
                    ))
            getSourceAs<GeoJsonSource>(geojsonSourceLayerId)?.setGeoJson(
                FeatureCollection.fromFeatures(
                    arrayOf(Feature.fromJson(getCarmenFeatureFromPlace(donationCenter).toJson()))))
        }
    }
    //>


    //enableLocationComponent
    private fun getDeviceLocation(loadedMapStyle: Style): Location? {
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

                zoomWhileTracking(15.0)
                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
            return mapBoxMap.locationComponent.lastKnownLocation!!
        }
        else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
            return null
        }

    }

    // Draw Navigation Route
    private fun drawCenterDirection(style: Style) {
        Log.d(TAG, "drawCenterDirection: Activated")
        val location = getDeviceLocation(style)
        val origin = Point.fromLngLat(location!!.longitude, location!!.latitude)
        val destination = donationCenter.getPoint()
        Log.d(TAG, "drawCenterDirection: Device coordinates : $origin")

        val nav = NavigationRoute.builder(this)
            .accessToken(MapboxToken.access_token)
            .origin(origin, location.bearing.toDouble(), 90.0)
            .destination(destination)
            .build()
            .getRoute(object: Callback<DirectionsResponse> {
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Log.d(TAG, "drawCenterDirection: onFailure: Error ! ${t.message}")
                    fastToast("Error")
                }
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    if(response.body() == null || response.body()!!.routes().size < 1){
                        Log.d(TAG, "drawCenterDirection: onResponse: No route found, Check the user and access token")
                        fastToast("No route found")
                        return
                    }
                    else {
//                        fetch the route from the response
                        currentRoute = response.body()!!.routes()[0]
                        Log.d(TAG, "drawCenterDirection: onResponse: Route found : $currentRoute")
                        // Draw the route on the map
                        navigationMapRoute = NavigationMapRoute(null, mapView, mapBoxMap, R.style.NavigationMapRoute)
                        navigationMapRoute!!.addRoute(currentRoute)
//                        how to control the zoom
                        animateCamemra(getDistanceMedian(origin, destination), 1000, 14.0)

                    }
                }
            })
    }

    private fun getDistanceMedian(origin: Point, destination: Point): LatLng{
        val lat = (origin.latitude()+destination.latitude())/2
        val lng = (origin.longitude()+destination.longitude())/2
        return LatLng(lat,lng)
    }

    private fun launchNavigationAssistant(route: DirectionsRoute){
        // Create a NavigationLauncherOptions object to package everything together
        val options = NavigationLauncherOptions.builder()
            .directionsRoute(route)
            .shouldSimulateRoute(true)
            .build()

// Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(this, options)
    }
    //>

    private fun animateCamemra(latLng: LatLng, duration: Int, zoom: Double = 15.0){
        mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build()), duration)
    }

    // Match map with the default device language
    private fun matchMapLanguageWithDevice(v: MapView, m: MapboxMap, s: Style) {
        localizationPlugin = LocalizationPlugin(v, m, s)
        localizationPlugin.matchMapLanguageWithDeviceDefault(true)
    }

    // Interface implementations
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Log.d(TAG, "onExplanationNeeded: Explanation ......")
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            Log.d(TAG, "onPermissionResult: User location permission granted")
            getDeviceLocation(mapBoxMap.style!!)
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
    //>

//    Toast shortcut
    private fun fastToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    // MapView lifecycle
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        navigation.onDestroy()
    }



}
