package com.impression.savealife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = resources.getString(R.string.app_name)

    }

    fun GoHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun GoWelcome(view: View) {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }


}
