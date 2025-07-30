package com.samiksha.ui.drawer.settings.pojo

import com.google.gson.annotations.SerializedName

class SettingResponsePojo {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("language_list")
    var languageList: LanguageList? = null

    override fun toString(): String {
        return "SettingResponsePojo{" +
                "message = '" + message + '\'' +
                ",language_list = '" + languageList + '\'' +
                "}"
    }



    class LanguageList {
        @SerializedName("data")
        var data: List<DataItem>? = null

        override fun toString(): String {
            return "LanguageList{" +
                    "data = '" + data + '\'' +
                    "}"
        }
    }


    class DataItem {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "DataItem{" +
                    "name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }
}