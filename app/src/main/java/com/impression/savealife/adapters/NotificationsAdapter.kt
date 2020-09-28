package com.impression.savealife.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.impression.savealife.R
import com.impression.savealife.models.Notification

class NotificationsAdapter(private val posts: List<Notification>)
    : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>(){

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  posts.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.setData(post, position)
//        notifyItemChanged(holder.adapterPosition)
//        notifyItemInserted(position)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var titleField: TextView? = null
        var dateField: TextView? = null
        var bodyField: TextView? = null

        init {
            titleField = itemView.findViewById(R.id.notification_title)
            dateField = itemView.findViewById(R.id.notification_date)
            bodyField = itemView.findViewById(R.id.notification_body)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    listener.onItemClick(pos)
                }
            }
        }



        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(notification: Notification, pos: Int) {
            notification?.let{
                titleField!!.text = notification.title
                dateField!!.text = notification.date
                bodyField!!.text = notification.body
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
