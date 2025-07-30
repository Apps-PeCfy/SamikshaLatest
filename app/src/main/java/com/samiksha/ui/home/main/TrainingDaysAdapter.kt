package com.samiksha.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R

class TrainingDaysAdapter() : RecyclerView.Adapter<TrainingDaysAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<TrainingPathModel.ScheduleDays>? = null
    var  days:String? = null
    var  type:String? = null


    fun updateAdapter(mList: ArrayList<TrainingPathModel.ScheduleDays>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(days:String?,context: Context?, mList: ArrayList<TrainingPathModel.ScheduleDays>,type:String?) : this() {
        this.context = context
        this.mList = mList
        this.days = days
        this.type = type
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule_days, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: TrainingPathModel.ScheduleDays = mList!![position]

        if(type.equals("TrainingPath")) {
            holder.crdPoint.visibility = View.GONE
            holder.txtName.text = model.flag
            if (model.is_complete && model.name.equals(days)) {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.login_button)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
            } else if (model.is_complete) {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorYellow)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.black)!!)
            } else {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorLightGray)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.colorGray)!!)
            }
        }else{
            holder.crdPoint.visibility = View.VISIBLE

            holder.txtName.text = model.flag
            if (model.is_complete && model.name.equals(days)) {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.login_button)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
                holder.txtpoint.text = "10"
                holder.txtpoint.setTextColor(context?.resources?.getColor(R.color.black)!!)
            } else if (model.is_complete) {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorYellow)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
                holder.txtpoint.text = "10"
                holder.txtpoint.setTextColor(context?.resources?.getColor(R.color.black)!!)

            } else {
                holder.crdMain.setCardBackgroundColor(context?.resources?.getColor(R.color.colorLightGray)!!)
                holder.txtName.setTextColor(context?.resources?.getColor(R.color.colorGray)!!)
            }
        }

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val txtpoint: TextView = itemView.findViewById(R.id.txtpoint)
        val crdMain: CardView = itemView.findViewById(R.id.crd_main)
        val crdPoint: CardView = itemView.findViewById(R.id.crdPoint)
    }
}