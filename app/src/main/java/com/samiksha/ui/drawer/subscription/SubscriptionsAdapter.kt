package com.samiksha.ui.drawer.subscription

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.commonMethod.UpdateProfile
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel

class SubscriptionsAdapter(
    var context: Context,
    var subscriptionsList: List<SubscriptionModel.Subscriptions>
) : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionViewHolder>() {

    private var iclickListener: SubscriptionClick? = null
    lateinit var myHolder: SubscriptionViewHolder
    var row_index = 0
    var isActive = false


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SubscriptionViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.subscription_templete, viewGroup, false)
        return SubscriptionViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: SubscriptionViewHolder, position: Int) {
        myHolder = viewHolder

        /*when (position) {
            1 -> {

                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
                viewHolder.tvMonth.text = "Unlock all modules for 3 months"
                viewHolder.tvHeading.text = "3 Months - Rs.1,999"
                viewHolder.tvHeading.setTextColor(R.color.black)

            }
            2 -> {
                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
                viewHolder.tvMonth.text = "Unlock all modules for 12 months"
                viewHolder.tvHeading.text = "12 Months - Rs.4,999"
                viewHolder.tvHeading.setTextColor(R.color.black)


            }
            else -> {
                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription)

            }
        }*/


        viewHolder.tvHeading.text =
            subscriptionsList.get(position).name + " - ₹ " + subscriptionsList.get(position).sellingPrice
        viewHolder.tvMonth.text = subscriptionsList.get(position).description







        isActive = false
        for (i in subscriptionsList.indices) {
            if (subscriptionsList[i].is_active) {
                isActive = true
            }
        }


        viewHolder.llSubscription.setOnClickListener(View.OnClickListener {
            row_index = position

            if(isActive){
               // UpdateProfile.showToast(context)
                Toast.makeText(context,R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
           //     Toast.makeText(context,R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
            }
            notifyDataSetChanged()
        })





        if (subscriptionsList.get(position).is_active) {

            viewHolder.subscriptionIsActive.visibility = View.VISIBLE
            viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription)
            viewHolder.tvHeading.setTextColor(Color.parseColor("#FFFFFF"))
            viewHolder.tvMonth.setTextColor(Color.parseColor("#FFFFFF"))


        } else {
            if (row_index == position && !isActive) {

                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription)
                viewHolder.tvHeading.setTextColor(Color.parseColor("#FFFFFF"))
                viewHolder.tvMonth.setTextColor(Color.parseColor("#FFFFFF"))

                iclickListener!!.click(subscriptionsList.get(position).id.toString(), position)


            } else {
                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
                viewHolder.tvHeading.setTextColor(Color.BLACK)
                viewHolder.tvMonth.setTextColor(Color.BLACK)

            }

        }


    }

    override fun getItemCount(): Int {
        return subscriptionsList.size
    }

    inner class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var llSubscription: LinearLayout
        var tvHeading: TextView
        var tvMonth: TextView
        var subscriptionIsActive: ImageView


        init {
            llSubscription = itemView.findViewById<View>(R.id.llSubscription) as LinearLayout
            tvMonth = itemView.findViewById<View>(R.id.tvMonth) as TextView
            tvHeading = itemView.findViewById<View>(R.id.tvHeading) as TextView
            subscriptionIsActive =
                itemView.findViewById<View>(R.id.subscriptionIsActive) as ImageView


        }

        override fun onClick(p0: View?) {
            iclickListener!!.click(subscriptionsList.get(position).id.toString(), position)
        }


    }


    fun setClickListener(iclickListener: Subscription) {
        this.iclickListener = iclickListener
    }


    interface SubscriptionClick {
        fun click(toString: String, position: Int)

    }

}