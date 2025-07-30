package com.samiksha.ui.informationScreen.chooseGoals.pojo

import com.google.gson.annotations.SerializedName
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO

class VerifyOTPResponse {
    @SerializedName("redirectTo ")
    var redirectTo: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("user")
    var user: User? = null

    @SerializedName("token")
    var token: String? = null

    override fun toString(): String {
        return "TestResponse{" +
                "redirectTo  = '" + redirectTo + '\'' +
                ",message = '" + message + '\'' +
                ",user = '" + user + '\'' +
                ",token = '" + token + '\'' +
                "}"
    }

    inner class User {
        @SerializedName("lastName")
        var lastName: String? = null

        @SerializedName("role")
        var role: String? = null

        @SerializedName("gender")
        var gender: String? = null

        @SerializedName("ifMinorGuardianContact")
        var ifMinorGuardianContact: String? = null

        @SerializedName("city")
        var city: State? = null

        @SerializedName("date_of_birth")
        var dateOfBirth: String? = null

        @SerializedName("fullPhoneNo")
        var fullPhoneNo: String? = null

        @SerializedName("profilePic")
        var profilePic: String? = null

        @SerializedName("fullName")
        var fullName: String? = null

        @SerializedName("whoIAm")
        var whoIAm: String? = null

        @SerializedName("phoneVerification")
        var phoneVerification: String? = null

        @SerializedName("academyInstitute")
        var academyInstitute: String? = null

        @SerializedName("phoneNo")
        var phoneNo: String? = null

        @SerializedName("verificationTokens")
        var verificationTokens: List<Any>? = null

        @SerializedName("firstName")
        var firstName: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id: String? = null

        @SerializedName("state")
        var state: State? = null

        @SerializedName("designation")
        var designation: String? = null

        @SerializedName("subscription")
        var subscription: Subscription? = null

        @SerializedName("totalRewards")
        var totalRewards: Int? = 0


        @SerializedName("email")
        var email: String? = null

        override fun toString(): String {
            return "User{" +
                    "lastName = '" + lastName + '\'' +
                    ",role = '" + role + '\'' +
                    ",gender = '" + gender + '\'' +
                    ",ifMinorGuardianContact = '" + ifMinorGuardianContact + '\'' +
                    ",city = '" + city + '\'' +
                    ",date_of_birth = '" + dateOfBirth + '\'' +
                    ",fullPhoneNo = '" + fullPhoneNo + '\'' +
                    ",profilePic = '" + profilePic + '\'' +
                    ",fullName = '" + fullName + '\'' +
                    ",whoIAm = '" + whoIAm + '\'' +
                    ",phoneVerification = '" + phoneVerification + '\'' +
                    ",academyInstitute = '" + academyInstitute + '\'' +
                    ",phoneNo = '" + phoneNo + '\'' +
                    ",verificationTokens = '" + verificationTokens + '\'' +
                    ",firstName = '" + firstName + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    ",state = '" + state + '\'' +
                    ",designation = '" + designation + '\'' +
                    ",email = '" + email + '\'' +
                    "}"
        }
    }

    inner class State {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("country_id")
        var countryId = 0

        override fun toString(): String {
            return "State{" +
                    "name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    ",country_id = '" + countryId + '\'' +
                    "}"
        }
    }

    inner class Subscription {
        @SerializedName("name")
        var name: String? = null


        override fun toString(): String {
            return "State{" +
                    "name = '" + name + '\'' +
                    "}"
        }
    }

}