package com.impression.savealife.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.impression.savealife.activities.HomeActivity
import com.impression.savealife.activities.LoginActivity
import com.impression.savealife.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

//object that contains all the constants used in this App
object Cst {

//    FIELDS
    private val TAG = "Cst"
    val CHANNEL_1_ID = "New Post Notification"

    val USER_PREFS = "SAL-user preferences"
    val DEVICE_TOKEN_PREFS = "SAL-device token preferences"
    val USER_ID = "user_id"
    val USERNAME = "username"
    val CITY = "city"
    val BLOOD_TYPE = "bloodType"
    val LAST_DONATION = "last_donation"
    val ACTIVE = "isActive"
    val AUTHENTICATED = "authenticated"
    val JWT = "jwt"
    val DEVICE_TOKEN = "device_token"

//    VARs
    var isSelected = false

    var token: String? = null

    var deviceToken: String? = null

    var currentUser: Appuser? = null
        private set

    var authenticated: Boolean = false
        private set

//    LISTS

    val BLOOD_TYPE_LIST = arrayListOf(
        "None", "O+","O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
    )

    val CITY_LIST = listOf<City>(
        City("Marrakech", 31.669746, -7.973328),
        City("Casablanca", 33.5731, -7.5898),
        City("Agadir", 30.4278, -9.5981)
    )

//    FUNCTIONS

    fun  CITY_NAMES_LIST(): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        CITY_LIST.forEach {
            list.add(it.name)
        }
        return list
    }

    fun fastToast(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    fun subscribeToTopic(topic: String): Task<Void> = FirebaseMessaging.getInstance().subscribeToTopic(topic)
    fun unsubscribeFromTopic(topic: String): Task<Void> = FirebaseMessaging.getInstance().subscribeToTopic(topic)


    fun login(jwt: String, context: Context){
        token = "Bearer $jwt"
        val payload = jwt!!.split(".")[1]
        val decodedBytes = Base64.decode(payload, 0)
        val json = String(decodedBytes)
        val map = Gson().fromJson<Map<String, Objects>>(json, Map::class.java)
        currentUser = Appuser(map)
        Log.d(TAG, "Cst login: currentUser : $currentUser")
        Log.d(TAG, "Cst login: TOKEN : $token")
        authenticated = true
        subscribeToTopic(currentUser!!.city!!)
        saveData(context)
//        updateDeviceToken(loadDeviceToken(context))
        //TESTING
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener {
                updateDeviceToken(it.token)
                Log.d(TAG, "login: device_token: ${it.token}")
            }
            .addOnFailureListener {
                updateDeviceToken("none")
            }
    }

    fun logout(context: Context){
        unsubscribeFromTopic(currentUser!!.city!!)
        updateDeviceToken("none")
        token = null
        currentUser = null
        authenticated = false
        clearData(context, USER_PREFS)
        fastToast(context, "You have logged out Successfully")
        Log.d(TAG, "logout: Logged out successfully")
    }

    fun saveData(context: Context){
        val sharedPreferences = context.getSharedPreferences(USER_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putLong(USER_ID, currentUser!!.id!!)
        editor.putString(USERNAME, currentUser!!.username)
        editor.putString(CITY, currentUser!!.city)
        editor.putString(BLOOD_TYPE, currentUser!!.bloodType)
        editor.putString(LAST_DONATION, currentUser!!.lastDonation)
        editor.putBoolean(ACTIVE, currentUser!!.active!!)

        editor.putBoolean(AUTHENTICATED, authenticated)
        editor.putString(JWT, token)

        editor.apply()
    }

    fun loadData(context: Context): Boolean{
        val sharedPreferences = context.getSharedPreferences(USER_PREFS, MODE_PRIVATE)
        authenticated = sharedPreferences.getBoolean(AUTHENTICATED, false)

        if(authenticated){
            token = sharedPreferences.getString(JWT, null)
            currentUser = Appuser(
                sharedPreferences.getLong(USER_ID, 0),
                sharedPreferences.getString(USERNAME, ""),
                sharedPreferences.getString(CITY, ""),
                sharedPreferences.getString(BLOOD_TYPE, ""),
                sharedPreferences.getString(LAST_DONATION, null),
                sharedPreferences.getBoolean(ACTIVE, true)
                )
        }

        return authenticated
    }

    private fun clearData(context: Context, sharedPrefs: String){
        val sharedPreferences = context.getSharedPreferences(sharedPrefs, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun updateDeviceToken(deviceToken: String){
        Log.d(TAG, "updateDeviceToken: launch")
        ApiClient.getAppuserServices().updateToken(deviceToken, token)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e(TAG, "updateDeviceToken: onFailure: ${t.message}")
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(!response.isSuccessful){
                            Log.d(TAG, "updateDeviceToken: onResponse: Update not Successful ${response.code()} : ${response.body()}")
                            return
                        }
                        else{
                            val msg = response.body()
                            Log.d(TAG, "updateDeviceToken:  Token Updated Successfully : $msg")
                        }
                    }
                })
    }

    fun saveDeviceToken(context: Context, deviceToken: String?){
        val sharedPreferences = context.getSharedPreferences(DEVICE_TOKEN_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(DEVICE_TOKEN, deviceToken)
        editor.apply()
    }

    private fun loadDeviceToken(context: Context): String{
        val sharedPreferences = context.getSharedPreferences(DEVICE_TOKEN_PREFS, MODE_PRIVATE)
        return sharedPreferences.getString(DEVICE_TOKEN, "none")!!
        }








}
