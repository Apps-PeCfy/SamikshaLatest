package com.samiksha.ui.progressReport

interface IProgressReportPresenter {

    fun getReportSummary(token: String?, skillID: String?,selectedDate:String?)
    fun getReportSummaryWithID(token: String?, userId:String?,skillID: String?,selectedDate:String?)
}