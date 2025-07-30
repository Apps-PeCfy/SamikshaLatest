package com.samiksha.ui.drawer.mypoints.pojo

import com.google.gson.annotations.SerializedName

class MyRewardsResponsePOJO {
    @SerializedName("data")
    var data: List<DataItem>? =
        null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "MyRewardsResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("key")
        var key: String? = null

        @SerializedName("points")
        var points = 0

        override fun toString(): String {
            return "DataItem{" +
                    "key = '" + key + '\'' +
                    ",points = '" + points + '\'' +
                    "}"
        }
    }
}