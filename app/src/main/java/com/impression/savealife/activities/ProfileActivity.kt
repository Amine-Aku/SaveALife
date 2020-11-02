package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.dialogs.EditDialog
import com.impression.savealife.dialogs.LogoutDialog
import com.impression.savealife.models.Cst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ProfileActivity : AppCompatActivity(), EditDialog.EditDialogClickListener{

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        Log.d(TAG, "onCreate: Current User : ${Cst.currentUser}")

//        Initialization
        init()

//        Events
        findViewById<Button>(R.id.profile_edit_btn).setOnClickListener {
            Log.d(TAG, "onClick: Edit Button Clicked")
            EditDialog().show(supportFragmentManager, "Profile Edit Dialog")
        }

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
//            Log.d(TAG, "init: ${Cst.currentUser}")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout_icon -> {
                Log.d(TAG, "onOptionsItemSelected: Opening Logout Dialog")
                LogoutDialog(this).show(supportFragmentManager, "logout alert dialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogEditClick(city: String, bloodType: String) {
        val newData = mapOf(
            Pair("city", city),
            Pair("bloodType", bloodType)
        )
        val call = ApiClient.getAppuserServices().updateUser(newData, Cst.token)
        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "onDialogEditClick: onFailure: ${t.message}")
                Cst.fastToast(this@ProfileActivity, "Update Failed")
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(!response.isSuccessful){
                    Log.d(TAG, "onDialogEditClick: onResponse: Update not Successful ${response.code()} : ${response.body()}")
                    Cst.fastToast(this@ProfileActivity, "Update not Successful")
                    return
                }
                else{
                    Cst.fastToast(this@ProfileActivity, "User Updated Successfully")
                    val msg = response.body()
                    Log.d(TAG, "onDialogEditClick:  User Updated Successfully : $msg")
                    cityField.text = resources.getString(R.string.city) + " " + city
                    bloodTypeField.text = resources.getString(R.string.blood_type) + " " + bloodType

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Cst.currentUser!!.city!!)
                    FirebaseMessaging.getInstance().subscribeToTopic(city)
                    Cst.currentUser!!.city = city
                    Cst.currentUser!!.bloodType = bloodType

                    Cst.saveData(this@ProfileActivity)
                }
            }
        })
    }
}
