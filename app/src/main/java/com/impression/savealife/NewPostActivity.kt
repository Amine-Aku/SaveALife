package com.impression.savealife

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.impression.savealife.api.ApiClient
import com.impression.savealife.services.PostServices
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class NewPostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "NewPostActivity"

    private lateinit var postBtn: Button
    private lateinit var gpsBtn: ImageView
    private lateinit var patientNameField: EditText
    private lateinit var cityField: EditText
    private lateinit var donationCenterField: TextView
    private lateinit var detailsField: EditText
    private lateinit var spinner: Spinner

    private lateinit var donationCenter: String
    private lateinit var bloodType: String

    private val BLOOD_TYPE_LIST = arrayOf(
        "None", "O+","O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        title = resources.getString(R.string.new_post)

        init()

        gpsBtn.setOnClickListener {
            Log.d(TAG, "onCreate: Open Map Activity")
            Toast.makeText(this, "Open Map Activity", Toast.LENGTH_SHORT).show()
            donationCenterField.text = "Center X"
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

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "onResponse: Code : " +response.code())
                        Toast.makeText(this@NewPostActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                        return
                    }
                    val post = response.body()
                    // QUESTION abt the default <- Child back button
                    finish()
                }
            })
        }

    }

    private fun init(){
        gpsBtn = findViewById(R.id.center_donation_gps_btn)
        postBtn = findViewById(R.id.post_btn)
        patientNameField = findViewById(R.id.post_input_name)
        cityField = findViewById(R.id.post_input_city)
        donationCenterField = findViewById(R.id.post_input_donation_center)
        bloodType = getString(R.string.not_specified)
        spinner = findViewById(R.id.bloodType_spinner)
        detailsField = findViewById(R.id.post_input_details)

        setSpinner()
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BLOOD_TYPE_LIST)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNewPostFromFields(): Post{
//        Fetsh data from the Inputs fields
        val patientName= patientNameField.text.toString().trim()
        val date = LocalDateTime.now().toString()
        val city = cityField.text.toString().trim()
        donationCenter = donationCenterField.text.toString().trim()
        val details: String = detailsField.text.toString().trim()

        return Post("", patientName, date, city,donationCenter,bloodType, details)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected: No Blood Type is selected in the spinner")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        bloodType = parent!!.getItemAtPosition(position).toString()
        Log.d(TAG, "onItemSelected: Blood Type ($bloodType) is selected in the spinner")
        Toast.makeText(this, bloodType, Toast.LENGTH_SHORT).show()
    }
}
