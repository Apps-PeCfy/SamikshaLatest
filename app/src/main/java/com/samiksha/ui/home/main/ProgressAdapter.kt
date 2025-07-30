package com.samiksha.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R

class ProgressAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<TrainingPathModel.DataItem>? = null
    var mListener: ProgressAdapterInterface? = null

    //pagination
    private var isLoadingAdded = false
    private val ITEM = 0
    private val LOADING = 1

    fun updateAdapter(mList: ArrayList<TrainingPathModel.DataItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(
        context: Context?,
        mList: ArrayList<TrainingPathModel.DataItem>,
        mListener: ProgressAdapterInterface?
    ) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface ProgressAdapterInterface {
        fun onItemSelected(
            position: Int,
            model: TrainingPathModel.DataItem,
            s: String
        )
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
                val model: TrainingPathModel.DataItem = mList!![position]

                holder.txtName.text = model.name


                holder.llDailyReport.setOnClickListener {
                    mListener?.onItemSelected(position, model,"txt")
                }

                holder.llViewReport.setOnClickListener {
                    mListener?.onItemSelected(position, model,"img")
                }


            }
            LOADING -> {
            }
        }
    }


    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_progress_report, parent, false)
        viewHolder = MyViewHolder(v1)
        return viewHolder
    }

    private class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val img_next: ImageView = itemView.findViewById(R.id.img_next)
        val llDailyReport: LinearLayout = itemView.findViewById(R.id.llDailyReport)
        val llViewReport: LinearLayout = itemView.findViewById(R.id.llViewReport)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList?.size!! - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addAll(pmList: List<TrainingPathModel.DataItem?>) {
        for (pd in pmList) {
            add(pd)
        }
    }

    fun add(model: TrainingPathModel.DataItem?) {
        mList?.add(model!!)
        notifyItemInserted(mList?.size!! - 1)
    }

    fun remove(model: TrainingPathModel.DataItem?) {
        val position: Int = mList?.indexOf(model)!!
        if (position > -1) {
            mList?.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        mList?.clear()
        notifyDataSetChanged()
    }

    /**
     * ADD AND REMOVE LOADING FOOTER
     **/

    open fun addLoadingFooter(): Unit {
        isLoadingAdded = true
        add(TrainingPathModel().DataItem())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        if (mList?.size!! > 0) {
            val position: Int = mList?.size!! - 1
            val item: TrainingPathModel.DataItem? = getItem(position)
            if (item != null) {
                mList?.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int): TrainingPathModel.DataItem? {
        return mList?.get(position)
    }


}