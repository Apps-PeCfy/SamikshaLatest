package com.samiksha.ui.otp

import com.google.gson.GsonBuilder
import com.samiksha.networking.ApiClient.client
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class OtpPresenterImplmenter(var view: IOtpView) : IOtpPresenter {
    override fun checkOtp(
        country_phone_code: String?,
        phone_no: String?,
        otp: String?
    ) {
        view.showWait()
        val call = client.checkOtp(
            country_phone_code, phone_no, otp)
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(call: Call<CheckOtpResponsePOJO?>, response: Response<CheckOtpResponsePOJO?>) {
                view.removeWait()
                if (response.code() == 200) {
                    view.otpSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
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
                }else{
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



    override fun verifyOTP( type: String?,
                            country_phone_code: String?,
                            phone_no: String?,
                            otp: String?,
                            device_id: String?,
                            device_type: String?,
                            device_token: String?,
                            userCountry_code_Login: String?
    ) {
        view.showWait()
        val call = client.verifyOtp(
            type,country_phone_code, phone_no, otp,device_id,device_type,device_token,userCountry_code_Login)
        call!!.enqueue(object : Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(call: Call<CheckOtpResponsePOJO?>, response: Response<CheckOtpResponsePOJO?>) {
                view.removeWait()
                if (response.code() == 200) {
                    view.verifyotpSuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
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
                }else{
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




    override fun login(
        phoneNo: String?,
        type: String?,
        countryCode: String?

    )
    {
        view.showWait()
        val call = client.login(
            phoneNo, type,
            countryCode)
        call!!.enqueue(object : Callback<LoginResponcePOJO?> {
            override fun onResponse(
                call: Call<LoginResponcePOJO?>,
                response: Response<LoginResponcePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onLoginSUccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<LoginResponcePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun resendOtp(phoneNo: String?, type: String?, countryCode: String?) {
        view.showWait()
        val call = client.sendotpRegister(
            phoneNo, type,
            countryCode)
        call!!.enqueue(object : Callback<LoginResponcePOJO?> {
            override fun onResponse(
                call: Call<LoginResponcePOJO?>,
                response: Response<LoginResponcePOJO?>
            ) {
                view.removeWait()
                if (response.code() == 200) {
                    view.onLoginSUccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    view.removeWait()
                    view.onFailure(response.message())

                }else{
                    view.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<LoginResponcePOJO?>,
                t: Throwable
            ) {
                view.removeWait()
                view.onFailure(t.message)
            }
        })
    }

    override fun register(
        type: String?,
        first_name: String?,
        last_name: String?,
        email: String?,
        country_phone_code: String?,
        phone_no: String?,
        gender: String?,
        userCurrentState: String?
    )
    {


        if (first_name!!.length == 0) {
        }else{
            view.showWait()
            val call = client.register(
                type, first_name,last_name,email,country_phone_code,phone_no,gender,userCurrentState)
            call!!.enqueue(object : Callback<RegisterResponsePOJO?> {
                override fun onResponse(
                    call: Call<RegisterResponsePOJO?>,
                    response: Response<RegisterResponsePOJO?>
                ) {
                    view.removeWait()
                    if (response.code() == 200) {
                        view.onRegisterSuccess(response.body())
                    } else if (response.code() == 400||response.code()==422)
                    {
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


                    }else{
                        view.onFailure(response.message())

                    }
                }

                override fun onFailure(
                    call: Call<RegisterResponsePOJO?>,
                    t: Throwable
                ) {
                    view.removeWait()
                    view.onFailure(t.message)
                }
            })


        }


    }



}