package com.samiksha.ui.drawer.readempoints

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.ui.drawer.readempoints.pojo.CoupanListResponsePOJO
import com.samiksha.ui.drawer.subscription.CounsellingSessionPaymentActivity
import com.samiksha.ui.drawer.subscription.SubscriptionProcessACtivity
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.SessionManager
import java.text.SimpleDateFormat

class RedeemPointsAdapter(
    var context: FragmentActivity?,
    var coupanList: List<CoupanListResponsePOJO.DataItem>?,
    var subscription: CheckOtpResponsePOJO.Subscription?
) :
    RecyclerView.Adapter<RedeemPointsAdapter.MyViewHolder>() {
    var preferencesManager: PreferencesManager? = null
    private var sessionManager: SessionManager? = null


    var getCallingActivity: String? = null


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_redeem_points, viewGroup, false)
        PreferencesManager.initializeInstance(context!!)
        preferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(context!!)

        getCallingActivity = preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)



        return MyViewHolder(v)
    }


    override fun getItemCount(): Int {
        return coupanList!!.size
    }

    override fun onBindViewHolder(holder: RedeemPointsAdapter.MyViewHolder, position: Int) {

        holder.tvDescription.text = coupanList?.get(position)!!.appDescription
        holder.txt_description.text = coupanList?.get(position)!!.description
        holder.tvCoupanCode.text = coupanList?.get(position)!!.couponName

        if(coupanList?.get(position)!!.expireAt.isNullOrEmpty()){
            holder.txt_expiry_date .visibility = View.GONE
        }else{
            holder.txt_expiry_date .visibility = View.VISIBLE

            var oldDateFormat = SimpleDateFormat("yyyy-MM-dd")

            var newDateFormat = SimpleDateFormat("dd/MM/yyyy")

            val convertedDate = newDateFormat.format(oldDateFormat!!.parse(coupanList?.get(position)!!.expireAt))

            holder.txt_expiry_date.text = "Coupon expires on "+convertedDate


        }



        Glide.with(context!!)
            .load(coupanList?.get(position)!!.companyIcon)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(holder.imgCoupan)



        if (coupanList?.get(position)!!.discountType.equals("Subscription")) {

                if (sessionManager?.getUserModel()?.totalRewards!! >= (coupanList?.get(position)!!.rewardPoint!!).toInt()) {
                    holder.imgCopy.visibility = View.GONE
                    holder.tvApply.visibility = View.VISIBLE

                }

        }else{
            if (sessionManager?.getUserModel()?.totalRewards!! >= (coupanList?.get(position)!!.rewardPoint!!).toInt()) {
                holder.imgCopy.visibility = View.VISIBLE
                holder.tvApply.visibility = View.GONE

            }

        }


        /*if (coupanList?.get(position)!!.rewardPoint.isNullOrEmpty()) {
            holder.tvApply.visibility = View.GONE
            holder.imgCopy.visibility = View.GONE

        } else {
            if (sessionManager?.getUserModel()?.totalRewards!! >= (coupanList?.get(position)!!.rewardPoint!!).toInt()) {
                if (coupanList?.get(position)!!.discountType.equals("Subscription")) {
                    holder.tvApply.visibility = View.VISIBLE
                    holder.imgCopy.visibility = View.VISIBLE
                    holder.imgCopy.setImageResource(android.R.color.transparent)
                } else {
                    holder.tvApply.visibility = View.GONE

                }
            } else {
                holder.tvApply.visibility = View.GONE
                holder.imgCopy.visibility = View.GONE

            }

        }*/



        holder.imgCopy.setOnClickListener(View.OnClickListener {
            val clipboard =
                context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", coupanList?.get(position)!!.couponName)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()

        })

        holder.tvApply.setOnClickListener(View.OnClickListener {

            var sub_id = preferencesManager!!.getStringValue(Constants.SUBSCRIPTION_ID)
            if (preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)
                    .equals("SubscriptionActivity")
            ) {
                if (subscription!!.name!!.isEmpty()) {
                    if (coupanList?.get(position)!!.discountType.equals("Subscription")
                        && (coupanList?.get(position)!!.discountId)?.equals(sub_id!!.toInt())!!
                    ) {
                        if (coupanList?.get(position)!!.status.equals("Active")) {
                            val intent = Intent(context, SubscriptionProcessACtivity::class.java)
                            intent!!.putExtra("CopuanName", coupanList?.get(position)!!.couponName)
                            intent!!.putExtra("CopuanAmt", coupanList?.get(position)!!.amount)
                            intent!!.putExtra("CopuanID", coupanList?.get(position)!!.id)
                            context!!.startActivity(intent)
                            context!!.finish()
                        } else {
                            Toast.makeText(
                                context,
                                "You already use this coupon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Coupon not applicable", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "You have already subscription", Toast.LENGTH_SHORT)
                        .show()
                }

            } else if (preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)
                    .equals("CounsellingSessionPaymentActivity")
            ) {
                if (coupanList?.get(position)!!.discountType.equals("Subscription")) {
                    if (coupanList?.get(position)!!.status.equals("Active")) {
                        val intent = Intent(context, CounsellingSessionPaymentActivity::class.java)
                        intent!!.putExtra("CopuanName", coupanList?.get(position)!!.couponName)
                        intent!!.putExtra("CopuanAmt", coupanList?.get(position)!!.amount)
                        intent!!.putExtra("CopuanID", coupanList?.get(position)!!.id)
                        context!!.startActivity(intent)
                        context!!.finish()
                    } else {
                        Toast.makeText(context, "You already use this coupon", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Coupon not applicable", Toast.LENGTH_SHORT).show()
                }


            }


        })


    }


    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgCoupan: ImageView = itemView.findViewById(R.id.imgCoupan)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvCoupanCode: TextView = itemView.findViewById(R.id.tvCoupanCode)
        val tvApply: TextView = itemView.findViewById(R.id.tvApply)
        val txt_expiry_date: TextView = itemView.findViewById(R.id.txt_expiry_date)
        val txt_description: TextView = itemView.findViewById(R.id.txt_description)
        val imgCopy: ImageView = itemView.findViewById(R.id.imgCopy)

    }


}