package com.samiksha.ui.home.categories

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.home.main.Homefragment
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.utils.ProjectUtils

class CategortAdapter(
    var context: FragmentActivity,
    var croparrayList: List<AllCategoriesResponsePOJO.DataItem>
) : RecyclerView.Adapter<CategortAdapter.CategoryViewHolder>() {

    private var iclickListener: IClickListener? = null
    lateinit var myHolder: CategoryViewHolder
    var row_index = 0

    var isClick = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.category_item_list, viewGroup, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: CategoryViewHolder, position: Int) {
        myHolder = viewHolder


        if (position >= 0) {

            viewHolder.llCropView.setBackgroundResource(R.drawable.shape_roundedbtn)
            viewHolder.iconName.setTextColor(Color.parseColor("#FFFFFF"))

            viewHolder.icon.visibility = View.VISIBLE

            if(croparrayList[position].name.equals("Yoga")){
                viewHolder.iconName.text = "Meditation"
            }else
            {
                viewHolder.iconName.text = croparrayList[position].name
            }
           Glide.with(context!!)
                .load(croparrayList?.get(position)?.image)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                ).into(viewHolder.icon!!)



        } else {
            viewHolder.icon.visibility = View.GONE
            viewHolder.iconName.text = croparrayList[position].name
            Glide.with(context!!)
                .load(croparrayList?.get(position)?.image)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                ).into(viewHolder.icon!!)


        }

        viewHolder.llCropView.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                isClick = true
                row_index = position
                notifyDataSetChanged()

                iclickListener!!.categoryClick(croparrayList[position].value, position)
            }else{
                ProjectUtils.showToast(
                    context,
                   "No network available, please check your WiFi or Data connection"
                )

            }


        })



        if (row_index == position) {

            viewHolder.llCropView.setBackgroundResource(R.drawable.shape_roundedbtn)
            viewHolder.iconName.setTextColor(Color.parseColor("#FFFFFF"))

        } else {
            viewHolder.llCropView.setBackgroundResource(R.drawable.shape_rounded_white)
            viewHolder.iconName.setTextColor(Color.parseColor("#000000"))
        }





    }

    override fun getItemCount(): Int {
        return croparrayList.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var icon: ImageView
        var iconName: TextView
        var llCropView: LinearLayout


        init {
            icon = itemView.findViewById<View>(R.id.icon) as ImageView
            iconName = itemView.findViewById<View>(R.id.icon_name) as TextView
            llCropView = itemView.findViewById<View>(R.id.llCropView) as LinearLayout


        }

        override fun onClick(v: View?) {
            if (v!!.id == R.id.icon) {


            }

        }
    }


    fun setClickListener(iclickListener: Homefragment) {
        this.iclickListener = iclickListener
    }


    interface IClickListener {
        fun categoryClick(value: String?, pos: Int)

    }

}