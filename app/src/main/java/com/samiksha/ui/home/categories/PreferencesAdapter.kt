package com.samiksha.ui.home.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.samiksha.R
import com.samiksha.ui.home.main.Homefragment
import com.samiksha.ui.home.pojo.CategoriesResponsePOJO

class PreferencesAdapter(
    var context: FragmentActivity?,
    var preferenceCategortArrayList: List<CategoriesResponsePOJO.DataItem>
) : RecyclerView.Adapter<PreferencesAdapter.PreferencesViewHolder>() {

    private var iclickListener: IClickListener? = null
    lateinit var myHolder: PreferencesViewHolder
    var row_index = -1


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PreferencesViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.preferences_item_list, viewGroup, false)
        return PreferencesViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PreferencesViewHolder, position: Int) {
        val model = preferenceCategortArrayList[position]
        viewHolder.tvName.text = model.name
       /* if(model.name!!.length>30){
            viewHolder.tvName.layoutParams.height = 60

        }
       */ Glide.with(context!!)
            .load(model.image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(viewHolder.iv_Module)

        if (model.type.equals("Paid") && model.isSubscription || model.isAccessible) {
            viewHolder.iclock.visibility = View.VISIBLE
            viewHolder.iclock.setBackgroundResource(R.drawable.unlock)
            viewHolder.tvFreeOrPaid.visibility = View.GONE

        } else if (model.type.equals("Free") || model.isSubscription) {
            viewHolder.tvFreeOrPaid.text = model.type
            viewHolder.iclock.visibility = View.GONE
            viewHolder.tvFreeOrPaid.visibility = View.VISIBLE


        } else if (model.type.equals("Coming soon")) {
            viewHolder.tvFreeOrPaid.text = model.type
            viewHolder.iclock.visibility = View.GONE
            viewHolder.tvFreeOrPaid.visibility = View.VISIBLE

        } else {
            viewHolder.iclock.visibility = View.VISIBLE
            viewHolder.iclock.setBackgroundResource(R.drawable.lock)

            viewHolder.tvFreeOrPaid.visibility = View.GONE
        }

        if ((model.moduleType.equals("Basic Skill")) ||
            model.moduleType.equals("Yoga")
        ) {
            if (model.moduleType.equals("Basic Skill")){
                viewHolder.tvModuleType.text = "Core Skills"

            }else{
                viewHolder.tvModuleType.text = "Meditation"

            }

        } else {

            if (model.gaols!!.size > 0) {
                viewHolder.tvModuleType.text = model.gaols!![0]

            }

        }

        if (model.isFavorite) {
            viewHolder.imgFavorites.setImageResource(R.drawable.ic_favorite_selected)
        } else {
            viewHolder.imgFavorites.setImageResource(R.drawable.ic_favorite_normal)
        }


        // viewHolder.tvTime.text = model.time

        viewHolder.ratingBar.rating = (model.reviews!!).toFloat()


        if (model.time == "Morning") {
            viewHolder.ivTime.setBackgroundResource(R.drawable.iv_morning)
        } else if (model.time == "Afternoon") {
            viewHolder.ivTime.setBackgroundResource(R.drawable.iv_afternoon)

        } else {
            viewHolder.ivTime.setBackgroundResource(R.drawable.iv_evening)

        }




        viewHolder.imgFavorites.setOnClickListener {
            iclickListener!!.onFavoriteClick(model)
        }

        myHolder = viewHolder

        viewHolder.rlPreference.setOnClickListener(View.OnClickListener {
            row_index = position
            notifyDataSetChanged()
        })

        if (row_index == position) {

            iclickListener!!.clickMoreSkill(
                model
            )

        }

    }

    override fun getItemCount(): Int {
        return preferenceCategortArrayList.size
    }

    inner class PreferencesViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {

        var llPreference: LinearLayout
        var rlPreference: RelativeLayout
        var tvFreeOrPaid: TextView
        var tvName: TextView
        var tvModuleType: TextView
        var tvTime: TextView
        var iv_Module: ImageView
        var ivTime: ImageView
        var ratingBar: SimpleRatingBar
        var imgFavorites: ImageView
        var iclock: ImageView


        init {
            llPreference = itemView.findViewById<View>(R.id.llPreference) as LinearLayout
            rlPreference = itemView.findViewById<View>(R.id.rlPreference) as RelativeLayout
            tvFreeOrPaid = itemView.findViewById<View>(R.id.tvFreeOrPaid) as TextView
            tvName = itemView.findViewById<View>(R.id.tvName) as TextView
            tvModuleType = itemView.findViewById<View>(R.id.tvModuleType) as TextView
            tvTime = itemView.findViewById<View>(R.id.tvTime) as TextView
            iv_Module = itemView.findViewById<View>(R.id.iv_Module) as ImageView
            ivTime = itemView.findViewById<View>(R.id.ivTime) as ImageView
            ratingBar = itemView.findViewById<View>(R.id.ratingBar) as SimpleRatingBar
            imgFavorites = itemView.findViewById<View>(R.id.img_favorites) as ImageView
            iclock = itemView.findViewById<View>(R.id.iclock) as ImageView


        }


        override fun onClick(v: View?) {
            if (v!!.id == R.id.rlPreference) {


            }
        }


    }


    fun setClickListener(iclickListener: Homefragment) {
        this.iclickListener = iclickListener
    }


    interface IClickListener {
        fun clickMoreSkill(model: CategoriesResponsePOJO.DataItem)
        fun onFavoriteClick(model: CategoriesResponsePOJO.DataItem)
    }

}