package com.impression.savealife.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Cst
import com.impression.savealife.models.Notification
import com.impression.savealife.models.Place
import com.impression.savealife.models.Post
import kotlinx.android.synthetic.main.activity_patient.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class PatientActivity : AppCompatActivity() {

    private val TAG = "PatientActivity"
    private lateinit var post: Post

    private lateinit var gpsBtn: ImageView
    private var donationCenter: Place? = null

    private lateinit var iDonatedBtn: Button

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
        findViewById<TextView>(R.id.patient_poster).text = post.poster

        gpsBtn = findViewById(R.id.center_donation_gps_btn)
        gpsBtn.setOnClickListener{
            openMap()
        }


        iDonatedBtn = findViewById<Button>(R.id.patient_iDonated_btn)
        checkIfUserHasDonated()
            iDonatedBtn.setOnClickListener {
            registerDonation()
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

    private fun registerDonation(){
        ApiClient.getDonationServices().addDonation(post, Cst.token)
            .enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "registerDonation: onFailure: ${t.message}")
                    Cst.fastToast(this@PatientActivity, "Donation Request Failed")
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "registerDonation: onResponse: Donation Request not Successful ${response.code()} : ${response.body()}")
                        Cst.fastToast(this@PatientActivity, "Donation Request not Successful")
                        return
                    }
                    else{
                        Cst.fastToast(this@PatientActivity, "Donation registered Successfully")
                        val msg = response.body()
                        Log.d(TAG, "registerDonation:  Donation registered Successfully : $msg")
                        Cst.hasDonated = true
//                        SEND Notif to this device
                        FirebaseInstanceId.getInstance().instanceId
                            .addOnSuccessListener {
                                Log.d(TAG, "registerDonation: onResponse: FCM instanceId got")
                                sendDonationNotification(it.token)
                            }
                            .addOnFailureListener {
                                Log.e(TAG, "registerDonation: onResponse: Failed to get instanceId")
                            }

//                        update shared Prefs lastDonation
                        updateLastDonation()
                        Cst.fastToast(this@PatientActivity, getString(R.string.donation_notification_title))
                        startActivity(Intent(this@PatientActivity, HomeActivity::class.java))
                    }
                }
            })
    }

    private fun updateLastDonation(){
        ApiClient.getAppuserServices().getLastDonation(Cst.token)
            .enqueue(object: Callback<Date>{
                override fun onFailure(call: Call<Date>, t: Throwable) {
                    Log.e(TAG, "updateLastDonation: onFailure: ${t.message}")
                }

                override fun onResponse(call: Call<Date>, response: Response<Date>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "updateLastDonation: onResponse: LastDonation update not Successful, " +
                                "Code: ${response.code()} " + ": ${response.body()}")
                    }
                    else{
                        val date = response.body()
                        Log.d(TAG, "updateLastDonation: onResponse: LastDonation update Successful: ${date}")
                        Cst.currentUser!!.lastDonation = "${date!!.date}/${date!!.month+1}/20${date!!.year%100}"
                    }

                }
            })
    }

    private fun sendDonationNotification(deviceToken : String){
        val title = getString(R.string.donation_notification_title)
        val body = getString(R.string.donation_notification_body_1) + " " + post!!.patientName + ".\n" +
                getString(R.string.donation_notification_body_2)
        val notification = Notification(title, body, post.city!!)
        notification.token = deviceToken
        ApiClient.getNotificationServices().addNotificationToToken(notification, Cst.token)
            .enqueue(object : Callback<Notification>{
                override fun onFailure(call: Call<Notification>, t: Throwable) {
                    Log.e(TAG, "createNotificationFromPost-register: onFailure: ${t.message}")
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

    private fun checkIfUserHasDonated(){
        if(Cst.hasDonated){
            iDonatedBtn.visibility = View.GONE
            findViewById<TextView>(R.id.iDonated_label).text = getString(R.string.i_donated_label_hasDonated)
        }
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        overridePendingTransition(0,0)
    }
}
