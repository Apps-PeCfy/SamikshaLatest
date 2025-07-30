package com.samiksha.ui.sportPsychology.session

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.mysession.MySessionResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MySessionSubAdapter(
    var context: FragmentActivity?,
    var sessionList: List<MySessionResponsePOJO.SubUserSchedulesItem>?,
    var userRole: String?,
    var playerRole: String?
) :
    RecyclerView.Adapter<MySessionSubAdapter.MyViewHolder>() {
    private var icommentclick: ICommentClickListener? = null
    private var mPreferencesManager: PreferencesManager? = null
    var token: String? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_my_session_sub, viewGroup, false)
        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return sessionList!!.size
    }

    override fun onBindViewHolder(holder: MySessionSubAdapter.MyViewHolder, position: Int) {

        PreferencesManager.initializeInstance(context!!)
        mPreferencesManager = PreferencesManager.instance


        if (sessionList!!.get(position).status.equals("Completed")) {
            holder.txtModule.visibility = View.VISIBLE
            holder.txtModule.text = "Module - " + sessionList!![position].module_name
        } else {
            holder.txtModule.visibility = View.GONE

        }

        if (userRole.equals("Coach")) {
            holder.tvJoin.visibility = View.GONE
        } else {
            holder.tvJoin.visibility = View.VISIBLE

        }

        if (sessionList!!.get(position).link == null) {
            holder.tvJoin.visibility = View.GONE
        } else {
            if (sessionList!!.get(position).status.equals("Completed")) {
                holder.tvJoin.visibility = View.GONE
            } else {
                holder.tvJoin.visibility = View.VISIBLE

            }

        }


        if (userRole.equals("Counsellor")) {
            holder.tvEdit.visibility = View.VISIBLE
        } else {
            if (userRole.equals("SuperCounsellor") && playerRole.equals("MasterUser")) {
                holder.tvEdit.visibility = View.VISIBLE

            } else {
                holder.tvEdit.visibility = View.GONE

            }


        }


        if (userRole.equals("Counsellor") || userRole.equals("SuperCounsellor")) {
            holder.rlComment.visibility = View.VISIBLE
            holder.editComment.setText(sessionList?.get(position)?.comment)
        } else {
            holder.rlComment.visibility = View.GONE


        }

        holder.txtStatus.text = "Status - " + sessionList!!.get(position).status
        holder.tvSessionName.text = sessionList!!.get(position).name


        if (sessionList!!.get(position).goal == null) {
            holder.txtSessionScale.visibility = View.GONE
        } else {
            holder.txtSessionScale.visibility = View.VISIBLE
            holder.txtSessionScale.text = "Goal - " + sessionList!!.get(position).goal!!.name
        }

        if (sessionList!!.get(position).startDate == null) {
            holder.txtSessionDate.visibility = View.GONE
        } else {
            holder.txtSessionDate.visibility = View.VISIBLE
            holder.txtSessionDate.text = "Session Date - " + sessionList!!.get(position).startDate
        }

        if (sessionList!!.get(position).counsellor!!.isEmpty()) {
            holder.txtcounsellor.visibility = View.GONE
        } else {
            holder.txtcounsellor.visibility = View.VISIBLE
            holder.txtcounsellor.text = "Session By - " + sessionList!!.get(position).counsellor
        }


        holder.tvEdit.setOnClickListener(View.OnClickListener {
            /*  val intent = Intent(context, AcademyMembersActivity::class.java)
              intent!!.action = "EditSession"
              context!!.startActivity(intent)
  */


          //  context!!.bottomLayout.visibility = View.GONE
          //  context!!.toolbar_home.title = "Edit Sessions"

            val gson = GsonBuilder().create()
            val jsonList: String = gson.toJson(sessionList!![position])
            val args = Bundle()
            args.putString("EditSessionData", jsonList)
            val fragobj = EditSessionFragment()
            fragobj.setArguments(args)

            val fragmentManager: FragmentManager = context!!.getSupportFragmentManager()
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_framelayout, fragobj)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        })


        holder.tvJoin.setOnClickListener(View.OnClickListener {

            if (sessionList!!.get(position).link == null) {
                holder.tvJoin.visibility = View.GONE
                Toast.makeText(context, "Google meet link not available", Toast.LENGTH_SHORT).show()
            } else {
                holder.tvJoin.visibility = View.VISIBLE
                val uri: Uri =
                    Uri.parse(sessionList!!.get(position).link) // google meet link

                val intent = Intent(Intent.ACTION_VIEW, uri)
                context!!.startActivity(intent)
            }


        })
        holder.commentSubmit.setOnClickListener(View.OnClickListener {

            token = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
            if ((sessionList?.get(position)?.comment).equals(
                    holder.editComment.text.toString().trim()
                )
            ) {

            } else {

                val progressDialogLogout = ProgressDialog(context)
                progressDialogLogout.setCancelable(false) // set cancelable to false
                progressDialogLogout.setMessage("Please Wait") // set message
                progressDialogLogout.show() // show progress dialog

                ApiClient.client.updateComment(
                    "Bearer $token",
                    sessionList!![position].id,
                    holder.editComment.text.toString()
                )!!.enqueue(object :
                    Callback<OnlyMessageResponsePOJO?> {
                    override fun onResponse(
                        call: Call<OnlyMessageResponsePOJO?>,
                        response: Response<OnlyMessageResponsePOJO?>
                    ) {
                        progressDialogLogout.dismiss()
                        holder.editComment.clearFocus()
                        if (response.code() == 200) {
                            Toast.makeText(
                                context,
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else if (response.code() == 422) {
                            val gson = GsonBuilder().create()
                            var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                            try {
                                pojo = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ValidationResponsePOJO::class.java
                                )
                                Toast.makeText(
                                    context,
                                    pojo.errors!!.get(0).message,
                                    Toast.LENGTH_LONG
                                ).show()


                            } catch (exception: IOException) {
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Internal Server Error",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onFailure(
                        call: Call<OnlyMessageResponsePOJO?>, t: Throwable
                    ) {
                        progressDialogLogout.dismiss()
                        Toast.makeText(
                            context,
                            t.message,
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
                })

            }


        })

        if(sessionList!!.get(position).status.equals("Unscheduled")){
           // holder.tvEdit.visibility = View.GONE
            holder.txtSessionScale.visibility = View.GONE
            holder.txtSessionDate.visibility = View.GONE
            holder.txtcounsellor.visibility = View.GONE
            holder.tvJoin.visibility = View.GONE
            holder.rlComment.visibility = View.GONE


        }


    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvView: View = itemView.findViewById(R.id.tvView)
        val tvSessionName: TextView = itemView.findViewById(R.id.tvSessionName)
        val txtSessionDate: TextView = itemView.findViewById(R.id.txtSessionDate)
        val txtSessionScale: TextView = itemView.findViewById(R.id.txtSessionScale)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val txtexpertCounsellor: TextView = itemView.findViewById(R.id.txtexpertCounsellor)
        val txtcounsellor: TextView = itemView.findViewById(R.id.txtcounsellor)
        val tvJoin: TextView = itemView.findViewById(R.id.tvJoin)
        val tvEdit: TextView = itemView.findViewById(R.id.tvEdit)
        val rlComment: RelativeLayout = itemView.findViewById(R.id.rlComment)
        val editComment: TextView = itemView.findViewById(R.id.editComment)
        val commentSubmit: TextView = itemView.findViewById(R.id.commentSubmit)
        val txtModule: TextView = itemView.findViewById(R.id.txtModule)
    }


    fun setCommentClickListener(icommentclick: MySessionCoach) {
        this.icommentclick = icommentclick
    }


    interface ICommentClickListener {
        fun commentClick(comment: String?, id: Int)

    }


}