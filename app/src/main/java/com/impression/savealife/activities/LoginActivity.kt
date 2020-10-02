package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.gson.JsonObject
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Cst
import com.impression.savealife.services.AuthenticationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = resources.getString(R.string.login)

        init()
        showHidePassword()
    }

    private fun showHidePassword() {

    }

    private fun init(){
        usernameField = findViewById(R.id.login_username)
        passwordField = findViewById(R.id.login_password)
        findViewById<Button>(R.id.login_login_btn).setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            if(username == "" || password == "") {
                if(username == "") usernameField.error = "Empty Field"
                if(password == "") passwordField.error = "Empty Field"
                Cst.fastToast(this, "Empty username or password")
            }
            else login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        val request = AuthenticationRequest(username, password)
        val call = ApiClient.getAuthenticationService().authenticate(request)
        call.enqueue(object: Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG, "login : onFailure: ${t.message}")
                Cst.fastToast(this@LoginActivity, "Login Failed")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(!response.isSuccessful){
                    Log.i(TAG, "onResponse: Login not successful : $response , Code : ${response.code()}")
                    Cst.fastToast(this@LoginActivity, "Invalid Username or Password")
                    passwordField.text.clear()
                    return
                }
                else{
                    val jwt = response.body()!!.get("jwt").toString().removeSurrounding("\"")
                    Cst.login(jwt)
                    Log.d(TAG, "onResponse: Login Successful: Token = $jwt")
                    Cst.fastToast(this@LoginActivity, "Welcome")
                    usernameField.text.clear()
                    passwordField.text.clear()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                }
            }
        })

    }

}
