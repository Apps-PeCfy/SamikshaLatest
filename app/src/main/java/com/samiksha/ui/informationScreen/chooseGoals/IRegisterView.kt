package com.samiksha.ui.informationScreen.chooseGoals

import com.samiksha.base.IBaseView
import com.samiksha.ui.informationScreen.chooseGoals.pojo.VerifyOTPResponse
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO

interface IRegisterView :IBaseView{
    fun onRegisterSuccess(body: CheckOtpResponsePOJO?)
    fun onUpdateWebUserSuccess(body: CheckOtpResponsePOJO?)
    fun onUpdateUserSuccess(body: VerifyOTPResponse?)
    fun validationError(validationResponse: ValidationResponsePOJO?)
    fun validationErrorEmpty(position:Int?)


    fun onQuestionSuccess(questionResponsePOJO: QuestionResponsePOJO?)


}