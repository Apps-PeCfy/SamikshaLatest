package com.samiksha.ui.infoAfterLearning.SportInformation

import com.samiksha.base.IBaseView
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO

interface ISportInformationView : IBaseView {
    fun submitAnswerSuccess(body: SportInformationModel?)

    fun updateSkillStatus(body: SportInformationModel?)

    fun addToMyTrainingSuccess(body: SportInformationModel?)

    fun addToTrainingSchedule(body: SportInformationModel?)

    fun updateTrainingSchedule(body: SportInformationModel?)
    fun progressQuizSuccess(body: OnlyMessageResponsePOJO?)
}