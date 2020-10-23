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
import com.impression.savealife.api.PrivateAPIs
import com.impression.savealife.models.Cst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        title = resources.getString(R.string.app_name)

        Cst.subscribeToTopic("all_users")

        var connected: Boolean = false
        var counter = 1

        welcomeTest(counter)
// Load data from shared preferences and test if authenticated


    }

    private fun welcomeTest(counter: Int = 1){
        ApiClient.getAuthenticationService().welcome()
            .enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    if(counter != 3 ) {
                        Log.i(TAG, "welcomeTest : onFailure: Test N°$counter : ${t.message}")
                        welcomeTest(counter + 1)
                    }
                    else{
                        Log.i(TAG, "welcomeTest : onFailure: Test N°$counter : ${t.message} : Connection Failed !")
                        Cst.fastToast(this@MainActivity, "Connection Failed")
                    }
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "onResponse: Connection not Successful : ${response.code()}: ${response.body()}")
                    }
                    else{
                        Log.d(TAG, "onResponse: Connection Successful : ${response.body()}")
                        redirect()
                    }
                }
            })
    }

    private fun pingTest(): Boolean{
        return try {
            val inAddress = InetAddress.getByName(PrivateAPIs.SERVER_BASE_URL)
            !inAddress.equals("")
        } catch (e: Exception){
            Log.e(TAG, "pingTest: Exception : ${e.message}")
            false
        }
    }

    private fun redirect(){
        if(Cst.loadData(this@MainActivity)){
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
        else {
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    fun GoHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun GoWelcome(view: View) {
        startActivity(Intent(this, WelcomeActivity::class.java))
    }


}
