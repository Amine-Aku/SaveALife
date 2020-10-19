package com.impression.savealife.activities

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
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.api.PrivateAPIs
import com.impression.savealife.models.Cst
import com.impression.savealife.models.Notification
import com.impression.savealife.models.Place
import com.impression.savealife.models.Post
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewPostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "NewPostActivity"
    private val PLACE_PICKER_REQUEST_CODE = 100

    private lateinit var postBtn: Button
//    private lateinit var gpsBtn: ImageView
    private lateinit var patientNameField: EditText
    private lateinit var donationCenterField: TextView
    private lateinit var detailsField: EditText
    private lateinit var bloodTypeSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var donationCenterSpinner: Spinner
    private lateinit var donationCenterSwitch: Switch

    private lateinit var donationCenterSpinnerLayout: LinearLayout

    private var donationCenterList: List<Place>? = null
    private var cityNameList: List<String>? = null


//    private lateinit var bloodType: String
    private var donationCenter: Place? = null




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, PrivateAPIs.MAPBOX_ACCESS_TOKEN)
        setContentView(R.layout.activity_new_post)

        title = resources.getString(R.string.new_post)

        init()

    }

    private fun createNotificationFromPost(post: Post){
        val title = getString(R.string.SOS_notification_title)
        val body = post!!.patientName + " " +getString(R.string.SOS_notification_body)
        val userId = Cst.currentUser!!.id.toString()
        val notification = Notification(title, body, post.city!!, userId)
        Log.d(TAG, "createNotificationFromPost: Notification Created $notification")
        val registerNotifCall = ApiClient.getNotificationServices().addNotification(notification, Cst.token)

        // register notif in the BD then send it to users
        registerNotifCall.enqueue(object : Callback<Notification>{
            override fun onFailure(call: Call<Notification>, t: Throwable) {
                Log.e(TAG, "createNotificationFromPost-register: onFailure: ${t.message}")
                fastToast(t.message!!)
            }

            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
                if(!response.isSuccessful){
                    Log.e(TAG, "createNotificationFromPost-register : onResponse not Successful : $response \n$notification")
                    return
                }
                val newNotification = response.body()
                Log.d(TAG, "createNotificationFromPost-register: onResponse: Successful: $newNotification")
            }
        })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        loadCityNames()

        postBtn = findViewById(R.id.post_btn)
        patientNameField = findViewById(R.id.post_input_name)
        citySpinner = findViewById(R.id.post_city_spinner)
        donationCenterSpinner = findViewById(R.id.post_donationCenter_spinner)
        donationCenterSpinnerLayout = findViewById(R.id.post_donation_center_spinner_layout)
        donationCenterSwitch = findViewById(R.id.post_donation_center_switch)
        bloodTypeSpinner = findViewById(R.id.post_bloodType_spinner)
        detailsField = findViewById(R.id.post_input_details)

        donationCenterSpinnerLayout.visibility = View.GONE

        setSpinner(citySpinner, Cst.CITY_NAMES_LIST())
        setSpinner(bloodTypeSpinner, Cst.BLOOD_TYPE_LIST)

        donationCenterSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                donationCenterSpinnerLayout.visibility = View.VISIBLE
                loadPlaces(citySpinner.selectedItem.toString())
            }
            else donationCenterSpinnerLayout.visibility = View.GONE
        }


        postBtn.setOnClickListener {
            createNewPost()
        }

    }

    private fun loadCityNames(){
        ApiClient.getPlaceServices().getCityNames(Cst.token)
            .enqueue(object : Callback<List<String>>{
                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.e(TAG, "loadCityName: onFailure: ${t.message}")
                    Cst.fastToast(this@NewPostActivity, "Failed to load donation Centers")
                    return
                }

                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "loadCityName: onResponse: not Successful : ${response.code()} : ${response.body()}")
                        Cst.fastToast(this@NewPostActivity, "loading no successful")
                        return
                    }
                    else{
                        Log.d(TAG, "loadCityName: onResponse: Successful : ${response.body()}")
                        cityNameList = response.body()!!
                        setSpinner(citySpinner, cityNameList!!)
                    }
                }
            })
    }

    private fun loadPlaces(city: String){
        ApiClient.getPlaceServices().getDonationCenters(city, Cst.token)
            .enqueue(object : Callback<List<Place>>{
                override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                    Log.e(TAG, "loadPlaces: onFailure: ${t.message}")
                    Cst.fastToast(this@NewPostActivity, "Failed to load donation Centers")
                    return
                }

                override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "loadPlaces: onResponse: not Successful : ${response.code()} : ${response.body()}")
                        Cst.fastToast(this@NewPostActivity, "loading no successful")
                        return
                    }
                    else{
                        Log.d(TAG, "loadPlaces: onResponse: Successful : ${response.body()}")
                        donationCenterList = response.body()!!
                        setSpinner(donationCenterSpinner, getDonationCenterNameList())
                    }
                }
            })
    }

    private fun getDonationCenterNameList(): List<String>{
        var list = arrayListOf<String>()
            donationCenterList!!.forEach {
                list.add(it.title!!)
            }
        Log.d(TAG, "getDonationCenterNameList: $list")
        return list
    }

    private fun setSpinner(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(0) // default selected item 0
    }

    private fun selectedCity(): String = citySpinner.selectedItem.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewPost(){
//            Fetsh data from the Inputs fields
        val newPost = getNewPostFromFields()
        if(newPost == null) {
            Log.d(TAG, "createNewPost : newPost is null")
            fastToast("Enter Patient Name & City")
            return
        }
        Log.d(TAG, "createNewPost : newPost is not null")
        val call = ApiClient.getPostServices().addPost(newPost, Cst.token)

        call.enqueue(object: Callback<Post>{
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(TAG, "createNewPost : onFailure: ${t.message}")
                Toast.makeText(this@NewPostActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            @SuppressLint("LogNotTimber")
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(!response.isSuccessful){
                    Log.e(TAG, "createNewPost : onResponse not Successful : $response \n$newPost")
                    Toast.makeText(this@NewPostActivity, "Response not Successful\nCode : "+response.code(), Toast.LENGTH_LONG).show()
                    return
                }
                Toast.makeText(this@NewPostActivity, "Your Post has been created", Toast.LENGTH_SHORT).show()
                val newPost = response.body()
                // Send Notification
                createNotificationFromPost(newPost!!)
                Log.d(TAG, "createNewPost : onResponse Successful: New Post Created : $newPost")
                Log.d(TAG, "createNewPost : onResponse Successful : Donation Center : $donationCenter")
                NavUtils.navigateUpFromSameTask(this@NewPostActivity)
            }
        })
    }

    private fun goToPlacePickerActivity() {
//        val latLng = LatLng(40.7544, -73.9862)
//        val latLng = LatLng(-7.994885,31.664051)
        val cityName = selectedCity()
        val city = Cst.CITY_LIST.filter { it.name == cityName }
//        val latLng = LatLng(31.664051, -7.994885)
        val latLng = city[0].getLatlng()
        Log.d(TAG, "goToPlacePickerActivity: Open Picker on : ${city[0].toString()}")
        startActivityForResult(
            PlacePicker.IntentBuilder()
                .accessToken(PrivateAPIs.MAPBOX_ACCESS_TOKEN)
                .placeOptions(
                    PlacePickerOptions.builder()
                        .statingCameraPosition(
                            CameraPosition.Builder()
                                .target(latLng).zoom(11.0).build())
                        .build())
                .build(this), PLACE_PICKER_REQUEST_CODE);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNewPostFromFields(): Post? {
//        Fetsh data from the Inputs fields
        val patientName= patientNameField.text.toString().trim()

        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy [HH:mm:ss]")

//        Cannot add format to date cuz it doesn't match the Date Class in the server
        val date = LocalDateTime.now().toString()
        val bloodType = bloodTypeSpinner.selectedItem.toString()
        val city = selectedCity()
        val details: String = detailsField.text.toString().trim()

        donationCenter = if(donationCenterSwitch.isChecked) donationCenterList?.get(donationCenterSpinner.selectedItemPosition)
        else null

        val post = Post(Cst.currentUser!!.username, patientName, city,donationCenter,bloodType, details)
        Log.d(TAG, "getNewPostFromFields: New Post : $post")
        return if(fieldsAreValid(patientName, city)) post
        else null
    }

    private fun fieldsAreValid(patientName: String, city: String): Boolean = !(patientName == "" || city == "")

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
//                donationCenter = Place(carmen.text()!!, p.latitude(), p.longitude(), city, carmen.placeName()!!)
            }
            else{
                Log.d(TAG, "onActivityResult: No place picked")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected: Nothing is selected in the spinner")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSelected = parent!!.getItemAtPosition(position).toString()
        Log.d(TAG, "onItemSelected: Item ($itemSelected) is selected in the spinner")
        if(cityNameList != null && cityNameList!!.contains(itemSelected)){
            loadPlaces(itemSelected)
        }
    }


    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }

    private fun fastToast(msg: String) = Cst.fastToast(this@NewPostActivity, msg)
}
