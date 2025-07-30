package com.samiksha.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils

class TrainingPathReportAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context? = null
    var mList: ArrayList<TrainingPathModel.DataItem>? = null
    var mAdapter: TrainingDaysAdapter? = null
    var  days:String? = null
    var  userRole:String? = null
    var selectedposition:Int? = null
    private var mPreferencesManager: PreferencesManager? = null




    private val ITEM = 0
    private val LOADING = 1

    constructor(
        days:String?,
        context: Context?,
        mList: ArrayList<TrainingPathModel.DataItem>,
        selectedposition:Int?
    ) : this() {
        this.days = days
        this.context = context
        this.mList = mList
        this.selectedposition = selectedposition
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2 = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val holder = viewHolder as MyViewHolder
                val model: TrainingPathModel.DataItem = mList!![selectedposition!!]

                holder.txtName.text = model.name
                mAdapter = TrainingDaysAdapter(days,context, ProjectUtils.getDaysList(model.days),"Report")
                holder.recyclerView.adapter = mAdapter
                holder.recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            LOADING -> {
            }
        }
    }

    private class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    override fun getItemCount(): Int {
        return  1
    }


    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_report_training_path, parent, false)
        viewHolder = MyViewHolder(v1)
        return viewHolder
    }


    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view)
    }


}