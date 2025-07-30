package com.samiksha.ui.home.dealingWithDistraction

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.register.pojo.StateResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class dealingWithDistractionImplementer(var view: IDealingDistractionView) :
    IDealingDistractionPresenter {

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

    override fun subscriptionList(token: String?) {
        view.showWait()
        val call = ApiClient.client.getSubscriptionList("Bearer $token")
        call!!.enqueue(object : Callback<SubscriptionModel?> {
            override fun onResponse(
                call: Call<SubscriptionModel?>,
                response: Response<SubscriptionModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onSubscriptionList(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SubscriptionModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun updateSubscriptions(token: String?, subscriptionId: String?) {
        view.showWait()
        val call = ApiClient.client.updateSubscription("Bearer $token",subscriptionId)
        call!!.enqueue(object : Callback<SubscriptionModel?> {
            override fun onResponse(
                call: Call<SubscriptionModel?>,
                response: Response<SubscriptionModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.updateSubscription(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<SubscriptionModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun createOrder(
        token: String?,
        context_type: String?,
        context_id: String?,
        item_name: String?,
        gst_no: String?,
        total_amount: Double?
    ) {
        view.showWait()
        val call = ApiClient.client.createOrderPayment("Bearer $token",context_type, context_id, item_name, gst_no, total_amount)
        call!!.enqueue(object : Callback<OrderModel?> {
            override fun onResponse(
                call: Call<OrderModel?>,
                response: Response<OrderModel?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.createOrderSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<OrderModel?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun state(country_id:String?) {

        val call = ApiClient.client.getState(country_id)
        call!!.enqueue(object : Callback<StateResponsePOJO?> {
            override fun onResponse(
                call: Call<StateResponsePOJO?>,
                response: Response<StateResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    view.onStateSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<StateResponsePOJO?>,
                t: Throwable
            ) {
                view.onFailure(t.message)
            }
        })
    }

}