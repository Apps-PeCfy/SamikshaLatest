package com.samiksha.ui.search

interface ISearchPresenter {
    fun searchSkills(token: String?, searchKey: String?, page: Int?)
    fun skillDetails(token: String?, skillID: String?)
}