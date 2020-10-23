package com.impression.savealife.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.impression.savealife.R
import com.impression.savealife.models.Donation
import com.impression.savealife.models.Notification
import java.text.SimpleDateFormat

class HistoryAdapter(private val donations: List<Donation>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  donations.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donation = donations[position]
        holder.setData(donation, position)
//        notifyItemChanged(holder.adapterPosition)
//        notifyItemInserted(position)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var dateField: TextView? = null
        var patientNameField: TextView? = null
        var bodyField: TextView? = null

        init {
            dateField = itemView.findViewById(R.id.history_donation_date)
            patientNameField = itemView.findViewById(R.id.history_patientName)
            bodyField = itemView.findViewById(R.id.history_details)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    listener.onItemClick(pos)
                }
            }
        }



        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(donation: Donation, pos: Int) {
            donation?.let{
                val str1 = it.date!!.substringBeforeLast('/')
                val str2 = it.date!!.substringAfterLast('/')
                dateField!!.text = "$str1\n$str2"
                patientNameField!!.text = donation.patientPost!!.patientName
                bodyField!!.text = donation.patientPost!!.city
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
