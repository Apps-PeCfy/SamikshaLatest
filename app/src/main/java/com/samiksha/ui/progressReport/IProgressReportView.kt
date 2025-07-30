package com.samiksha.ui.progressReport

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.main.TrainingPathModel

interface IProgressReportView:IBaseView {
    fun reportSummarySuccess(model: ProgressReportModel?)
}