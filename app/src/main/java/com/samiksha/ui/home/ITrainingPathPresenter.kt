package com.samiksha.ui.home

interface ITrainingPathPresenter {

    fun trainingSkillList(token: String?, startDate: String?)
    fun trainingSkillListNext(token: String?, startDate: String?,pageNumber:Int?)
    fun deleteSchedule(token: String?, scheduleID: String?)
    fun repeatSchedule(token: String?, scheduleID: String?)
    fun skillDetails(token: String?, skillID: String?)
    fun updateRegisterDate(token: String?)
}