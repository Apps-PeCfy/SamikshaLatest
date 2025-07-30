package com.samiksha.ui.informationScreen.chooseProfession

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
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO

class ChooseProfessionAdapter(
    var context: FragmentActivity?,
    var questionList: List<QuestionResponsePOJO.OptionsItem>?
) :
    RecyclerView.Adapter<ChooseProfessionAdapter.MyViewHolder>() {
    var row_index = -1
    private var iclickListener: IClickListenerChooseProfession? = null



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.templetechoosesport, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return questionList!!.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ChooseProfessionAdapter.MyViewHolder, position: Int) {
        holder.tvTitle.text = questionList?.get(position)?.name
        Glide.with(context!!)
            .load(questionList?.get(position)?.image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.ivIcon)


        holder.llView.setOnClickListener(View.OnClickListener {
            row_index = position
            iclickListener!!.selectedSport(questionList?.get(position)!!.id)
            notifyDataSetChanged()
        })

        if (row_index == position) {

            holder.llView.setBackgroundResource(R.drawable.shape_rounded_white)
            holder.tvTitle.setTextColor(R.color.login_button)

        } else {
            holder.llView.setBackgroundResource(R.drawable.shape_rounded_white_border)
            holder.tvTitle.setTextColor(Color.WHITE)

        }



    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val llView: LinearLayout = itemView.findViewById(R.id.llView)


    }


    fun setClickListener(iclickListener: ChooseProfessionActivity) {
        this.iclickListener = iclickListener
    }


    interface IClickListenerChooseProfession {
        fun selectedSport(position: Int)

    }



}