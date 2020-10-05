package com.impression.savealife.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.impression.savealife.activities.HomeActivity
import com.impression.savealife.activities.LoginActivity
import java.util.*
import kotlin.collections.ArrayList

//object that contains all the constants used in this App
object Cst {

//    FIELDS
    private val TAG = "Cst"
    val CHANNEL_1_ID = "New Post Notification"

    val SHARED_PREFS = "SAL-shared preferences"
    val USER_ID = "user_id"
    val USERNAME = "username"
    val CITY = "city"
    val BLOOD_TYPE = "bloodType"
    val LAST_DONATION = "last_donation"
    val ACTIVE = "isActive"
    val AUTHENTICATED = "authenticated"
    val JWT = "jwt"

//    VARs
    var isSelected = false

    var token: String? = null

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
    fun unsubscribeFromTopic(topic: String) = FirebaseMessaging.getInstance().subscribeToTopic(topic)


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
    }

    fun logout(context: Context){
        unsubscribeFromTopic(currentUser!!.city!!)
        token = null
        currentUser = null
        authenticated = false
        clearData(context)
        fastToast(context, "You have logged out Successfully")
        Log.d(TAG, "logout: Logged out successfully")
    }

    fun saveData(context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
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
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
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

    private fun clearData(context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }








}
