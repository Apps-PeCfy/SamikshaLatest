package com.samiksha.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils

class TrainingPathAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<TrainingPathModel.DataItem>? = null
    var mListener: TrainingPathAdapterInterface? = null
    var mAdapter: TrainingDaysAdapter? = null
    var  days:String? = null
    var  userRole:String? = null

    private var mPreferencesManager: PreferencesManager? = null


    //pagination
    private var isLoadingAdded = false
    private val ITEM = 0
    private val LOADING = 1

    fun updateAdapter(days:String?,mList: ArrayList<TrainingPathModel.DataItem>?) {
        this.mList = mList
        this.days = days
        notifyDataSetChanged()
    }

    constructor(
        days:String?,
        context: Context?,
        mList: ArrayList<TrainingPathModel.DataItem>,
        mListener: TrainingPathAdapterInterface?
    ) : this() {
        this.days = days
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface TrainingPathAdapterInterface {
        fun onItemSelected(position: Int, model: TrainingPathModel.DataItem)
        fun onRepeatClick(position: Int, model: TrainingPathModel.DataItem)
        fun onDeleteClick(position: Int, model: TrainingPathModel.DataItem)
        fun onReportClick(position: Int, model: TrainingPathModel.DataItem)
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


                PreferencesManager.initializeInstance(context!!)
                mPreferencesManager = PreferencesManager.instance

                userRole = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)

                if(userRole.equals("MasterUser")){
                    holder.imgDelete.visibility = View.VISIBLE
                    holder.tvTapHere.visibility = View.VISIBLE
                }else{
                    holder.imgDelete.visibility = View.GONE
                    holder.tvTapHere.visibility = View.GONE

                }


                if (model.time == "Morning") {
                    holder.img_timing.setBackgroundResource(R.drawable.iv_morning)
                } else if (model.time == "Afternoon") {
                    holder.img_timing.setBackgroundResource(R.drawable.iv_afternoon)

                } else {
                    holder.img_timing.setBackgroundResource(R.drawable.iv_evening)

                }


                holder.itemView.setOnClickListener {
                    mListener?.onItemSelected(position, model)
                }

                holder.txtRepeat.setOnClickListener {
                    mListener?.onRepeatClick(position, model)
                }

                holder.imgDelete.setOnClickListener {
                    mListener?.onDeleteClick(position, model)
                }

                holder.llReport.setOnClickListener {
                    mListener?.onReportClick(position, model)
                }

                holder.txtRepeat.visibility = View.GONE

                mAdapter = TrainingDaysAdapter(days,context, ProjectUtils.getDaysList(model.days),"TrainingPath")
                holder.recyclerView.adapter = mAdapter
                holder.recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
        val v1 = inflater.inflate(R.layout.item_training_path, parent, false)
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
        val tvTapHere: TextView = itemView.findViewById(R.id.tvTapHere)
        val txtRepeat: TextView = itemView.findViewById(R.id.txt_repeat)
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        val img_timing: ImageView = itemView.findViewById(R.id.img_timing)
        val llReport: LinearLayout = itemView.findViewById(R.id.llReport)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view)
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