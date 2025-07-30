package com.samiksha.ui.home.dealingWithDistraction.subscription

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R

class SubscriptionAdapter(
    var context: Context
) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {

    private var iclickListener: IClickListener? = null
    lateinit var myHolder: SubscriptionViewHolder
    var row_index = 0


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SubscriptionViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.templete_subscription, viewGroup, false)
        return SubscriptionViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: SubscriptionViewHolder, position: Int) {
        myHolder = viewHolder

        when (position) {
            1 -> {

                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
                viewHolder.tvPrice.setTextColor(R.color.black)
                viewHolder.tvPrice.text = "₹1,500"
                viewHolder.tvMonth.text = "for 3 months"
                viewHolder.tvHeading.text = "Our best trail package"
                viewHolder.tvHeading.setTextColor(R.color.black)

            }
            2 -> {
                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
                viewHolder.tvPrice.setTextColor(R.color.black)
                viewHolder.tvPrice.text = "₹5,500"
                viewHolder.tvMonth.text = "for 6 months"
                viewHolder.tvHeading.text = "Our best trail package"
                viewHolder.tvHeading.setTextColor(R.color.black)


            }
            else -> {
                viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription)

            }
        }

        viewHolder.llSubscription.setOnClickListener(View.OnClickListener {
            row_index = position
            notifyDataSetChanged()
            iclickListener!!.click()
        })

        if(row_index==position){

            viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription)
            viewHolder.tvPrice.setTextColor(Color.WHITE)
            viewHolder.tvHeading.setTextColor(Color.parseColor("#FC6D2D"))

        }
        else
        {
            viewHolder.llSubscription.setBackgroundResource(R.drawable.rounded_corner_subscription_white)
            viewHolder.tvPrice.setTextColor(Color.BLACK)
            viewHolder.tvHeading.setTextColor(Color.BLACK)

        }







    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        var llSubscription: LinearLayout
        var tvPrice: TextView
        var tvHeading: TextView
        var tvMonth: TextView


        init {
            llSubscription = itemView.findViewById<View>(R.id.llSubscription) as LinearLayout
            tvPrice = itemView.findViewById<View>(R.id.tvPrice) as TextView
            tvMonth = itemView.findViewById<View>(R.id.tvMonth) as TextView
            tvHeading = itemView.findViewById<View>(R.id.tvHeading) as TextView


        }

        override fun onClick(p0: View?) {
            iclickListener!!.click()
        }


    }


    fun setClickListener(iclickListener: SubscriptionActivity) {
        this.iclickListener = iclickListener
    }


    interface IClickListener {
        fun click()

    }

}