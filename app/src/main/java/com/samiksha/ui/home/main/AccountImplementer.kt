package com.samiksha.ui.home.main

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountImplementer(var view: IAccountView) :
    IAccountPresenter  {
    override fun stateList(countryId: String?) {
        val call = ApiClient.client.getState(countryId)
        call!!.enqueue(object : Callback<StateResponsePOJO?> {
            override fun onResponse(
                call: Call<StateResponsePOJO?>,
                response: Response<StateResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    view.stateListSuccess(response.body())
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

    override fun cityList(stateId: String?) {
        val call = ApiClient.client.getCity(stateId)
        call!!.enqueue(object : Callback<StateResponsePOJO?> {
            override fun onResponse(
                call: Call<StateResponsePOJO?>,
                response: Response<StateResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    view.cityListSuccess(response.body())
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

    override fun getProfile(token: String?) {
        val call = ApiClient.client.getProfile("Bearer $token")
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
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