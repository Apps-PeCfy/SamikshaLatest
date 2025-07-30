package com.samiksha.ui.home

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingPathImplementer(var view: ITrainingPathView) :
    ITrainingPathPresenter  {
    
    override fun trainingSkillList(token: String?, startDate: String?) {
        view.showWait()
        val call = ApiClient.client.getTrainingSkillList("Bearer $token", startDate)
        call!!.enqueue(object : Callback<TrainingPathModel?> {
            override fun onResponse(
                call: Call<TrainingPathModel?>,
                response: Response<TrainingPathModel?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.trainingMentalSkillListSuccess(response.body())
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

    override fun trainingSkillListNext(token: String?, startDate: String?,currentPage:Int?) {
        view.showWait()
        val call = ApiClient.client.getTrainingSkillListNext("Bearer $token", startDate,currentPage)
        call!!.enqueue(object : Callback<TrainingPathModel?> {
            override fun onResponse(
                call: Call<TrainingPathModel?>,
                response: Response<TrainingPathModel?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.trainingMentalSkillListSuccess(response.body())
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


    override fun deleteSchedule(token: String?, scheduleID: String?) {
        view.showWait()
        val call = ApiClient.client.deleteSchedule("Bearer $token",scheduleID)
        call!!.enqueue(object : Callback<TrainingPathModel?> {
            override fun onResponse(
                call: Call<TrainingPathModel?>,
                response: Response<TrainingPathModel?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.deleteScheduleSuccess(response.body())
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

    override fun repeatSchedule(token: String?, scheduleID: String?) {
        view.showWait()
        val call = ApiClient.client.repeatSchedule("Bearer $token",scheduleID)
        call!!.enqueue(object : Callback<TrainingPathModel?> {
            override fun onResponse(
                call: Call<TrainingPathModel?>,
                response: Response<TrainingPathModel?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.repeatScheduleSuccess(response.body())
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

    override fun skillDetails(token :String?,skillID: String?) {

        view.showWait()
        val call = ApiClient.client.getMentalSkillDetails("Bearer $token",skillID)
        call!!.enqueue(object : Callback<SkillDetailsResponsePOJO?> {
            override fun onResponse(
                call: Call<SkillDetailsResponsePOJO?>,
                response: Response<SkillDetailsResponsePOJO?>
            ) {

                if (response.code() == 200) {
                    view.removeWait()
                    view.onSkillDetails(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SkillDetailsResponsePOJO?>,
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