package com.samiksha.ui.register

import android.widget.Toast
import com.google.gson.GsonBuilder
import com.samiksha.networking.ApiClient.client
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginPresenterImplementer(var view: ILoginView) :
    ILoginPresenter {
    override fun login(
        phoneNo: String?,
        type: String?,
        countryCode: String?,
        country_Code: String?


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


                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                         view.onFailure( pojo.errors!!.get(0).message)



                    } catch (exception: IOException) {
                    }

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

    override fun sendOtpRegister(phoneNo: String?, type: String?, countryCode: String?) {
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


                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        view.onFailure( pojo.errors!!.get(0).message)



                    } catch (exception: IOException) {
                    }

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
        })}


    override fun register(
        type: String?,
        first_name: String?,
        last_name: String?,
        email: String?,
        country_phone_code: String?,
        phone_no: String?,
        gender: String?,
        userState: String?
    )
    {


        if (first_name!!.length == 0) {
            view.validationErrorEmpty(1)
        }else{
            view.showWait()
            val call = client.register(
                type, first_name,last_name,email,country_phone_code,phone_no,gender,userState
            )
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

    override fun state(country_id:String?) {

        val call = client.getState(country_id)
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