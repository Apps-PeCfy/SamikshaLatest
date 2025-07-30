package com.samiksha.ui.home.main

interface IAccountPresenter {
    fun stateList(countryId: String?)
    fun cityList(stateId: String?)
    fun getProfile(token: String?)
}