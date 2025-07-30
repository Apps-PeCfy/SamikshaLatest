package com.samiksha.ui.drawer.favourites

interface IFavoritesPresenter {
    fun allCategories(token: String?)
    fun favoritesSkills(token: String?, category: String?, page: Int?)
    fun skillDetails(token: String?, skillID: String?)
    fun deleteFromFavorites(token: String?, favorite_id: String?)
}