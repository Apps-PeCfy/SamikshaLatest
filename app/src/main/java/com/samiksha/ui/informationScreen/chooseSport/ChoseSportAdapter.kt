package com.samiksha.ui.informationScreen.chooseSport

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import java.util.ArrayList

class ChoseSportAdapter(
    var context: FragmentActivity?,
    var questionList: ArrayList<QuestionResponsePOJO.OptionsItem>
) :
    RecyclerView.Adapter<ChoseSportAdapter.MyViewHolder>() {

    lateinit var myHolder: MyViewHolder
    var row_index = -1
    private var iclickListener: IClickListenerChooseSport? = null



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.choose_sport_templete, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return questionList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ChoseSportAdapter.MyViewHolder, position: Int) {
        myHolder = holder
        holder.tvTitle.text = questionList[position].name
        Glide.with(context!!)
            .load(questionList[position].image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.ivIcon!!)


        holder.llView.setOnClickListener(View.OnClickListener {

            if(questionList[position].status.equals("Active")){
                row_index = position
                iclickListener?.selectedSport(questionList[position].id)

                notifyDataSetChanged()

            }else{
                Toast.makeText(context,"This sport is currently inactive.",Toast.LENGTH_SHORT).show()
            }

        })

        if (row_index == position) {

            myHolder.llView.setBackgroundResource(R.drawable.shape_rounded_white)
            myHolder.tvTitle.setTextColor(R.color.login_button)

        } else {
            myHolder.llView.setBackgroundResource(R.drawable.shape_rounded_white_border)
            myHolder.tvTitle.setTextColor(Color.WHITE)

        }


    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val llView: LinearLayout = itemView.findViewById(R.id.llView)
    }


    fun setClickListener(iclickListener: ChooseSportActivity) {
        this.iclickListener = iclickListener
    }


    interface IClickListenerChooseSport {
        fun selectedSport(position: Int)

    }



}