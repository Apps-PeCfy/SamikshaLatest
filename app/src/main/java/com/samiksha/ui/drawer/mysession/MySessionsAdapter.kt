package com.samiksha.ui.drawer.mysession

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.ui.sportPsychology.session.MySessionCoach
import com.samiksha.ui.sportPsychology.session.MySessionSubAdapter


class MySessionsAdapter(

    var context: FragmentActivity?,
    var sessionList: List<MySessionResponsePOJO.DataItem>?,
    var userRole: String?,
   var playerRole: String?
) :
    RecyclerView.Adapter<MySessionsAdapter.MyViewHolder>() {

    var mySessionsSubAdapter: MySessionSubAdapter? = null



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_my_session, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return sessionList!!.size
    }

    override fun onBindViewHolder(holder: MySessionsAdapter.MyViewHolder, position: Int) {


        mySessionsSubAdapter =
            MySessionSubAdapter(context, sessionList!!.get(position).subUserSchedules, userRole,playerRole)
        holder.recyclermySubsessions!!.layoutManager = LinearLayoutManager(context)
        holder.recyclermySubsessions!!.adapter = mySessionsSubAdapter
        mySessionsSubAdapter!!.setCommentClickListener(MySessionCoach())


        /*holder.itemView.setOnClickListener {
            if (holder.recyclermySubsessions.visibility == View.VISIBLE){
                holder.recyclermySubsessions.visibility = View.GONE
                holder.imgArrow.rotation = 270F
            }else{
                holder.recyclermySubsessions.visibility = View.VISIBLE
                holder.imgArrow.rotation = 0F
            }
        }*/
        holder.recyclermySubsessions.visibility = View.VISIBLE
        holder.txtSessionScale.text = sessionList!!.get(position).schedularScale
        holder.tvSessionName.text = sessionList!!.get(position).itemName
        /*if (position == (sessionList!!.size - 1)) {
            holder.tvView.visibility = View.GONE
        }*/






        /*      holder.editComment.addTextChangedListener(
                 object : TextWatcher {

                     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                     }

                     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                     }

                     override fun afterTextChanged(s: Editable?) {
                         icommentclick!!.commentClick(holder.editComment.text.toString(), sessionList!![position].id)

                     }
                 }
             )
     */

    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvView: View = itemView.findViewById(R.id.tvView)
        val imgArrow: ImageView = itemView.findViewById(R.id.img_arrow)
        val tvSessionName: TextView = itemView.findViewById(R.id.tvSessionName)
        val txtSessionScale: TextView = itemView.findViewById(R.id.txtSessionScale)
        val recyclermySubsessions: RecyclerView = itemView.findViewById(R.id.recyclermySubsessions)

    }




}