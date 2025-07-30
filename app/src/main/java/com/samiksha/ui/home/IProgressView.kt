package com.samiksha.ui.home

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO

interface IProgressView:IBaseView {
    fun progressMentalSkillListSuccess(model: TrainingPathModel?)
     fun getProfileSuccess(body: CheckOtpResponsePOJO?)
}