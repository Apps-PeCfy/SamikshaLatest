package com.samiksha.ui.drawer.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.drawer.notification.pojo.NotificationResponsePOJO
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    var context: FragmentActivity?,
    var notificationList: List<NotificationResponsePOJO.DataItem>?
) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_notification, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return notificationList!!.size
    }

    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {

        holder.tvNotification.text = notificationList?.get(position)?.message
        holder.tvNotificationType.text = notificationList?.get(position)?.type

        if (notificationList?.get(position)?.createdAt!!.isNotEmpty()) {
            holder.tvNotificationDate.visibility=View.VISIBLE

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormat = SimpleDateFormat("dd-MMM-yy, hh:mma")
            var date: Date? = null
            date = inputFormat.parse(notificationList?.get(position)?.createdAt)
            var formattedDate: String = outputFormat.format(date)
            formattedDate = if(formattedDate.contains("pm")){
                formattedDate.replace("pm", "PM")
            }else{
                formattedDate.replace("am", "AM")

            }
            holder.tvNotificationDate.text = formattedDate
        }
    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNotificationDate: TextView = itemView.findViewById(R.id.tvNotificationDate)
        val tvNotification: TextView = itemView.findViewById(R.id.tvNotification)
        val tvNotificationType: TextView = itemView.findViewById(R.id.tvNotificationType)

    }


}