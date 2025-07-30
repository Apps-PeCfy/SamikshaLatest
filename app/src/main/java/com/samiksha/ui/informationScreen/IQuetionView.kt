package com.samiksha.ui.informationScreen

import com.samiksha.base.IBaseView
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO

interface IQuetionView: IBaseView {

    fun onQuestionSuccess(questionResponsePOJO: QuestionResponsePOJO?)
}