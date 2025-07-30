package com.samiksha.ui.home.dealingWithDistraction.pojo

import com.google.gson.annotations.SerializedName

class SubscriptionModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("subscriptions")
    var subscriptions: ArrayList<Subscriptions> = ArrayList()

    inner class Subscriptions {
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("plan_duration")
        var planDuration: Int? = null

        @SerializedName("regular_price")
        var regularPrice: String? = null

        @SerializedName("selling_price")
        var sellingPrice: String? = null

        @SerializedName("selected")
        var selected: Boolean = false

        @SerializedName("description")
        var description: String? = null

        @SerializedName("is_active")
        var is_active: Boolean = false
    }
}