package com.samiksha.ui.home

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.home.pojo.CategoriesResponsePOJO
import com.samiksha.ui.home.pojo.TestimonialsResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO

interface IHomeView:IBaseView {
    fun categoriesSuccess(allCategoriesResponsePOJO: AllCategoriesResponsePOJO?)
    fun allSkillSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?)
    fun selectedCategoriesSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?)
    fun yogaSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?)
    fun testimonialsSuccess(testimonialsResponsePOJO: TestimonialsResponsePOJO?)
    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
    fun addFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
    fun deleteFromFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
    fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?)


}