package com.samiksha.ui.progressReport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider
import com.samiksha.R

class ProgressReportAdapter() : RecyclerView.Adapter<ProgressReportAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: List<ProgressReportModel.DataItem>? = null
    var mListener: ProgressReportAdapterInterface? = null

    fun updateAdapter(mList: List<ProgressReportModel.DataItem>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(
        context: Context?,
        mList: List<ProgressReportModel.DataItem>,
        mListener: ProgressReportAdapterInterface?
    ) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface ProgressReportAdapterInterface {
        fun onItemSelected(position: Int, model: ProgressReportModel.DataItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_progress_report_summary, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: ProgressReportModel.DataItem = mList!![position]


        if (mList!![position].rank.equals("1")) {
            holder.tvScore.text = "Learning Score"
            holder.txtTitle.text = "Progress Quiz Score"
            holder.txtMessage.text = model.message

        }

        if (mList!![position].rank.equals("2")) {
            holder.tvScore.text = "Training Score"
            holder.txtTitle.text = "Effectiveness of Mental Training"
            holder.txtMessage.text =
                "this score represents the extent to which your mental exercise is helping your mind."

        }
        if (mList!![position].rank.equals("3")) {
            holder.tvScore.visibility = View.GONE
            holder.txtTitle.text = "Confidence to Perform"
            holder.txtMessage.text =
                "this score represents the extent to which the mental exercise is helping you feel confident to perform."

        }






        if (model.type.equals("Learning")) {
            holder.txtScore.text = model.average + "/10"
            holder.imgIcon.visibility = View.INVISIBLE
            holder.slider.progress = model.average?.toInt()!!
        } else {
            holder.txtScore.text = model.average + "/5"
            if (model.icon != null) {
                Glide.with(context!!)
                    .load(model.icon)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform()
                    ).into(holder.imgIcon!!)
                holder.imgIcon.visibility = View.VISIBLE
                holder.slider.progress = 2 * model.average?.toInt()!!
            }
        }


        holder.itemView.setOnClickListener { mListener!!.onItemSelected(position, model) }


    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        val txtScore: TextView = itemView.findViewById(R.id.txt_score)
        val txtMessage: TextView = itemView.findViewById(R.id.txt_message)
        val imgIcon: ImageView = itemView.findViewById(R.id.img_emoji)
        val slider: LinearProgressIndicator = itemView.findViewById(R.id.slider)
    }
}