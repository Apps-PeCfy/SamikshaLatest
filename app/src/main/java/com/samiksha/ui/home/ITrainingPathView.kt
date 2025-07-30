package com.samiksha.ui.home

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO

interface ITrainingPathView:IBaseView {
    fun trainingMentalSkillListSuccess(model: TrainingPathModel?)
    fun deleteScheduleSuccess(model: TrainingPathModel?)
    fun repeatScheduleSuccess(model: TrainingPathModel?)
    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
     fun getProfileSuccess(body: CheckOtpResponsePOJO?)
}