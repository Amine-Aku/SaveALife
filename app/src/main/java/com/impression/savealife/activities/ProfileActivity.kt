package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NavUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.impression.savealife.R
import com.impression.savealife.models.Cst

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"
    private lateinit var usernameField: TextView
    private lateinit var cityField: TextView
    private lateinit var bloodTypeField: TextView
    private lateinit var lastDonationField: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        title = resources.getString(R.string.profile)
        bottomNavigationInitialize(R.id.nav_profile)

        Log.d(TAG, "onCreate: Current User : ${Cst.currentUser}")

//        Initialization
        init()

//        Events
        findViewById<Button>(R.id.profile_edit_btn).setOnClickListener {}

        findViewById<Button>(R.id.profile_history_btn).setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }


    }

    private fun init(){
        usernameField = findViewById(R.id.profile_username)
        cityField = findViewById(R.id.profile_city)
        bloodTypeField = findViewById(R.id.profile_bloodType)
        lastDonationField = findViewById(R.id.profile_lastDonation)

        Cst.currentUser?.let {
            usernameField.text = it.username
            cityField.text = resources.getString(R.string.city) + " " + it.city
            bloodTypeField.text = resources.getString(R.string.blood_type) + " " + it.bloodType
            lastDonationField.text = resources.getString(R.string.last_donation) + " " + it.lastDonation
        }
    }


    fun bottomNavigationInitialize(selectedItemId: Int){
        //Initialization
        val bottomnavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //Item selected
        bottomnavigation.selectedItemId = selectedItemId

        //Item Selected Listener
        bottomnavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_profile -> return@setOnNavigationItemSelectedListener true
            }
            return@setOnNavigationItemSelectedListener false

        }
    }

    override fun onBackPressed() {
        // Go back to the Parent Activity
        NavUtils.navigateUpFromSameTask(this)
        overridePendingTransition(0,0)
    }
}
