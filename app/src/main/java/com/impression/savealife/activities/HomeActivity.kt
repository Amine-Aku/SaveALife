package com.impression.savealife.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.impression.savealife.*
import com.impression.savealife.adapters.HomeAdapter
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Constants
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    var recyclerView: RecyclerView? = null
    var adapter: HomeAdapter? = null
    var list: List<Post>? = null
    var isSelected = false

    private val call: Call<List<Post>> = ApiClient.getPostServices().getPosts()

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onHighlighted: onStart: EMPTY LIST")
        setRecyclerView(emptyList())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = resources.getString(R.string.app_name)
        bottomNavigationInitialize(R.id.nav_home)

//   Add+ Button(FloatingActionButton)
            findViewById<FloatingActionButton>(R.id.home_add_alert).setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

//        init
        recyclerView = findViewById(R.id.home_recycler_view)

        retrofitCall()




    }


    private fun retrofitCall(){
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
                Log.d(TAG, "onHighlighted: onResponse: TRUE LIST")
                setRecyclerView(response.body()!!)

//                Toast.makeText(this@HomeActivity, response.body()!!.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setRecyclerView(list: List<Post>){
        val layoutManager = LinearLayoutManager(this@HomeActivity)
        adapter = HomeAdapter(list)
        var pos: Int = 0
        if(intent != null && intent.hasExtra("patientName")){
             val patientName = intent.extras!!.getString("patientName")!!.trim()

            list.forEachIndexed lit@{ index, post ->
                if(post.patientName == patientName){
                    pos = index
                    layoutManager.scrollToPosition(pos)
                    Log.d(TAG, "setRecyclerView: Position: $pos")
                    setupAdapter(list, layoutManager, patientName, pos)
                    Log.d(TAG, "setRecyclerView: setupAdapter WAY : 1")
                    return@lit
                }
            }
        }
        else{
            setupAdapter(list, layoutManager, null, null)
            Log.d(TAG, "setRecyclerView: setupAdapter WAY : 3")
        }



    }

    private fun setupAdapter(list: List<Post>, layoutManager: LinearLayoutManager, patientName: String?, position: Int?) {
        recyclerView!!.layoutManager = layoutManager
        adapter?.let {
            adapter!!.setOnItemClickListener(object : HomeAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    Log.d(TAG, "onItemClick: Item Clicked : ${list[pos]}")
                    val intent = Intent(this@HomeActivity, PatientActivity::class.java)
                    intent.putExtra("patient", list[pos])
                    startActivity(intent)
                }

                override fun onHighlighted(header: LinearLayout, name: String, pos: Int) {
                    if(list.isNotEmpty()){
                        patientName?.let {
                            Log.d(TAG, "onHighlighted: patientName = $patientName / this position : $pos")
                            if(pos != RecyclerView.NO_POSITION && list[pos].patientName == patientName && !isSelected){
                                header.background = resources.getDrawable(R.drawable.bg_article_header_selected, theme)
                                isSelected = true
                                Log.d(TAG, "onHighlighted: $patientName $pos is highlighted")
                            }

                        }
                    }
                    else Log.d(TAG, "onHighlighted: Empty List")
                }


            })
            recyclerView!!.adapter = adapter
            recyclerView!!.post{
                Log.d(TAG, "setRecyclerView: Data Change !")
                adapter!!.notifyDataSetChanged()
            }
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
