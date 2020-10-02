package com.impression.savealife.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.impression.savealife.R
import com.impression.savealife.adapters.NotificationsAdapter
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Cst
import com.impression.savealife.models.Notification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private val TAG = "NotificationsActivity"
    var recyclerView: RecyclerView? = null
    var adapter: NotificationsAdapter? = null
    var list: List<Notification>? = null

    private val call: Call<List<Notification>> = ApiClient.getNotificationServices().getNotifications(Cst.currentUser!!.id!!, Cst.token)

    override fun onStart() {
        super.onStart()
        setRecyclerView(emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        title = resources.getString(R.string.notifications)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        bottomNavigationInitialize(R.id.nav_notifications)

        //        init
        recyclerView = findViewById(R.id.notifications_recycler_view)

        retrofitCall()

    }

    private fun retrofitCall(){
        call.enqueue(object: Callback<List<Notification>> {
            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                Log.e(TAG, "retrofitCall : onFailure: ${t.message}")
                Toast.makeText(this@NotificationsActivity, "Failed to Load Notifications", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if(!response.isSuccessful){
                    Log.d(TAG, "retrofitCall : onResponse : Not successful : Code : " +response.code())
                    Toast.makeText(this@NotificationsActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                    return
                }
                Log.d(TAG, "retrofitCall : onResponse: Call Successful")
                setRecyclerView(response.body()!!)
            }

        })
    }

    private fun setRecyclerView(list: List<Notification>){
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        adapter = NotificationsAdapter(list)
        adapter!!.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                Log.d(TAG, "onItemClick: Item Clicked : ${list[pos]}")
                val notif = list[pos]
//                Cst.fastToast(this@NotificationsActivity, "You clicked ${notif.title}")
                val patientName = notif.body.substringBefore(getString(R.string.notification_delimiter))
                val intent = Intent(this@NotificationsActivity, HomeActivity::class.java)
                intent.putExtra("patientName", patientName)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        })
        recyclerView!!.adapter = adapter
        recyclerView!!.post{
            Log.d(TAG, "setRecyclerView: Data Change !")
            adapter!!.notifyDataSetChanged()
        }
    }


    // Make it one function in Constants object
    fun bottomNavigationInitialize(selectedItemId: Int){
        //Initialization
        val bottomnavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //Item selected
        bottomnavigation.selectedItemId = selectedItemId

        //Item Selected Listener
        bottomnavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_notifications -> return@setOnNavigationItemSelectedListener true

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false

        }
    }

    override fun onBackPressed() {
        // Go back to the Parent Activity
        NavUtils.navigateUpFromSameTask(this)
        overridePendingTransition(0,0)
    }
}
