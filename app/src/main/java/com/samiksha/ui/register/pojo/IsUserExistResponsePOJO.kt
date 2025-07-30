package com.samiksha.ui.register.pojo

import com.google.gson.annotations.SerializedName

class IsUserExistResponsePOJO {


    @SerializedName("isUserExist ")
    var isUserExist: Boolean = false

    @SerializedName("message")
    var message: String? = null

}