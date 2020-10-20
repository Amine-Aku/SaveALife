package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.messaging.FirebaseMessaging
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Cst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        title = resources.getString(R.string.app_name)

        Cst.subscribeToTopic("all_users")

        ApiClient.getAuthenticationService().welcome()
            .enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "onFailure: : ${t.message}")
                    Cst.fastToast(this@MainActivity, "Connection Failed")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "onResponse: Connection not Successful : ${response.code()}: ${response.body()}")
                    }
                    else{
                        Log.d(TAG, "onResponse: Connection Successful : ${response.body()}")
                        if(Cst.loadData(this@MainActivity)){
                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        }
                        else {
                            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                        }
                    }
                }
            })

// Load data from shared preferences and test if authenticated


    }

    fun GoHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun GoWelcome(view: View) {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }


}
