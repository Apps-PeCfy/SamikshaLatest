package com.samiksha.ui.sportPsychology.session.pojo

import com.google.gson.annotations.SerializedName

class SessionResponsePOJO {
    @SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("message")
    val message: String? = null


    class DataItem {
        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0
    }
}