package com.samiksha.ui.register.pojo

import com.google.gson.annotations.SerializedName

class LoginResponcePOJO {
    @SerializedName("redirectTo")
    var redirectTo: String? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "LoginResponcePOJO{" +
                "redirectTo = '" + redirectTo + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }
}