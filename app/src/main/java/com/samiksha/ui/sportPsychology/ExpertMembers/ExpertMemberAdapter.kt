package com.samiksha.ui.sportPsychology.ExpertMembers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersFragment
import com.samiksha.utils.RoundedImageView

class ExpertMemberAdapter(
    var context: FragmentActivity,
    var userList: ArrayList<UserResponsePOJO.DataItem>?,
    var role: String?
) : RecyclerView.Adapter<ExpertMemberAdapter.CategoryViewHolder>() {

    private var iecpertClickListener: IClickListener? = null

    fun updateAdapter(list: java.util.ArrayList<UserResponsePOJO.DataItem>?) {
        userList = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.templete_academy_members, viewGroup, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: CategoryViewHolder, position: Int) {

        if(role.equals("Coach")){
            viewHolder.img_learning_next.visibility = View.VISIBLE
        }else{
            viewHolder.img_learning_next.visibility = View.VISIBLE

        }

        viewHolder.tvUserName.text = userList?.get(position)?.name
        viewHolder.tvProfessionalLevel.text = userList?.get(position)?.professionalLevel!!.name

        Glide.with(context)
            .load(userList?.get(position)?.profilePic)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(viewHolder.iv_user)



        viewHolder.rl_learning.setOnClickListener(View.OnClickListener {

            iecpertClickListener?.expertMemberClick(position, userList!![position].id!!)


        })


    }

    override fun getItemCount(): Int {
        return userList?.size!!
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvUserName: TextView
        var tvProfessionalLevel: TextView
        var rl_learning: RelativeLayout
        var iv_user: RoundedImageView
        var img_learning_next: ImageView


        init {
            tvUserName = itemView.findViewById<View>(R.id.tvUserName) as TextView
            tvProfessionalLevel = itemView.findViewById<View>(R.id.tvProfessionalLevel) as TextView
            rl_learning = itemView.findViewById<View>(R.id.rl_learning) as RelativeLayout
            iv_user = itemView.findViewById<View>(R.id.iv_user) as RoundedImageView
            img_learning_next = itemView.findViewById<View>(R.id.img_learning_next) as ImageView


        }

    }


    fun setClickListener(iecpertClickListener: ExpertMemberFragment) {
        this.iecpertClickListener = iecpertClickListener
    }


    interface IClickListener {
        fun expertMemberClick(position: Int, id: String?)

    }


}