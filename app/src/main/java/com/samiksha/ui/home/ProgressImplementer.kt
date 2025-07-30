package com.samiksha.ui.home

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressImplementer(var view: IProgressView) :
    IProgressPresenter  {
    override fun progressSkillList(token: String?,date:String?) {
        view.showWait()
        val call = ApiClient.client.getProgressList("Bearer $token",date)
        call!!.enqueue(object : Callback<TrainingPathModel?> {
            override fun onResponse(
                call: Call<TrainingPathModel?>,
                response: Response<TrainingPathModel?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.progressMentalSkillListSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<TrainingPathModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }



    override fun updateRegisterDate(token: String?) {
        val call = ApiClient.client.getProfile("Bearer $token")
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    view.getProfileSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {
                view.onFailure(t.message)
            }
        })
    }

}