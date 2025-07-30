@file:Suppress("DEPRECATION")

package com.samiksha.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import com.samiksha.R
import com.samiksha.ui.home.main.TrainingPathModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object ProjectUtils {

    private var pDialog: ProgressDialog? = null
    private var isInternetPresent = false
    private var cd: ConnectionDetector? = null


    var s1 = "Java"
    fun showAlertDialog(mContext: Context?) {
        if (mContext != null) {
            val alertDialog = AlertDialog.Builder(mContext)
            alertDialog.setCancelable(false)
            alertDialog.setTitle(mContext.getString(R.string.app_name))
            alertDialog.setMessage("Are you sure, you want to exit?")
            alertDialog.setPositiveButton("No") { dialog, which -> dialog.cancel() }
            alertDialog.setNegativeButton("Yes") { dialog, which -> finishAffinity(mContext as Activity) }





            alertDialog.show()
        }
    }

    fun showToast(mContext: Context?, message: String) {
    //  val Toast toast= Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT);

        val toast: Toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    // This method is for checking internet connection
    fun checkInternetAvailable(mContext: Context?): Boolean? {
        cd = ConnectionDetector(mContext!!)
        isInternetPresent =
            cd!!.isConnectingToInternet
        return isInternetPresent
    }


    fun showProgressDialog(mContext: Context?) {
        if(!(mContext as Activity).isFinishing){
            pDialog = ProgressDialog(mContext)
            pDialog?.setMessage("Please wait...")
            pDialog?.setCancelable(false)
            pDialog?.setCanceledOnTouchOutside(false)

            pDialog?.show()
        }

    }

    fun dismissProgressDialog() {
        if (pDialog != null && pDialog!!.isShowing) pDialog!!.dismiss()
    }

    fun getTodayDate(dateFormat: String?): String? {
        val dateFormat: DateFormat = SimpleDateFormat(dateFormat)
        val date = Date()
        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun changeDateFormat(sourceDate: String?, oldFormat: String?, newFormat: String?): String? {
        var outputText: String = ""
        if (sourceDate != null && oldFormat != null && newFormat != null){
            val formatter = SimpleDateFormat(oldFormat)
            val outputFormat = SimpleDateFormat(newFormat)
            try {
                val date = formatter.parse(sourceDate)
                outputText = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        return outputText
    }

    @SuppressLint("SimpleDateFormat")
    fun dateToRequiredFormat(sourceDate: Date?, newFormat: String?): String? {
        var outputText: String = ""
        if (sourceDate != null && newFormat != null){
            val outputFormat = SimpleDateFormat(newFormat)
            try {
                outputText = outputFormat.format(sourceDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        return outputText
    }

    fun getDaysList(scheduleDays: ArrayList<TrainingPathModel.ScheduleDays>): ArrayList<TrainingPathModel.ScheduleDays> {
        var list: ArrayList<TrainingPathModel.ScheduleDays> = ArrayList()
        var  model = TrainingPathModel().ScheduleDays()
        model.name = "Monday"
        model.flag = "M"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Tuesday"
        model.flag = "T"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Wednesday"
        model.flag = "W"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Thursday"
        model.flag = "T"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Friday"
        model.flag = "F"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Saturday"
        model.flag = "S"
        model.is_complete = false
        list.add(model)

        model = TrainingPathModel().ScheduleDays()
        model.name = "Sunday"
        model.flag = "S"
        model.is_complete = false
        list.add(model)

        for (model in list){
            for (schedule in scheduleDays){
                if(model.name == schedule.name){
                    model.is_complete = true
                }
            }
        }




        return list
    }
}