package com.impression.savealife.models

object Constants {
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


}
