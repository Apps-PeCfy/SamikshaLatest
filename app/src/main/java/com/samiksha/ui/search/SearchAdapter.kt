package com.samiksha.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.samiksha.R

class SearchAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<SearchModel.DataItem>? = null
    var mListener: SearchAdapterInterface? = null

    //pagination
    private var isLoadingAdded = false
    private val ITEM = 0
    private val LOADING = 1

    fun updateAdapter(mList: ArrayList<SearchModel.DataItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: ArrayList<SearchModel.DataItem>, mListener: SearchAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface SearchAdapterInterface {
        fun onItemSelected(position: Int, model: SearchModel.DataItem)
        fun onFavoriteClick(position: Int, model: SearchModel.DataItem)
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
                val model: SearchModel.DataItem = mList!![position]
                holder.txtName.text = model.name
                holder.txtType.text = model.type
                if((model.moduleType.equals("Basic Skill"))||
                    model.moduleType.equals("Yoga"))
                {

                    if (model.moduleType.equals("Basic Skill")){
                        holder.txtModuleType.text = "Core Skills"

                    }else{
                        holder.txtModuleType.text = "Meditation"

                    }


                }else{
                    if(model.gaols!!.size>0){
                        holder.txtModuleType.text = model.gaols!![0]

                    }
                }

                Glide.with(context!!)
                    .load(mList!![position].image)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform()
                    ).into(holder.imgModule)


                holder.ratingBar.rating = (model.reviews!!).toFloat()

                if (model.time == "Morning") {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_morning)
                } else if (model.time == "Afternoon") {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_afternoon)

                } else {
                    holder.imgTime.setBackgroundResource(R.drawable.iv_evening)

                }

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
        val v1 = inflater.inflate(R.layout.item_favorites, parent, false)
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
        val txtTime: TextView = itemView.findViewById(R.id.tvTime)
        val imgModule: ImageView = itemView.findViewById(R.id.iv_Module)
        val imgTime: ImageView = itemView.findViewById(R.id.ivTime)
        val imgFavorites: ImageView = itemView.findViewById(R.id.img_favorites)
        val ratingBar: SimpleRatingBar = itemView.findViewById(R.id.ratingBar)
        
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList?.size!! - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addAll(pmList: List<SearchModel.DataItem?>) {
        for (pd in pmList) {
            add(pd)
        }
    }

    fun add(model: SearchModel.DataItem?) {
        mList?.add(model!!)
        notifyItemInserted(mList?.size!! - 1)
    }

    fun remove(model: SearchModel.DataItem?) {
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
        add(SearchModel().DataItem())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        if (mList?.size!! > 0) {
            val position: Int = mList?.size!! - 1
            val item: SearchModel.DataItem ?= getItem(position)
            if (item != null) {
                mList?.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    private fun getItem(position: Int): SearchModel.DataItem? {
        return mList?.get(position)
    }
}