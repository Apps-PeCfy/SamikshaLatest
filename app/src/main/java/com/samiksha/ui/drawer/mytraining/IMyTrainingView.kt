package com.samiksha.ui.drawer.mytraining

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO

interface IMyTrainingView:IBaseView {
    fun getMyActivitiesSuccess(model: MyTrainingModel?)
    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
}