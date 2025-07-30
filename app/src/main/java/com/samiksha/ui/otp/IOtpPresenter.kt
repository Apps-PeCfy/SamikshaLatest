package com.samiksha.ui.otp

interface IOtpPresenter {
    fun checkOtp(
        country_phone_code: String?,
        phone_no: String?,
        otp: String?
    )

    fun verifyOTP(
        type: String?,
        country_phone_code: String?,
        phone_no: String?,
        otp: String?,
        device_id: String?,
        device_type: String?,
        device_token: String?,
        country_code: String?
    )



    fun login(
        phoneNo: String?,
        type: String?,
        countryCode: String?
    )

fun resendOtp(
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
        userCurrentState: String?
    )


}