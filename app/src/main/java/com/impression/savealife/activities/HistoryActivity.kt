package com.impression.savealife.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impression.savealife.R
import com.impression.savealife.adapters.HistoryAdapter
import com.impression.savealife.adapters.NotificationsAdapter
import com.impression.savealife.api.ApiClient
import com.impression.savealife.models.Cst
import com.impression.savealife.models.Donation
import com.impression.savealife.models.Notification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private val TAG = "HistoryActivity"
    private val call = ApiClient.getDonationServices().getDonations(Cst.token)

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter


    override fun onStart() {
        super.onStart()
        setRecyclerView(emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        title = resources.getString(R.string.history)

        recyclerView = findViewById(R.id.history_recycler_view)
        retrofitCall()
    }

    private fun retrofitCall(){
        call.enqueue(object: Callback<List<Donation>> {
            override fun onFailure(call: Call<List<Donation>>, t: Throwable) {
                Log.e(TAG, "retrofitCall : onFailure: ${t.message}")
                Toast.makeText(this@HistoryActivity, "Failed to Load Donations", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<Donation>>, response: Response<List<Donation>>) {
                if(!response.isSuccessful){
                    Log.d(TAG, "retrofitCall : onResponse : Not successful : Code : ${response.code()} : ${response.body()}")
                    Toast.makeText(this@HistoryActivity, "Code : "+response.code(), Toast.LENGTH_LONG).show()
                    return
                }
                Log.d(TAG, "retrofitCall : onResponse: Call Successful")
                setRecyclerView(response.body()!!)
            }
        })
    }

    private fun setRecyclerView(list: List<Donation>){
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        adapter = HistoryAdapter(list)
        adapter!!.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                val donation = list[pos]
                Log.d(TAG, "onItemClick: Item Clicked : $donation")
                Cst.fastToast(this@HistoryActivity, "You clicked ${donation.patientPost!!.patientName}")
            }
        })
        recyclerView!!.adapter = adapter
        recyclerView!!.post{
            Log.d(TAG, "setRecyclerView: Data Change !")
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
    }


}
