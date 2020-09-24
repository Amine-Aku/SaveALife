package com.impression.savealife.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.impression.savealife.R
import com.impression.savealife.models.Place
import com.impression.savealife.models.Post

class PatientActivity : AppCompatActivity() {

    private val TAG = "PatientActivity"
    private lateinit var post: Post

    private lateinit var gpsBtn: ImageView
    private lateinit var donationCenter: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        intent?.let {
            post = intent.extras!!.getParcelable<Post>("patient")!!
            Log.d(TAG, "onCreate: Post Bundle Received $post")
            title = post.patientName
            init()
        }


    }

    private fun init(){
        findViewById<TextView>(R.id.patient_city).text = post.city
        findViewById<TextView>(R.id.patient_date).text = post.date
        donationCenter = post.donationCenter!!
        findViewById<TextView>(R.id.patient_input_donation_center).text = donationCenter.placeName
        findViewById<TextView>(R.id.patient_input_bloodType).text = post.bloodType
        findViewById<TextView>(R.id.patient_input_details).text = post.details
        gpsBtn = findViewById(R.id.center_donation_gps_btn)

        gpsBtn.setOnClickListener{
            openMap()
        }
    }


    private fun openMap(){
        Toast.makeText(this, "Open Map Activity", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("center", post.donationCenter)
        intent.putExtra("patientName", post.patientName)
        startActivity(intent)
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        overridePendingTransition(0,0)
    }
}
