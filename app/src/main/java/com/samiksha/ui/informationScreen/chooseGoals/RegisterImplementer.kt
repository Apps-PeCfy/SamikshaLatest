package com.samiksha.ui.informationScreen.chooseGoals

import com.google.gson.GsonBuilder
import com.samiksha.networking.ApiClient
import com.samiksha.ui.informationScreen.chooseGoals.pojo.VerifyOTPResponse
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterImplementer(var view: IRegisterView) : IRegisterPresenter {
    override fun registerUser(

        first_name: String?,
        last_name: String?,
        email: String?,
        country_phone_code: String?,
        phone_no: String?,
        otp: String?,
        gender: String?,
        sport_id: String?,
        professional_level_id: String?,
        goal_ids: ArrayList<Int>?,
        device_id: String?,
        device_type: String?,
        device_token: String?,
        userCurrentState: String?,
        referee_code: String?,
        type: String?,
        userUserCountryCOde: String?
    ) {

        view.showWait()
        val call = ApiClient.client.registerUser(
            first_name,
            last_name,
            email,
            country_phone_code,
            phone_no,
            otp,
            gender,
            sport_id,
            professional_level_id,
            goal_ids,
            device_id,
            device_type,
            device_token,
            userCurrentState,
            referee_code,
            type,userUserCountryCOde

            )
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onRegisterSuccess(response.body())
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        view.validationError(pojo)
                    } catch (exception: IOException) {
                    }
                }
            }

            override fun onFailure(
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })


    }

    override fun updateWebUser(
        token: String?,
        sport_id: String?,
        professional_level_id: String?,
        goal_ids: ArrayList<Int>?
    ) {

        view.showWait()
        val call = ApiClient.client.updateWebUser("Bearer $token",sport_id,professional_level_id, goal_ids)
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onUpdateWebUserSuccess(response.body())
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        view.validationError(pojo)
                    } catch (exception: IOException) {
                    }
                } else {
                    view.onFailure(response.message())
                }
            }

            override fun onFailure(
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })

    }

    override fun question() {
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
                } else if (response.code() == 400 || response.code() == 422) {
                    view.removeWait()
                    view.onFailure(response.message())

                } else {
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

    override fun updateGoals(
        token: String?,
        goal_ids: ArrayList<Int>?
    ) {

        view.showWait()
        val call = ApiClient.client.updateUserGoals("Bearer $token", goal_ids)
        call!!.enqueue(object : Callback<VerifyOTPResponse?> {
            override fun onResponse(
                call: Call<VerifyOTPResponse?>,
                response: Response<VerifyOTPResponse?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onUpdateUserSuccess(response.body())
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        view.validationError(pojo)
                    } catch (exception: IOException) {
                    }
                } else {
                    view.onFailure(response.message())
                }
            }

            override fun onFailure(
                call: Call<VerifyOTPResponse?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })


    }
}