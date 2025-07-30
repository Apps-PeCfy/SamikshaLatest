package com.samiksha.ui.drawer.mytraining

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.samiksha.R
import com.samiksha.utils.ProjectUtils

class MyTrainingAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<MyTrainingModel.DataItem>? = null
    var mListener: MyTrainingAdapterInterface? = null

    //pagination
    private var isLoadingAdded = false
    private val ITEM = 0
    private val LOADING = 1

    fun updateAdapter(mList: ArrayList<MyTrainingModel.DataItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: ArrayList<MyTrainingModel.DataItem>, mListener: MyTrainingAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface MyTrainingAdapterInterface {
        fun onItemSelected(position: Int, model: MyTrainingModel.DataItem)
        fun onFavoriteClick(position: Int, model: MyTrainingModel.DataItem)
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
                val model: MyTrainingModel.DataItem = mList!![position]
                holder.txtName.text = model.name
                holder.txtType.text = model.type
                if((model.moduleType.equals("Basic Skill"))||
                    model.moduleType.equals("Yoga"))
                {
                    holder.txtModuleType.text = model.moduleType

                }else{
                    if( model.gaols!= null && model.gaols?.size !! >0){
                        holder.txtModuleType.text = model.gaols!![0]

                    }
                }
                holder.ratingBar.rating = (model.reviews!!).toFloat()

                if (model.time == "Morning") {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_morning)
                } else if (model.time == "Afternoon") {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_afternoon)

                } else {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_evening)

                }

                holder.txtDate.text = ProjectUtils.changeDateFormat(model.created_at, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM yyyy")

                holder.imgFavorites.setOnClickListener {
                    mListener?.onFavoriteClick(position, model)
                }

                if (model.isFavorite){
                    holder.imgFavorites.setImageResource(R.drawable.ic_favorite_selected)
                }else{
                    holder.imgFavorites.setImageResource(R.drawable.ic_favorite_normal)
                }

                holder.itemView.setOnClickListener {
                    mListener?.onItemSelected(position, model)
                }
            }
            LOADING -> {
            }
        }
    }


    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_my_training, parent, false)
        viewHolder = MyViewHolder(v1)
        return viewHolder
    }

    private class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.tvName)
        val txtType: TextView = itemView.findViewById(R.id.tvFreeOrPaid)
        val txtModuleType: TextView = itemView.findViewById(R.id.tvModuleType)
        val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        val imgModule: ImageView = itemView.findViewById(R.id.iv_Module)
        val imgTime: ImageView = itemView.findViewById(R.id.ivTime)
        val imgFavorites: ImageView = itemView.findViewById(R.id.img_favorites)
        val ratingBar: SimpleRatingBar = itemView.findViewById(R.id.ratingBar)
        
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList?.size!! - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addAll(pmList: List<MyTrainingModel.DataItem?>) {
        for (pd in pmList) {
            add(pd)
        }
    }

    fun add(model: MyTrainingModel.DataItem?) {
        mList?.add(model!!)
        notifyItemInserted(mList?.size!! - 1)
    }

    fun remove(model: MyTrainingModel.DataItem?) {
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
        add(MyTrainingModel().DataItem())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        if (mList?.size!! > 0) {
            val position: Int = mList?.size!! - 1
            val item: MyTrainingModel.DataItem ?= getItem(position)
            if (item != null) {
                mList?.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int): MyTrainingModel.DataItem? {
        return mList?.get(position)
    }
}