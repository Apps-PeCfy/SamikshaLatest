package com.samiksha.ui.sportPsychology.session.pojo

import com.google.gson.annotations.SerializedName

class VendorResponsePOJO {
    @SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("message")
    val message: String? = null

    inner class DataItem {
        @SerializedName("last_name")
        val lastName: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("title")
        val title: String? = null

        @SerializedName("first_name")
        val firstName: String? = null
    }
}