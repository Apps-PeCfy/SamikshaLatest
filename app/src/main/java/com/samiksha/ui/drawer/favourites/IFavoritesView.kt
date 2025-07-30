package com.samiksha.ui.drawer.favourites

import com.samiksha.base.IBaseView
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO

interface IFavoritesView:IBaseView {
    fun categoriesSuccess(allCategoriesResponsePOJO: AllCategoriesResponsePOJO?)
    fun favoritesSkillSuccess(model: FavouritesModel?)
    fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
    fun deleteFromFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?)
}