package com.samiksha.ui.register.pojo

import com.google.gson.annotations.SerializedName

class ValidationResponsePOJO {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("errors")
    var errors: List<ErrorsItem>? =
        null

    override fun toString(): String {
        return "ValidationResponsePOJO{" +
                "message = '" + message + '\'' +
                ",errors = '" + errors + '\'' +
                "}"
    }

    inner class ErrorsItem {
        @SerializedName("message")
        var message: String? = null

        @SerializedName("key")
        var key: String? = null

        override fun toString(): String {
            return "ErrorsItem{" +
                    "message = '" + message + '\'' +
                    ",key = '" + key + '\'' +
                    "}"
        }
    }
}