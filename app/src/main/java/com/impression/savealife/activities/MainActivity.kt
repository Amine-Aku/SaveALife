package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.messaging.FirebaseMessaging
import com.impression.savealife.R
import com.impression.savealife.models.Cst

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.app_name)

        Cst.subscribeToTopic("all_users")


    }

    fun GoHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun GoWelcome(view: View) {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }


}
