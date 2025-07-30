package com.samiksha.ui.downloads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.samiksha.R
import com.samiksha.database.LocalCrudRepository
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.utils.DownloadUtils
import com.samiksha.utils.ProjectUtils


class DownloadsAdapter() : RecyclerView.Adapter<DownloadsAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mList: ArrayList<SkillModel>? = null
    var mListener: DownloadsAdapterInterface? = null
    private var localCrudRepository: LocalCrudRepository? = null


    fun updateAdapter(mList: ArrayList<SkillModel>?) {
        this.mList = mList
        notifyDataSetChanged()
    }

    interface DownloadsAdapterInterface {
        fun onItemSelected(position: Int, skillModel: SkillModel, skillDetail : SkillDetailsResponsePOJO.Data)
        fun onDeleteClick(position: Int, skillModel: SkillModel, skillDetail : SkillDetailsResponsePOJO.Data)
    }

    constructor(context: Context?, mList: ArrayList<SkillModel>,  mListener: DownloadsAdapterInterface?) : this() {
        this.context = context
        this.mList = mList
        this.mListener = mListener
        localCrudRepository = LocalCrudRepository.getInstance(context)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_downloads, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val skillModel: SkillModel = mList!![position]

        var model: SkillDetailsResponsePOJO.Data = Gson().fromJson(skillModel.jsonData, SkillDetailsResponsePOJO.Data::class.java)

        holder.txtName.text = model.name
        

        if (model.time == "Morning") {
            holder.imgTiming.setBackgroundResource(R.drawable.iv_morning)
        } else if (model.time == "Afternoon") {
            holder.imgTiming.setBackgroundResource(R.drawable.iv_afternoon)

        } else {
            holder.imgTiming.setBackgroundResource(R.drawable.iv_evening)

        }


        holder.itemView.setOnClickListener {
            mListener?.onItemSelected(position, skillModel, model)
        }


        holder.imgDelete.setOnClickListener {
            mListener?.onDeleteClick(position, skillModel, model)
        }

        if (localCrudRepository?.isTrainingCompleteOnDate(skillModel.user_id, ProjectUtils.getTodayDate("yyyy-MM-dd"), skillModel.type)!! > 0){
            holder.txtTrainingStatus.visibility = View.VISIBLE
            holder.txtTrainingStatus.text = "Completed on - ${ProjectUtils.getTodayDate("dd/MM/yyyy")}."
        }else{
            holder.txtTrainingStatus.visibility = View.GONE
        }
        

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        val imgTiming: ImageView = itemView.findViewById(R.id.img_timing)
        val txtTrainingStatus: TextView = itemView.findViewById(R.id.txt_training_status)
    }


}