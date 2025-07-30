package com.samiksha.ui.drawer.mytraining

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTrainingImplementer(var view: IMyTrainingView) : IMyTrainingPresenter  {

    override fun getMyActivities(token: String?, startDate: String?, endDate: String?, page: Int?) {
        view.showWait()
        val call = ApiClient.client.getMyActivities("Bearer $token",startDate, endDate, page)
        call!!.enqueue(object : Callback<MyTrainingModel?> {
            override fun onResponse(
                call: Call<MyTrainingModel?>,
                response: Response<MyTrainingModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.getMyActivitiesSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<MyTrainingModel?>,
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
                view.removeWait()
                if (response.code() == 200) {
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




}