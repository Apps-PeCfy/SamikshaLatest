package com.samiksha.ui.sportPsychology.session.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GoalResponsePOJO {
    @SerializedName("data")
    var data: List<DataItem>? = null

    @SerializedName("message")
    var message: String? = null




    inner class DataItem: Serializable {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("id")
        var id = 0
    }
}