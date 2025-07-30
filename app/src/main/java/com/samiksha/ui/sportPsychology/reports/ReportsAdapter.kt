package com.samiksha.ui.sportPsychology.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentReportResponsePOJO

class ReportsAdapter(
    var context: FragmentActivity?,
   var assesmentList: List<AssesmentReportResponsePOJO.DataItem>?
) :
    RecyclerView.Adapter<ReportsAdapter.MyViewHolder>() {

    private var iClickListener: IClickListener? = null




    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.templete_reports, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return assesmentList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvUserName.text = assesmentList?.get(position)!!.name

        holder.mainLayout.setOnClickListener(View.OnClickListener {




            iClickListener!!.memberClick(assesmentList!![position].id)


        })

    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)

    }

    fun setClickListener(iClickListener: ReportsFragment?) {
        this.iClickListener = iClickListener
    }



    interface IClickListener {
        fun memberClick(position: Int?)

    }



}