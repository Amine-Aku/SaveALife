package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.messaging.FirebaseMessaging
import com.impression.savealife.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.app_name)

//        FirebaseMessaging.getInstance().subscribeToTopic("Marrakech")
//        FirebaseMessaging.getInstance().unsubscribeFromTopic("Marrakech")

    }

    fun GoHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun GoWelcome(view: View) {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }


}
