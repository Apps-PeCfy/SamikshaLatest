package com.samiksha.ui.drawer.mytraining

interface IMyTrainingPresenter {
    fun getMyActivities(token: String?, startDate: String?, endDate: String?, page: Int?)
    fun skillDetails(token: String?, skillID: String?)

}