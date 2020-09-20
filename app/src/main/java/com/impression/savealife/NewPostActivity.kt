package com.impression.savealife

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NavUtils
import com.impression.savealife.api.ApiClient
import com.impression.savealife.api.MapboxToken
import com.impression.savealife.models.Constants
import com.impression.savealife.models.Place
import com.impression.savealife.services.PostServices
import com.impression.savealife.models.Post
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewPostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "NewPostActivity"
    private val PLACE_PICKER_REQUEST_CODE = 100

    private lateinit var postBtn: Button
    private lateinit var gpsBtn: ImageView
    private lateinit var patientNameField: EditText
    private lateinit var donationCenterField: TextView
    private lateinit var detailsField: EditText
    private lateinit var bloodTypeSpinner: Spinner
    private lateinit var citySpinner: Spinner


//    private lateinit var bloodType: String
    private lateinit var donationCenter: Place




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, MapboxToken.access_token)
        setContentView(R.layout.activity_new_post)

        title = resources.getString(R.string.new_post)

        init()

        gpsBtn.setOnClickListener {
            goToPlacePickerActivity()
        }

        postBtn.setOnClickListener {
//            Fetsh data from the Inputs fields
            val newPost = getNewPostFromFields()
            val call = ApiClient.getPostServices().addPost(newPost)

            call.enqueue(object: Callback<Post>{
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                    Toast.makeText(this@NewPostActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                @SuppressLint("LogNotTimber")
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "New Post.onClick : onResponse not Successful : $response \n$newPost")
                        Toast.makeText(this@NewPostActivity, "Response not Successful\nCode : "+response.code(), Toast.LENGTH_LONG).show()
                        return
                    }
                    Toast.makeText(this@NewPostActivity, "Your Post has been created", Toast.LENGTH_SHORT).show()
                    val newPost = response.body()
                    Log.d(TAG, "New Post.onClick : onResponse Successful: New Post Created : $newPost")
                    Log.d(TAG, "New Post.onClick : onResponse Successful : Donation Center : $donationCenter")
                    NavUtils.navigateUpFromSameTask(this@NewPostActivity)
                }
            })
        }
    }

    private fun selectedCity(): String = citySpinner.selectedItem.toString()

    private fun init(){
        gpsBtn = findViewById(R.id.center_donation_gps_btn)
        postBtn = findViewById(R.id.post_btn)
        patientNameField = findViewById(R.id.post_input_name)
        citySpinner = findViewById(R.id.post_city_spinner)
        donationCenterField = findViewById(R.id.post_input_donation_center)
        bloodTypeSpinner = findViewById(R.id.post_bloodType_spinner)
        detailsField = findViewById(R.id.post_input_details)

        setSpinner(citySpinner, Constants.CITY_NAMES_LIST())
        setSpinner(bloodTypeSpinner, Constants.BLOOD_TYPE_LIST)
    }

    private fun setSpinner(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(0) // default selected item 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNewPostFromFields(): Post{
//        Fetsh data from the Inputs fields
        val patientName= patientNameField.text.toString().trim()

        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy [HH:mm:ss]")

//        Cannot add format to date cuz it doesn't match the Date Class in the server
        val date = LocalDateTime.now().toString()
        val bloodType = bloodTypeSpinner.selectedItem.toString()
        val city = selectedCity()
        val details: String = detailsField.text.toString().trim()

        // Temporarly we passed donationCenter as a String, need to add the class 'Place' in the Server
        return Post("", patientName, city,donationCenter,bloodType, details)
    }

    private fun goToPlacePickerActivity() {
//        val latLng = LatLng(40.7544, -73.9862)
//        val latLng = LatLng(-7.994885,31.664051)
        val cityName = selectedCity()
        val city = Constants.CITY_LIST.filter { it.name == cityName }
//        val latLng = LatLng(31.664051, -7.994885)
        val latLng = city[0].getLatlng()
        Log.d(TAG, "goToPlacePickerActivity: Open Picker on : ${city[0].toString()}")
        startActivityForResult(
            PlacePicker.IntentBuilder()
                .accessToken(MapboxToken.access_token)
                .placeOptions(
                    PlacePickerOptions.builder()
                        .statingCameraPosition(
                            CameraPosition.Builder()
                                .target(latLng).zoom(11.0).build())
                        .build())
                .build(this), PLACE_PICKER_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED){
            Log.d(TAG, "onActivityResult: RESULT CANCELED")
        }
        else if(requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: RESULT OK")
            val carmen = PlacePicker.getPlace(data)
            if(carmen != null){
                Log.d(TAG, "onActivityResult: ${carmen.toJson()}")
                donationCenterField.text = carmen.placeName()
                //create an instance for the Place picked from CarmenFeature object
                val p: Point = carmen.geometry() as Point
                val city = selectedCity()
                donationCenter = Place(carmen.text()!!, p.latitude(), p.longitude(), city, carmen.placeName()!!)
            }
            else{
                Log.d(TAG, "onActivityResult: No place picked")
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected: No Blood Type is selected in the spinner")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSelected = parent!!.getItemAtPosition(position).toString()
        Log.d(TAG, "onItemSelected: Blood Type ($itemSelected) is selected in the spinner")
//        Toast.makeText(this, itemSelected, Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }
}
