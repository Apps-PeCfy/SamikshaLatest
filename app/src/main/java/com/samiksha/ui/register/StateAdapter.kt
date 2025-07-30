package com.samiksha.ui.register

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.home.main.Homefragment
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO

class StateAdapter(
    var context: FragmentActivity,
    var croparrayList: List<AllCategoriesResponsePOJO.DataItem>
) : RecyclerView.Adapter<StateAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.category_item_list, viewGroup, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: CategoryViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return croparrayList.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {



        init {


        }

        override fun onClick(v: View?) {
            if (v!!.id == R.id.icon) {


            }

        }
    }



}