package com.samiksha.ui.otp

import com.samiksha.base.IBaseView
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO

interface IOtpView : IBaseView {
    fun otpSuccess(body: CheckOtpResponsePOJO?)
    fun verifyotpSuccess(body: CheckOtpResponsePOJO?)
    fun validationError(validationResponse: ValidationResponsePOJO?)


    fun onLoginSUccess(loginResponsepojo: LoginResponcePOJO?)
    fun onRegisterSuccess(registerResponsePOJO: RegisterResponsePOJO?)

}