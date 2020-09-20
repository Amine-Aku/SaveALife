package com.impression.savealife.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.impression.savealife.R
import com.impression.savealife.models.Post
import java.time.format.DateTimeFormatter

class HomeAdapter(private val posts: List<Post>)
    : RecyclerView.Adapter<HomeAdapter.PostHolder>(){

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_posts, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return  posts.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.setData(post, position)
//        notifyItemChanged(holder.adapterPosition)
//        notifyItemInserted(position)
    }


    inner class PostHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var nameField: TextView? = null
        var dateField: TextView? = null
        var bodyField: TextView? = null

        init {
            nameField = itemView.findViewById(R.id.home_post_name)
            dateField = itemView.findViewById(R.id.home_post_date)
            bodyField = itemView.findViewById(R.id.home_post_body)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    listener.onItemClick(pos)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(post: Post, pos: Int) {
            post?.let{
                nameField!!.text = post.patientName
                dateField!!.text = post.date!!.toString()
                bodyField!!.text = "City : " + post.city + "\n" +
                        "Donation Center : " + post.donationCenter!!.placeName + "\n" +
                        "Blood Type : " + post.bloodType
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
