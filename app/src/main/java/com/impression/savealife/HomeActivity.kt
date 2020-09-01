package com.impression.savealife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.impression.savealife.adapters.HomeAdapter
import com.impression.savealife.api.PostsAPI
import com.impression.savealife.models.ListPosts
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var adapter: HomeAdapter? = null

    override fun onStart() {
        super.onStart()
        recyclerView!!.layoutManager = LinearLayoutManager(this@HomeActivity)
        adapter = HomeAdapter(emptyList<Post>())
        recyclerView!!.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = resources.getString(R.string.app_name)
        bottomNavigationInitialize(R.id.nav_home)

//   Add Button
        val addAlert: FloatingActionButton = findViewById(R.id.home_add_alert)
        addAlert.setOnClickListener {
            startActivity(Intent(this, NewPostActivity::class.java))
        }

//        init
        recyclerView = findViewById(R.id.home_recycler_view)

//        var list: List<Post>? = ListPosts.list
//        recyclerView!!.layoutManager = LinearLayoutManager(this)
//        recyclerView!!.adapter = HomeAdapter(this, list!!)



//        Fetch the posts
        val retrofit = Retrofit.Builder()
            .baseUrl("https://save-a-life-web-server.herokuapp.com/home/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val postsAPI = retrofit.create(PostsAPI::class.java)
        val call = postsAPI.getPosts()
        call.enqueue(object: Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(!response.isSuccessful){
                    Toast.makeText(this@HomeActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                    return
                }

                recyclerView!!.layoutManager = LinearLayoutManager(this@HomeActivity)
                adapter = HomeAdapter(response.body()!!)
                recyclerView!!.adapter = adapter
//                recyclerView!!.adapter = HomeAdapter(this@HomeActivity, response.body()!!)
//                Toast.makeText(this@HomeActivity, response.body()!!.toString(), Toast.LENGTH_LONG).show()
            }

        })


//        RecyclerView



    }

    fun bottomNavigationInitialize(selectedItemId: Int){
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
