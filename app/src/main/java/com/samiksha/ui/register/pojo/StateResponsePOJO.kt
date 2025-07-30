package com.samiksha.ui.register.pojo

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class StateResponsePOJO {
    @SerializedName("data")
    var data: ArrayList<DataItem>? =
        null

    @SerializedName("message")
    var message: String? = null


    override fun toString(): String {
        return "StateResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("country_id")
        var countryId = 0

        override fun toString(): String {
            return name.toString()
        }
    }
}