package com.impression.savealife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NavUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        title = resources.getString(R.string.notifications)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        bottomNavigationInitialize(R.id.nav_notifications)

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

                R.id.nav_notifications -> return@setOnNavigationItemSelectedListener true

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }
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
