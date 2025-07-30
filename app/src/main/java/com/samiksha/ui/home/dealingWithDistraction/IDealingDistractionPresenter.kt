package com.samiksha.ui.home.dealingWithDistraction

interface IDealingDistractionPresenter {

    fun skillDetails(
        token: String?,
        skillID: String?)

    fun subscriptionList(
        token: String?)

    fun state(  country_id: String?)

    fun updateSubscriptions(
        token: String?,
        subscriptionId: String?)

    fun createOrder(
        token: String?,
        context_type: String?,
        context_id: String?,
        item_name: String?,
        gst_no: String?,
        total_amount: Double?)
}
