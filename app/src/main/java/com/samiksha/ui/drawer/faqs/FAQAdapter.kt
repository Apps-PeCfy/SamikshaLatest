package com.samiksha.ui.drawer.faqs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.samiksha.R
import com.samiksha.ui.drawer.faqs.POJO.FAQResponsePOJO

class FAQAdapter(
    var context: FragmentActivity?,
    var faqList: List<FAQResponsePOJO.DataItem>
) :
    RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_faqs, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: FAQAdapter.MyViewHolder, position: Int) {
        holder.tvQuestion!!.text = faqList[position].question
        holder.tvSubQuestion!!.text = faqList[position].answer



        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.stackFromEnd = true
        mLayoutManager.reverseLayout = true
        holder.recyclerView!!.setLayoutManager(mLayoutManager)



        holder.llmyaccount!!.setOnClickListener(View.OnClickListener {

           if (selectedPosition == position) {
                selectedPosition = -1
            } else {
                selectedPosition = position
            }
            notifyDataSetChanged()



        })


        if (selectedPosition == -1) {
            holder.tvSubQuestion!!.setVisibility(View.GONE)
            holder.ivExpand!!.setImageResource(R.drawable.down_arrow)

        } else if (selectedPosition == position) {
            holder.tvSubQuestion!!.setVisibility(View.VISIBLE)
            holder.ivExpand!!.setImageResource(R.drawable.up_arrow)

        } else {
            holder.tvSubQuestion!!.setVisibility(View.GONE)
            holder.ivExpand!!.setImageResource(R.drawable.down_arrow)

        }

    }



    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {



        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val tvSubQuestion: TextView = itemView.findViewById(R.id.tvSubQuestion)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        val llmyaccount: RelativeLayout = itemView.findViewById(R.id.llmyaccount)


    }


}