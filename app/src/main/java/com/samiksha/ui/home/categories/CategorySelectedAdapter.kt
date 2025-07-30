package com.samiksha.ui.home.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.home.main.Homefragment
import com.samiksha.ui.home.pojo.CategoriesResponsePOJO

class CategorySelectedAdapter(
    var context: FragmentActivity?,
   var categortArrayListselected: List<CategoriesResponsePOJO.DataItem>
) : RecyclerView.Adapter<CategorySelectedAdapter.PreferencesViewHolder>()  {

    private var iclickListener: IClickListener? = null
    lateinit var myHolder: PreferencesViewHolder
    var row_index = -1


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PreferencesViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.templete_category_selected, viewGroup, false)
        return PreferencesViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PreferencesViewHolder, position: Int) {
        myHolder = viewHolder
      //  viewHolder.tvName.text = selectedCategortArrayList[position].name

        viewHolder.llPreference.setOnClickListener(View.OnClickListener {
            row_index = position
            notifyDataSetChanged()
        })

        if(row_index==position){

            iclickListener!!.CategorySelected()

        }

    }

    override fun getItemCount(): Int {
        return categortArrayListselected.size
    }

    inner class PreferencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var llPreference: LinearLayout
        var tvName: TextView


        init {
            llPreference = itemView.findViewById<View>(R.id.llPreference) as LinearLayout
            tvName = itemView.findViewById<View>(R.id.tvName) as TextView

        }


        override fun onClick(v: View?) {
            if (v!!.id == R.id.llPreference) {


            }
        }


    }


    fun setClickListener(iclickListener: Homefragment) {
        this.iclickListener = iclickListener
    }


    interface IClickListener {
        fun CategorySelected()

    }

}