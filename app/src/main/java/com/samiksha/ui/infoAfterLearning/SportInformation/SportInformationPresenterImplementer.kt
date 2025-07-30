package com.samiksha.ui.infoAfterLearning.SportInformation

import android.util.Log
import com.samiksha.networking.ApiClient
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SportInformationPresenterImplementer(var view: ISportInformationView) : ISportInformationPresenter {

    override fun submitAnswer(token: String?, sub_skill_id: String?, answer_id: String?) {
        val c = Calendar.getInstance()
        val z = c.timeZone
        val createdDate: String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.time)
        val timeZone = z.id
       // Log.d("sewswsdwa","   "+timeZone+"   "+fDate)
        view.showWait()
        val call = ApiClient.client.submitUserAnswer("Bearer $token",sub_skill_id, answer_id,timeZone,createdDate)
        call!!.enqueue(object : Callback<SportInformationModel?> {
            override fun onResponse(
                call: Call<SportInformationModel?>,
                response: Response<SportInformationModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.submitAnswerSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SportInformationModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun updateSkillStatus(token: String?, sub_skill_id: String?, context_type: String?) {
      //  view.showWait()
        val c = Calendar.getInstance()
        val z = c.timeZone
        val createdDate: String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.time)
        val timeZone = z.id


        val call = ApiClient.client.updateSkillStatus("Bearer $token",sub_skill_id, context_type,timeZone,createdDate)
        call!!.enqueue(object : Callback<SportInformationModel?> {
            override fun onResponse(
                call: Call<SportInformationModel?>,
                response: Response<SportInformationModel?>
            ) {
              //  view.removeWait()
                if (response.code() == 200) {
                    view.updateSkillStatus(response.body())
                } else if (response.code() == 400||response.code()==422) {
                 //   view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SportInformationModel?>,
                t: Throwable
            ) {
              //  view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun addToMyTraining(token: String?, skill_id: String?) {
        val c = Calendar.getInstance()
        val z = c.timeZone
        val createdDate: String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.time)
        val timeZone = z.id

        view.showWait()
        val call = ApiClient.client.addToMyTraining("Bearer $token",skill_id,timeZone,createdDate)
        call!!.enqueue(object : Callback<SportInformationModel?> {
            override fun onResponse(
                call: Call<SportInformationModel?>,
                response: Response<SportInformationModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.addToMyTrainingSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SportInformationModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun progressQuiz(token: String?, rewardsPoint: Int?, mental_skill_id: String?) {

        val call = ApiClient.client.addUserReward("Bearer $token","MentalSkill",mental_skill_id!!,"PQ",rewardsPoint!!)
        call!!.enqueue(object : Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                  view.removeWait()
                if (response.code() == 200) {
                    view.progressQuizSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    // view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>,
                t: Throwable
            ) {
                //  view.removeWait()
                view.onFailure(t.message)
            }
        })

    }

    override fun addToTrainingSchedule(token: String?, skill_id: String?, group: String?) {
      //  view.showWait()
        val call = ApiClient.client.addToTrainingSchedule("Bearer $token",skill_id, group)
        call!!.enqueue(object : Callback<SportInformationModel?> {
            override fun onResponse(
                call: Call<SportInformationModel?>,
                response: Response<SportInformationModel?>
            ) {
              //  view.removeWait()
                if (response.code() == 200) {
                    view.addToTrainingSchedule(response.body())
                } else if (response.code() == 400||response.code()==422) {
                   // view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SportInformationModel?>,
                t: Throwable
            ) {
              //  view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun updateTrainingSchedule(token: String?, skill_id: String?) {
        view.showWait()
        val call = ApiClient.client.updateTrainingSchedule("Bearer $token",skill_id)
        call!!.enqueue(object : Callback<SportInformationModel?> {
            override fun onResponse(
                call: Call<SportInformationModel?>,
                response: Response<SportInformationModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.updateTrainingSchedule(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SportInformationModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }


}