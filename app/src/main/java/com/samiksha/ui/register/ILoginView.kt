package com.samiksha.ui.register

import com.samiksha.base.IBaseView
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO


interface ILoginView : IBaseView {
    fun onLoginSUccess(loginResponsepojo:LoginResponcePOJO?)
    fun onRegisterSuccess(registerResponsePOJO: RegisterResponsePOJO?)
    fun validationError(validationResponse: ValidationResponsePOJO?)
    fun validationErrorEmpty(position:Int?)

    fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?)


}