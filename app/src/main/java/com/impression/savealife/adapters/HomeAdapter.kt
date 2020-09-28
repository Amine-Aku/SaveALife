package com.impression.savealife.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.impression.savealife.R
import com.impression.savealife.models.Constants
import com.impression.savealife.models.Place
import com.impression.savealife.models.Post
import java.time.format.DateTimeFormatter

class HomeAdapter(private val posts: List<Post>, var selectedPosition: Int? = null)
    : RecyclerView.Adapter<HomeAdapter.PostHolder>() {

    private val TAG = "HomeAdapter"
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_posts, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        Log.d(TAG, "onBindViewHolder: Pos = $position")
        holder.setData(post, position)
        holder.selectedItem(position)


//        notifyItemChanged(holder.adapterPosition)
//        notifyItemInserted(position)
    }


    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameField: TextView? = null
        var dateField: TextView? = null
        var bodyField: TextView? = null
        var header: LinearLayout? = null

        init {
            nameField = itemView.findViewById(R.id.home_post_name)
            dateField = itemView.findViewById(R.id.home_post_date)
            bodyField = itemView.findViewById(R.id.home_post_body)
            header = itemView.findViewById(R.id.layout_header)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pos)
                }
            }

        }

        fun selectedItem(position: Int) {
            Log.d(TAG, "selectedItem: Launched")
            listener.onHighlighted(header!!, nameField!!.text.toString(), position)
            Log.d(TAG, "selectedItem: Listener activatd : pos = $position")
        }


    @RequiresApi(Build.VERSION_CODES.O)
        fun setData(post: Post, pos: Int) {
            post?.let {
                nameField!!.text = post.patientName
                dateField!!.text = post.date!!.toString()
                bodyField!!.text = "City : " + post.city + "\n" +
                        "Donation Center : " + getDonationCenterPlaceName(post.donationCenter) + "\n" +
                        "Blood Type : " + post.bloodType
                    }
        }

        private fun getDonationCenterPlaceName(donationCenter: Place?): String? = if(donationCenter != null) donationCenter!!.placeName ; else ""
    }

    interface OnItemClickListener{
        fun onItemClick(pos: Int)
        fun onHighlighted(header: LinearLayout, name: String, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
