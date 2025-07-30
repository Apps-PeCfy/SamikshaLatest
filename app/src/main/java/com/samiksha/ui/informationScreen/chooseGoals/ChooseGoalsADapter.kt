package com.samiksha.ui.informationScreen.chooseGoals

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

class ChooseGoalsADapter(
    var context: FragmentActivity?,
    var questionList: List<QuestionResponsePOJO.OptionsItem>?
) :
    RecyclerView.Adapter<ChooseGoalsADapter.MyViewHolder>() {
    var row_index = -1
    var goalsArrayList: ArrayList<Int> = ArrayList<Int>()
    private var iclickListener: IClickListenerChooseGoals? = null


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.templete_professional_level, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return questionList!!.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ChooseGoalsADapter.MyViewHolder, position: Int) {


        if (questionList?.get(position)!!.isSelected.equals("yes")) {
            if (goalsArrayList.contains(questionList!![position].id)) {

            } else {
                goalsArrayList.add(questionList!!.get(position).id)

            }

            holder.llView!!.setBackgroundResource(R.drawable.shape_rounded_white)
            holder.tvTitle!!.setTextColor(R.color.login_button)

        } else {

            goalsArrayList.remove(questionList!!.get(position).id)

            holder.llView!!.setBackgroundResource(R.drawable.shape_rounded_white_border)
            holder.tvTitle!!.setTextColor(Color.WHITE)


        }

        iclickListener!!.selectedSport(goalsArrayList)

        holder.tvTitle!!.text = questionList?.get(position)?.name
        Glide.with(context!!)
            .load(questionList?.get(position)?.image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.ivIcon!!)

        holder.llView!!.setOnClickListener(View.OnClickListener {

            if (questionList!![position].isSelected.equals("yes")) {

                questionList!![position].isSelected = "No"
                goalsArrayList.remove(questionList!!.get(position).id)
                iclickListener!!.selectedSport(goalsArrayList)
                notifyDataSetChanged()

            } else {

                if (goalsArrayList.size < 3) {
                    questionList!![position].isSelected = "yes"
                    goalsArrayList.add(questionList!!.get(position).id)
                    row_index = position
                    iclickListener!!.selectedSport(goalsArrayList)
                    notifyDataSetChanged()
                } else {
                    questionList!![position].isSelected = "No"
                    goalsArrayList.remove(questionList!!.get(position).id)
                    iclickListener!!.selectedSport(goalsArrayList)
                    notifyDataSetChanged()
                    if (goalsArrayList.size >= 3) {
                        Toast.makeText(
                            context,
                            "We select only three goals. Please Unselect one.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }


        })


        if (goalsArrayList.size == 3) {
            if (questionList!!.get(position).isSelected.equals("yes")) {

            } else {
                holder.llView!!.alpha = 0.3F

            }
        }else{
            if (questionList!!.get(position).isSelected.equals("yes")) {

            } else {
                holder.llView!!.alpha = 1.0F

            }
        }


    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val llView: LinearLayout = itemView.findViewById(R.id.llView)

        override fun onClick(v: View?) {
            if (v?.id == R.id.llView) {
                notifyDataSetChanged()
            }

        }


    }


    fun setClickListener(iclickListener: ChooseGoalsActivity) {
        this.iclickListener = iclickListener
    }


    interface IClickListenerChooseGoals {
        fun selectedSport(goalsArrayList: ArrayList<Int>)

    }


}