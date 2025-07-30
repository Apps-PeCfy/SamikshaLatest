package com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel

class SubscriptionsAdapter() : RecyclerView.Adapter<SubscriptionsAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<SubscriptionModel.Subscriptions>? = null
    var mListener: SubscriptionsAdapterInterface? = null

    fun updateAdapter(mList: ArrayList<SubscriptionModel.Subscriptions>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: ArrayList<SubscriptionModel.Subscriptions>, mListener: SubscriptionsAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface SubscriptionsAdapterInterface {
        fun onItemSelected( position: Int, model: SubscriptionModel.Subscriptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_subscriptions, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: SubscriptionModel.Subscriptions = mList!![position]
        holder.txtName.text = model.name
        holder.txtPrice.text = "₹${model.sellingPrice}"
        holder.txtDuration.text = "for ${model.planDuration} months"


        holder.itemView.setOnClickListener { mListener!!.onItemSelected(position, model) }

        if (model.selected){
            holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorPrimary)!!)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
            holder.txtPrice.setTextColor(context?.resources?.getColor(R.color.white)!!)
        }else{
            holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.white)!!)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.black)!!)
            holder.txtPrice.setTextColor(context?.resources?.getColor(R.color.black)!!)
        }


    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val txtPrice: TextView = itemView.findViewById(R.id.txt_price)
        val txtDuration: TextView = itemView.findViewById(R.id.txt_duration)
        val crdMain: CardView = itemView.findViewById(R.id.crd_main)
    }
}