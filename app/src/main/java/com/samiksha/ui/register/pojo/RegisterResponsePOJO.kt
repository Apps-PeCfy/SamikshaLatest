package com.samiksha.ui.register.pojo

class RegisterResponsePOJO {
    private var phoneResponce: PhoneResponce? = null
    var redirectTo: String? = null
    var message: String? = null
    fun setPhoneResponce(phoneResponce: PhoneResponce?) {
        this.phoneResponce = phoneResponce
    }

    fun getPhoneResponce(): PhoneResponce? {
        return phoneResponce
    }

    override fun toString(): String {
        return "RegisterResponsePOJO{" +
                "phone_responce = '" + phoneResponce + '\'' +
                ",redirectTo = '" + redirectTo + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }


    class PhoneResponce {
        var type: String? = null
        var requestId: String? = null

        override fun toString(): String {
            return "PhoneResponce{" +
                    "type = '" + type + '\'' +
                    ",request_id = '" + requestId + '\'' +
                    "}"
        }
    }
}