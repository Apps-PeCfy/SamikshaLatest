package com.samiksha.ui.home.pojo

import com.google.gson.annotations.SerializedName

class AllCategoriesResponsePOJO {
    @SerializedName("data")
    var data: List<DataItem>? = null

    @SerializedName("dailyMotivation")
    var dailyMotivation:DailyMotivation? = null


    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "AllCategoriesResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("attribute")
        var attribute: String? = null

        @SerializedName("value")
        var value: String? = null

        @SerializedName("checked")
        var checked: Boolean = false

        override fun toString(): String {
            return "DataItem{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",attribute = '" + attribute + '\'' +
                    ",value = '" + value + '\'' +
                    "}"
        }
    }

    class DailyMotivation {
        @SerializedName("date")
        var date: String? = null

        @SerializedName("motivation")
        var motivation: String? = null

        @SerializedName("by")
        var by: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "DailyMotivation{" +
                    "date = '" + date + '\'' +
                    ",motivation = '" + motivation + '\'' +
                    ",by = '" + by + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }
}