package com.samiksha.ui.drawer.faqs

import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.faqs.POJO.FAQResponsePOJO
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQImplementer(var view: IFAQView) :
    IFAQPresenter  {
    override fun getFaq(token: String?) {

        view.showWait()
        val call = ApiClient.client.getFaqs("Bearer $token")

        call!!.enqueue(object : Callback<FAQResponsePOJO?> {
            override fun onResponse(
                call: Call<FAQResponsePOJO?>,
                response: Response<FAQResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onFaqSUccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<FAQResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })

    }
}