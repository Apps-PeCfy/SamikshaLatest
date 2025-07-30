package com.samiksha.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import java.util.*
import kotlin.collections.ArrayList

class TrainingCalenderAdapter() : RecyclerView.Adapter<TrainingCalenderAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<TrainingCalenderModel>? = null
    var mListener: TrainingCalenderAdapterInterface? = null
    var date : Date? = null

    fun updateAdapter(mList: ArrayList<TrainingCalenderModel>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: ArrayList<TrainingCalenderModel>,  date:Date,mListener: TrainingCalenderAdapterInterface) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
        this.date = date
    }

    interface TrainingCalenderAdapterInterface {
        fun onItemSelected(position: Int, model: TrainingCalenderModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_calender_dates, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: TrainingCalenderModel = mList!![position]

        holder.txtDate.text = model.dayDate
        holder.txtDay.text = model.day

       if (model.selected && model.date?.after(date)!!){
           holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorPrimary)!!)
           holder.txtDate.setTextColor(context?.resources?.getColor(R.color.white)!!)
           holder.txtDay.setTextColor(context?.resources?.getColor(R.color.white)!!)
       }else if(model.date?.after(Date())!!){
           holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorLightGray)!!)
           holder.txtDate.setTextColor(context?.resources?.getColor(R.color.colorGreyText)!!)
           holder.txtDay.setTextColor(context?.resources?.getColor(R.color.colorGreyText)!!)
       }else if(model.date?.before(date)!!){
           holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorLightGray)!!)
           holder.txtDate.setTextColor(context?.resources?.getColor(R.color.colorGreyText)!!)
           holder.txtDay.setTextColor(context?.resources?.getColor(R.color.colorGreyText)!!)
       }else{
            holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.white)!!)
            holder.txtDate.setTextColor(context?.resources?.getColor(R.color.black)!!)
            holder.txtDay.setTextColor(context?.resources?.getColor(R.color.black)!!)
        }



        holder.itemView.setOnClickListener {
            mListener?.onItemSelected(position, model)
        }

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtDay: TextView = itemView.findViewById(R.id.txt_day)
        val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        val crdMain: CardView = itemView.findViewById(R.id.crd_main)
    }
}