package com.samiksha.ui.search

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO

interface ISearchView:IBaseView {
    fun searchSkillSuccess(model: SearchModel?)
    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
}