package com.samiksha.ui.home.pojo

import com.google.gson.annotations.SerializedName

class AppVersionPOJO {
    @SerializedName("data")
    var data: DataItem? = null

    @SerializedName("message")
    var message: String? = null



    inner class DataItem {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("version")
        var version: String? = null

        @SerializedName("version_code")
        var version_code: String? = null

        @SerializedName("is_mandatory")
        var is_mandatory: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("message")
        var message: String? = null

    }





    }