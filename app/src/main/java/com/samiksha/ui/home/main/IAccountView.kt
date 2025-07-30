package com.samiksha.ui.home.main

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO

interface IAccountView:IBaseView {
    fun stateListSuccess(model: StateResponsePOJO?)
    fun cityListSuccess(model: StateResponsePOJO?)
    fun getProfileSuccess(model: CheckOtpResponsePOJO?)
}