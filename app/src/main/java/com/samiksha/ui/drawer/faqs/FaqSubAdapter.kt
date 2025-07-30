package com.samiksha.ui.drawer.faqs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.samiksha.R

class FaqSubAdapter(
    var context: FragmentActivity?,
    var faqAnswer:String?
) :
    RecyclerView.Adapter<FaqSubAdapter.MyViewHolder>() {
    var selectedPosition = -1


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_faq_sub, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvQuestion!!.text = faqAnswer

        holder!!.tvQuestion!!.setOnClickListener(View.OnClickListener {
            if (selectedPosition == position) {
                selectedPosition = -1
            } else {
                selectedPosition = position
            }
            notifyDataSetChanged()
        })




    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        /*@JvmField
        @BindView(R.id.tvQuestion)
        var tvQuestion: TextView? = null

        @JvmField
        @BindView(R.id.tvSubQuestion)
        var tvSubQuestion: TextView? = null
*/

        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val tvSubQuestion: TextView = itemView.findViewById(R.id.tvSubQuestion)
        init {
            ButterKnife.bind(this, itemView)

        }

        override fun onClick(v: View?) {

        }


    }


}