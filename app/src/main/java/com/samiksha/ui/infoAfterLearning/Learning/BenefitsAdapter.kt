package com.samiksha.ui.infoAfterLearning.Learning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R

class BenefitsAdapter() : RecyclerView.Adapter<BenefitsAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: List<String>? = null
    var mListener: BenefitsAdapterInterface? = null

    fun updateAdapter(mList: List<String>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: List<String>, mListener: BenefitsAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface BenefitsAdapterInterface {
        fun onItemSelected( position: Int, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_benefit, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: String = mList!![position]
        holder.txtName.text = model



        holder.itemView.setOnClickListener { mListener!!.onItemSelected(position, model) }




    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)

    }
}