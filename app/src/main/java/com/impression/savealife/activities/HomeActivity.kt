package com.impression.savealife.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.impression.savealife.dialogs.LogoutDialog
import com.impression.savealife.models.Cst
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
    var count = 0

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
        if(intent != null && intent.hasExtra("patientName")) postExist@{
            val patientName = intent.extras!!.getString("patientName")!!.trim()
            var nameFound = false
            list.forEachIndexed lit@{ index, post ->
                if(post.patientName == patientName){
                    pos = index
                    recyclerView!!.layoutManager = layoutManager
                    scrollTo(layoutManager, pos)
                    Log.d(TAG, "setRecyclerView: Position: $pos")
                    setupAdapter(list,  patientName, pos)
                    Log.d(TAG, "setRecyclerView: setupAdapter Notification clicked & Post found, WAY 1")
                    nameFound = true
                    return@lit
                }
            }
            if(!nameFound){
                recyclerView!!.layoutManager = layoutManager
                setupAdapter(list, null, null)
                Log.i(TAG, "setRecyclerView: setupAdapter: Notification clicked & Post not found, Way 2")
                //  PROBLEM : Always Called in 1 of the Recycler View 2 calls
//                Cst.fastToast(this, "Post not found or deleted")
            }
        }
        else{
            recyclerView!!.layoutManager = layoutManager
            setupAdapter(list, null, null)
            Log.d(TAG, "setRecyclerView: setupAdapter default, WAY 3")
        }
    }


    private fun setupAdapter(list: List<Post>, patientName: String?, position: Int?) {
        adapter?.let {
            adapter!!.setOnItemClickListener(object : HomeAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    Log.d(TAG, "onItemClick: Item Clicked : ${list[pos]}")
                    val intent = Intent(this@HomeActivity, PatientActivity::class.java)
                    intent.putExtra("patient", list[pos])
                    startActivity(intent)
                }

                override fun onHighlighted(header: LinearLayout, name: String, pos: Int) {
                        patientName?.let {
                            Log.d(TAG, "onHighlighted: patientName = $patientName / this position : $pos")
                            if(pos != RecyclerView.NO_POSITION && list[pos].patientName == patientName && isSelected){
                                header.background = resources.getDrawable(R.drawable.bg_article_header_selected, theme)
                                isSelected = false
                                Log.d(TAG, "onHighlighted: $patientName $pos is highlighted")
                            }
                            else isSelected = true
                        }
                }


            })
            recyclerView!!.adapter = adapter
            recyclerView!!.post{
                Log.d(TAG, "setRecyclerView: Data Change !")
                adapter!!.notifyDataSetChanged()
            }
        }
    }

//    Scroll Recycler view to the corresponding Post to the notif clicked (NEED SMOOTH)
    private fun scrollTo(layoutManager: LinearLayoutManager, position: Int){
        layoutManager.scrollToPosition(position)
        // TEST SMOOTH SCROLL
//                    val smoothScroller = object: LinearSmoothScroller(this@HomeActivity){
//                        override fun getVerticalSnapPreference(): Int = SNAP_TO_START;
//                    }
//                    smoothScroller.targetPosition = position
//                    layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun bottomNavigationInitialize(selectedItemId: Int){
        //Initialization
        val bottomnavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //Item selected
        bottomnavigation.selectedItemId = selectedItemId

        //Item Selected Listener
        bottomnavigation.setOnNavigationItemSelectedListener {
            if(Cst.authenticated || it.itemId == R.id.nav_home){
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
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        // EXIT APP or put in background
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(Cst.authenticated) menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout_icon -> {
                Log.d(TAG, "onOptionsItemSelected: Opening Logout Dialog")
                LogoutDialog().show(supportFragmentManager, "logout alert dialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
