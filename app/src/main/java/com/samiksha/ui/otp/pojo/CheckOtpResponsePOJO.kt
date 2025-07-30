package com.samiksha.ui.otp.pojo

import com.google.gson.annotations.SerializedName

class CheckOtpResponsePOJO {
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

        @SerializedName("switchUserCountryCode")
        var switchUserCountryCode: String? = null

        @SerializedName("switchUserMobile")
        var switchUserMobile: String? = null

        @SerializedName("registerDate")
        var registerDate: String? = null

        @SerializedName("countryPhoneCode")
        var countryPhoneCode: String? = null

        @SerializedName("userType")
        var userType: String? = null

        @SerializedName("countryCode")
        var countryCode: String? = null

        @SerializedName("ifMinorGuardianContact")
        var ifMinorGuardianContact: String? = null

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

        @SerializedName("city")
        var city: State? = null

        @SerializedName("designation")
        var designation: String? = null

        @SerializedName("subscription")
        var subscription: Subscription? = null

        @SerializedName("sport")
        var sport: Sport? = null

        @SerializedName("professionalLevel")
        var professionalLevel: ProfessionalLevel? = null

        @SerializedName("totalRewards")
        var totalRewards: Int? = 0

        @SerializedName("languageId")
        var languageId: Int? = 1

        @SerializedName("email")
        var email: String? = null

        @SerializedName("referralCode")
        var referralCode: String? = null

        @SerializedName("counsellingSessionPaymentStatus")
        var counsellingSessionPaymentStatus: String? = null

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

        @SerializedName("state_id")
        var stateId = 0

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

    inner class Sport {
        @SerializedName("name")
        var name: String? = null


        override fun toString(): String {
            return "State{" +
                    "name = '" + name + '\'' +
                    "}"
        }
    }

    inner class ProfessionalLevel {
        @SerializedName("name")
        var name: String? = null


        override fun toString(): String {
            return "State{" +
                    "name = '" + name + '\'' +
                    "}"
        }
    }

}