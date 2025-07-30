package com.samiksha.ui.home

interface IHomePresenter {
    fun allcategories(token: String?)
    fun allskills(token: String?)
    fun state(state: String?)
    fun allTestimonials(token: String?)
    fun yogaSelected(token: String?,yoga: String?)
    fun selectedCategories(token: String?, value: String?)
    fun skillDetails(
        token: String?,
        skillID: String?)
    fun addToFavorites(
        token: String?,
        context_type: String?,
        context_id: String?)

    fun deleteFromFavorites(
        token: String?,
        favorite_id: String?)

}