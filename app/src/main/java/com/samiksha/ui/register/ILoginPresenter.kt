package com.samiksha.ui.register

interface ILoginPresenter {

    fun login(
        phoneNo: String?,
        type: String?,
        countryCode: String?,
        country_Code: String?
       )


fun sendOtpRegister(
        phoneNo: String?,
        type: String?,
        countryCode: String?
       )


    fun register(

        type: String?,
        first_name: String?,
        last_name: String?,
        email: String?,
        country_phone_code: String?,
        phone_no: String?,
        gender: String?,
        userState: String?
    )


    fun state(  country_id: String?)
}