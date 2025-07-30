package com.samiksha.ui.progressReport

import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.main.TrainingPathModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressReportImplementer(var reportView: IProgressReportView) :
    IProgressReportPresenter  {
    override fun getReportSummary(token: String?, skillID: String?,selectedDate:String?) {
        reportView.showWait()
        val call = ApiClient.client.progressReportSummary("Bearer $token", skillID,selectedDate)
        call!!.enqueue(object : Callback<ProgressReportModel?> {
            override fun onResponse(
                call: Call<ProgressReportModel?>,
                response: Response<ProgressReportModel?>
            ) {

                if (response.code() == 200) {
                    reportView.removeWait()
                    reportView.reportSummarySuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    reportView.removeWait()
                    reportView.onFailure(response.message())

                }else{
                    reportView.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<ProgressReportModel?>,
                t: Throwable
            ) {
                reportView.removeWait()
                reportView.onFailure(t.message)
            }
        })
    }




    override fun getReportSummaryWithID(token: String?, userId:String?,skillID: String?,selectedDate:String?) {
        reportView.showWait()
        val call = ApiClient.client.progressReportSummaryWithID("Bearer $token", userId,skillID,selectedDate)
        call!!.enqueue(object : Callback<ProgressReportModel?> {
            override fun onResponse(
                call: Call<ProgressReportModel?>,
                response: Response<ProgressReportModel?>
            ) {

                if (response.code() == 200) {
                    reportView.removeWait()
                    reportView.reportSummarySuccess(response.body())
                } else if (response.code() == 400||response.code()==422) {
                    reportView.removeWait()
                    reportView.onFailure(response.message())

                }else{
                    reportView.onFailure(response.message())

                }
            }

            override fun onFailure(
                call: Call<ProgressReportModel?>,
                t: Throwable
            ) {
                reportView.removeWait()
                reportView.onFailure(t.message)
            }
        })
    }



}