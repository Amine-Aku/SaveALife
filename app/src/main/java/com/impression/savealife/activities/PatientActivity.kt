package com.impression.savealife.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.impression.savealife.R
import com.impression.savealife.models.Cst
import com.impression.savealife.models.Place
import com.impression.savealife.models.Post

class PatientActivity : AppCompatActivity() {

    private val TAG = "PatientActivity"
    private lateinit var post: Post

    private lateinit var gpsBtn: ImageView
    private var donationCenter: Place? = null

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
        post.donationCenter?.let {
            donationCenter = it
            findViewById<TextView>(R.id.patient_input_donation_center).text = it.placeName
        }
        findViewById<TextView>(R.id.patient_input_bloodType).text = post.bloodType
        findViewById<TextView>(R.id.patient_input_details).text = post.details
        gpsBtn = findViewById(R.id.center_donation_gps_btn)

        gpsBtn.setOnClickListener{
            openMap()
        }
    }


    private fun openMap(){
        if(donationCenter == null) {
            Cst.fastToast(this, "No donation Center informed")
            Log.d(TAG, "openMap: No donation Center informed")
        }
        else {
            Cst.fastToast(this, "Opening Map")
            Log.d(TAG, "openMap: Open Map Activity")
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("center", post.donationCenter)
            intent.putExtra("patientName", post.patientName)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        overridePendingTransition(0,0)
    }
}
