package com.samiksha.ui.infoAfterLearning.trainingFeedback

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.utils.ConnectionDetector
import com.samiksha.utils.Dataset
import com.samiksha.utils.ProjectUtils

class TrainingFeedbackAdapter() : RecyclerView.Adapter<TrainingFeedbackAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>? = null
    var mListener: TrainingFeedbackAdapterInterface? = null

    fun updateAdapter(mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    constructor(context: Context?, mList: List<SkillDetailsResponsePOJO.SubSkillAnswersModel>, mListener: TrainingFeedbackAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
    }

    interface TrainingFeedbackAdapterInterface {
        fun onItemSelected( position: Int, model: SkillDetailsResponsePOJO.SubSkillAnswersModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sub_skill_emogi, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: SkillDetailsResponsePOJO.SubSkillAnswersModel = mList!![position]


        holder.rlMain.setOnClickListener { mListener!!.onItemSelected(position, model) }

        if (model.checked){
            holder.rlMain.background = context?.resources?.getDrawable(R.drawable.shape_rounded_white)
        }else{
            holder.rlMain.background = context?.resources?.getDrawable(R.drawable.rounded_stroke_blue_border)
        }

        if(ProjectUtils.checkInternetAvailable(context) == true){
            Glide.with(context!!)
                .load(model.icon)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                ).into(holder.imgEmoji!!)
        }else{
            holder.imgEmoji.setImageResource(Dataset.getMenuImage(model.weightage))
        }




    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val rlMain: RelativeLayout = itemView.findViewById(R.id.rl_main)
        val imgEmoji: ImageView = itemView.findViewById(R.id.img_emoji)
    }
}