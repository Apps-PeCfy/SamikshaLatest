package com.samiksha.ui.home.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.home.pojo.TestimonialsResponsePOJO

class TestomonialAdapter(
    var context: FragmentActivity,
    var testoMonialsList: List<TestimonialsResponsePOJO.TestimonialsItem>
) : RecyclerView.Adapter<TestomonialAdapter.TestomonialViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TestomonialViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.templete_testomonials, viewGroup, false)
        return TestomonialViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: TestomonialViewHolder, position: Int) {
        viewHolder.tvUserName.text = testoMonialsList[position].by
        viewHolder.tvMessage.text = testoMonialsList[position].review
        viewHolder.tvSport.text = testoMonialsList[position].sport!!.name



    }

    override fun getItemCount(): Int {
        return testoMonialsList.size
    }

    inner class TestomonialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvUserName: TextView
        var tvMessage: TextView
        var tvSport: TextView


        init {
            tvUserName = itemView.findViewById<View>(R.id.tvUserName) as TextView
            tvMessage = itemView.findViewById<View>(R.id.tvMessage) as TextView
            tvSport = itemView.findViewById<View>(R.id.tvSport) as TextView


        }




    }




}