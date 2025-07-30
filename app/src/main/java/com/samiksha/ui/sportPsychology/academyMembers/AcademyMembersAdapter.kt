package com.samiksha.ui.sportPsychology.academyMembers

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
import com.samiksha.ui.sportPsychology.ExpertMembers.ExpertMemberFragment
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.utils.RoundedImageView
import java.util.*

class AcademyMembersAdapter(
    var context: FragmentActivity,
    var userList: ArrayList<UserResponsePOJO.DataItem>?,
    var role: String?
) : RecyclerView.Adapter<AcademyMembersAdapter.CategoryViewHolder>() {

    private var iclickListener: IClickListener? = null

    fun updateAdapter(list: ArrayList<UserResponsePOJO.DataItem>?) {
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

        var model = userList?.get(position)

        if(role.equals("Coach")){
            viewHolder.img_learning_next.visibility = View.VISIBLE
        }else{
            viewHolder.img_learning_next.visibility = View.VISIBLE

        }

        viewHolder.tvUserName.text = model?.name
        viewHolder.tvProfessionalLevel.text = model?.professionalLevel!!.name

        Glide.with(context)
            .load(model.profilePic)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(viewHolder.iv_user)



        viewHolder.rl_learning.setOnClickListener(View.OnClickListener {


            iclickListener!!.memberClick(position,model)


        })


    }

    override fun getItemCount(): Int {
        return userList!!.size
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


    fun setClickListener(iclickListener: AcademyMembersFragment) {
        this.iclickListener = iclickListener
    }

 fun setClickListener1(iclickListener: ExpertMemberFragment) {
        this.iclickListener = iclickListener
    }


    interface IClickListener {
        fun memberClick(position: Int, model: UserResponsePOJO.DataItem?)

    }

}