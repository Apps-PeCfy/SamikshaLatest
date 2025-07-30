package com.samiksha.ui.infoAfterLearning.SportInformation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO

class SportsInformationAdapter() : RecyclerView.Adapter<SportsInformationAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>? = null
    var mListener: SportsInformationAdapterInterface? = null

    fun updateAdapter(mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>, mListener: SportsInformationAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface SportsInformationAdapterInterface {
        fun onItemSelected( position: Int, model: SkillDetailsResponsePOJO.SubSkillAnswersModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sub_skill_answer, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: SkillDetailsResponsePOJO.SubSkillAnswersModel = mList!![position]
        holder.txtName.text = model.answer


        holder.txtName.setOnClickListener { mListener!!.onItemSelected(position, model) }

        if (model.checked){
            holder.txtName.background = context?.resources?.getDrawable(R.drawable.shape_rounded_white)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.colorPrimary)!!)
        }else{
            holder.txtName.background = context?.resources?.getDrawable(R.drawable.rounded_stroke_white_border)
            holder.txtName.setTextColor(context?.resources?.getColor(R.color.white)!!)
        }


    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: Button = itemView.findViewById(R.id.txt_name)
    }
}