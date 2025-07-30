package com.samiksha.ui.informationScreen

import com.samiksha.networking.ApiClient
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionImplementer(var view: IQuetionView) :
    IQuestionPresenter  {
    override fun question()     {
        view.showWait()
        val call = ApiClient.client.getQuestions()
        call!!.enqueue(object : Callback<QuestionResponsePOJO?> {
            override fun onResponse(
                call: Call<QuestionResponsePOJO?>,
                response: Response<QuestionResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onQuestionSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<QuestionResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

}