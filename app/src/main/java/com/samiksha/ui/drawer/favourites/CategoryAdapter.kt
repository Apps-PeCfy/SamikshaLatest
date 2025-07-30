package com.samiksha.ui.drawer.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: List<AllCategoriesResponsePOJO.DataItem>? = null
    var mListener: CategoryAdapterInterface? = null

    fun updateAdapter(mList: List<AllCategoriesResponsePOJO.DataItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: List<AllCategoriesResponsePOJO.DataItem>, mListener: CategoryAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface CategoryAdapterInterface {
        fun onItemSelected( position: Int, model: AllCategoriesResponsePOJO.DataItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: AllCategoriesResponsePOJO.DataItem = mList!![position]
        holder.txtName.text = model.name

        Glide.with(context!!)
            .load(model.image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.imgIcon)


        holder.itemView.setOnClickListener { mListener!!.onItemSelected(position, model) }

        if (model.checked){
            holder.llCropView.setBackgroundResource(R.drawable.shape_roundedbtn)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
        }else{
            holder.llCropView.setBackgroundResource(R.drawable.shape_rounded_white)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.black)!!)
        }
        
        


    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.icon_name)
        val imgIcon: ImageView = itemView.findViewById(R.id.icon)
        val llCropView: LinearLayout = itemView.findViewById(R.id.llCropView)
    }
}