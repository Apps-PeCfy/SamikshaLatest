package com.samiksha.ui.sportPsychology.session

import com.google.gson.annotations.SerializedName

class OnlyMessageResponsePOJO {
    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "OnlyMessageResponsePOJO{" +
                "message = '" + message + '\'' +
                "}"
    }
}