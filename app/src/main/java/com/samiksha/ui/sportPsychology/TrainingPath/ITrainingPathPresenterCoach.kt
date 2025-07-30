package com.samiksha.ui.sportPsychology.TrainingPath

interface ITrainingPathPresenterCoach {

    fun trainingSkillList(token: String?, id:String?,startDate: String?)
    fun deleteSchedule(token: String?, scheduleID: String?)
    fun repeatSchedule(token: String?, scheduleID: String?)
    fun skillDetails(token: String?, skillID: String?)
    fun updateRegisterDate(token: String?)


}