package com.impression.savealife.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NavUtils
import com.impression.savealife.R
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Appuser
import com.impression.savealife.models.Cst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "RegisterActivity"

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPWField: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var bloodTypeSpinner: Spinner
    private lateinit var hasDonatedSwitch: Switch
    private lateinit var lastDonationPicker: DatePicker
    private lateinit var lastDonationLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        title = resources.getString(R.string.register)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        init()
    }

    private fun init() {
        usernameField = findViewById(R.id.register_username)
        passwordField = findViewById(R.id.register_password)
        confirmPWField = findViewById(R.id.register_confirm)
        citySpinner = findViewById(R.id.register_city_spinner)
        bloodTypeSpinner = findViewById(R.id.register_bloodType_spinner)
        hasDonatedSwitch = findViewById(R.id.register_hasDonated_switch)
        lastDonationPicker = findViewById(R.id.register_lastDonation_picker)

        lastDonationLayout = findViewById(R.id.register_layout_lastDonation)
        lastDonationLayout.visibility = View.GONE

        hasDonatedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) lastDonationLayout.visibility = View.VISIBLE
            else lastDonationLayout.visibility = View.GONE
        }

        val now = Date()
        lastDonationPicker.updateDate(2000+(now.year%100), now.month, now.date)

        setSpinner(citySpinner, Cst.CITY_NAMES_LIST())
        setSpinner(bloodTypeSpinner, Cst.BLOOD_TYPE_LIST)

        findViewById<Button>(R.id.register_register_btn).setOnClickListener {
            register()
        }
    }

    private fun register(){
        createUserFromFields()?.let {
            Log.d(TAG, "register: $it")
            val call = ApiClient.getAppuserServices().registerUser(it)
            call.enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "register: onFailure: ${t.message}")
                    Cst.fastToast(this@RegisterActivity, "Register Failed")
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful){
                        Log.d(TAG, "register: onResponse: Register not Successful ${response.code()}")
                        Cst.fastToast(this@RegisterActivity, "Register not Successful")
                        return
                    }
                    else{
                        Cst.fastToast(this@RegisterActivity, "User Registered")
                        val msg = response.body()
                        Log.d(TAG, "onResponse:  User Registered Successfully : $msg")
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                }
            })
        }
    }


    private fun createUserFromFields(): Appuser?{
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val confirmPW = confirmPWField.text.toString().trim()
        val city = citySpinner.selectedItem.toString()
        val bloodType = bloodTypeSpinner.selectedItem.toString()

        if(username == "") usernameField.error = "Empty Field"
        if(password == "") passwordField.error = "Empty Field"
        if(password != confirmPW) passwordField.error = "Passwords not matching"

        return if(username == "" || password != confirmPW || password == confirmPW) {
            val newUser = Appuser(username, password, city, bloodType)
            if(hasDonatedSwitch.isChecked){
                newUser.hasDonated = hasDonatedSwitch.isChecked
                var date = Date()
                date.month = lastDonationPicker.month
                date.date = lastDonationPicker.dayOfMonth
                val s = SimpleDateFormat("dd/MM/yyyy")
                newUser.lastDonation = s.format(date)
            }
            Log.d(TAG, "createUserFromFields: New User: ${newUser}")
            newUser
        }
        else {
            Cst.fastToast(this, "Invalid Fields")
            null
        }
    }

    private fun setSpinner(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(0) // default selected item 0
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected: Nothing is selected in the spinner")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSelected = parent!!.getItemAtPosition(position).toString()
        Log.d(TAG, "onItemSelected: Item ($itemSelected) is selected in the spinner")
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }

}
