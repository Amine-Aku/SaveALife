package com.impression.savealife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.impression.savealife.adapters.HomeAdapter
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    var recyclerView: RecyclerView? = null
    var adapter: HomeAdapter? = null
    var list: List<Post>? = null

    override fun onStart() {
        super.onStart()
        setRecyclerView(emptyList())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = resources.getString(R.string.app_name)
        bottomNavigationInitialize(R.id.nav_home)

//   Add+ Button(FloatingActionButton)
        val addAlert: FloatingActionButton = findViewById(R.id.home_add_alert)
        addAlert.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

//        init
        recyclerView = findViewById(R.id.home_recycler_view)

        retrofitCall()

    }


    private fun retrofitCall(){
        val call = ApiClient.getPostServices().getPosts()
        call.enqueue(object: Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e(TAG, "retrofitCall : onFailure: ${t.message}")
                Toast.makeText(this@HomeActivity, "Failed to Load Posts", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(!response.isSuccessful){
                    Log.d(TAG, "retrofitCall : onResponse : Not successful : Code : " +response.code())
                    Toast.makeText(this@HomeActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                    return
                }
                Log.d(TAG, "retrofitCall : onResponse: Call Successful")
                setRecyclerView(response.body()!!)

//                Toast.makeText(this@HomeActivity, response.body()!!.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setRecyclerView(list: List<Post>){
        val layoutManager = LinearLayoutManager(this@HomeActivity)
        recyclerView!!.layoutManager = layoutManager
        adapter = HomeAdapter(list)

        recyclerView!!.adapter = adapter
        recyclerView!!.post{
            Log.d(TAG, "setRecyclerView: Data Change !")
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun bottomNavigationInitialize(selectedItemId: Int){
        //Initialization
        val bottomnavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //Item selected
        bottomnavigation.selectedItemId = selectedItemId

        //Item Selected Listener
        bottomnavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> return@setOnNavigationItemSelectedListener true

                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false

        }
    }
}
