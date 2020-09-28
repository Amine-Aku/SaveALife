package com.impression.savealife.models

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

//object that contains all the constants used in this App
object Cst {

    val USER_ID = 1

    val CHANNEL_1_ID = "New Post Notification"

    var isSelected = false

    val BLOOD_TYPE_LIST = arrayListOf(
        "None", "O+","O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
    )

    val CITY_LIST = listOf<City>(
        City("Marrakech", 31.669746, -7.973328),
        City("Casablanca", 33.5731, -7.5898),
        City("Agadir", 30.4278, -9.5981)
    )

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






}
