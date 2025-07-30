package com.samiksha.commonMethod

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.samiksha.R
import com.samiksha.networking.ApiClient
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateProfile() {

    private var userData: CheckOtpResponsePOJO.User? = null
    private var sessionManager: SessionManager? = null
    private var mPreferencesManager: PreferencesManager? = null
    var contextM : Context?= null



    fun updateProfileData(context: Context, token: String?) {
        contextM = context
        sessionManager = SessionManager.getInstance(context)
        PreferencesManager.initializeInstance(context)
        mPreferencesManager = PreferencesManager.instance


        userData = sessionManager!!.getUserModel()
        val call = ApiClient.client.getProfile("Bearer $token")
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    userData = response.body()!!.user
                    mPreferencesManager!!.createLoginSession(userData)
                    SessionManager.getInstance(context).setUserModel(userData!!)
                } else if (response.code() == 400||response.code()==422) {

                }else{

                }
            }

            override fun onFailure(
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {
            }
        })

    }


    companion object {
        private var sInstance: UpdateProfile? = null

        @Synchronized
        fun getInstance(): UpdateProfile {
            if (sInstance == null) {
                sInstance = UpdateProfile()
            }
            return sInstance as UpdateProfile
        }

        fun showToast(context: Context) {





            var   mToastToShow: Toast? = null
            val toastDurationInMilliSeconds = 10000
            var i: Int = 0
            mToastToShow = Toast.makeText(context, R.string.subscription_plan_error, Toast.LENGTH_LONG)
            val toastCountDown: CountDownTimer
            toastCountDown = object :
                CountDownTimer(toastDurationInMilliSeconds.toLong(), 500 /*Tick duration*/) {
                override fun onTick(millisUntilFinished: Long) {
                    i = i+1
                    Log.d("onTick",i.toString())
                    mToastToShow.show()
                }

                override fun onFinish() {
                    Log.d("onTick1",i.toString())
                    mToastToShow.show()
                }
            }

           // mToastToShow.show()
            toastCountDown.start()
        }
    }
}