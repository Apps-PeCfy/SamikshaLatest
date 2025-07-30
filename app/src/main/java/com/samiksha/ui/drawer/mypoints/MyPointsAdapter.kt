package com.samiksha.ui.drawer.mypoints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.samiksha.R
import com.samiksha.ui.drawer.mypoints.pojo.MyRewardsResponsePOJO

class MyPointsAdapter(
    var context: FragmentActivity?,
    var rewardList: List<MyRewardsResponsePOJO.DataItem>?
) :
    RecyclerView.Adapter<MyPointsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_my_points, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return rewardList!!.size-1
    }

    override fun onBindViewHolder(holder: MyPointsAdapter.MyViewHolder, position: Int) {
        if (rewardList!!.get(position).key != "Total rewards") {
            holder.rewardType!!.text = rewardList?.get(position)!!.key
            holder.tvRewardAmt!!.text = rewardList?.get(position)!!.points.toString()
        }


    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val rewardType: TextView = itemView.findViewById(R.id.rewardType)
        val tvRewardAmt: TextView = itemView.findViewById(R.id.tvRewardAmt)

    }


}