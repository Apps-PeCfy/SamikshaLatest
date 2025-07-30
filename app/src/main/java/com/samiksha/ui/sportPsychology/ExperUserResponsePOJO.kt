package com.samiksha.ui.sportPsychology

import com.google.gson.annotations.SerializedName


class ExperUserResponsePOJO {
    @SerializedName("data")
    val data: Data? = null

    @SerializedName("message")
    val message: String? = null

    inner class Data {
        @SerializedName("counsellors")
        val counsellors: List<CounsellorsItem>? = null

        @SerializedName("users")
        val users: List<UsersItem>? = null
    }

    inner class CounsellorsItem {
        @SerializedName("lastName")
        val lastName: String? = null

        @SerializedName("role")
        val role: String? = null

        @SerializedName("gender")
        val gender: String? = null

        @SerializedName("city")
        val city: City? = null

        @SerializedName("date_of_birth")
        val dateOfBirth: String? = null

        @SerializedName("fullPhoneNo")
        val fullPhoneNo: String? = null

        @SerializedName("subscription")
        val subscription: Subscription? = null

        @SerializedName("phoneNo")
        val phoneNo: String? = null

        @SerializedName("countryPhoneCode")
        val countryPhoneCode: String? = null

        @SerializedName("countryCode")
        val countryCode: String? = null

        @SerializedName("referralCode")
        val referralCode: Any? = null

        @SerializedName("counsellingSessionPaymentStatus")
        val counsellingSessionPaymentStatus: String? = null

        @SerializedName("id")
        val id: String? = null

        @SerializedName("state")
        val state: State? = null

        @SerializedName("email")
        val email: String? = null

        @SerializedName("goals")
        val goals: List<Any>? = null

        @SerializedName("ifMinorGuardianContact")
        val ifMinorGuardianContact: String? = null

        @SerializedName("profilePic")
        val profilePic: String? = null

        @SerializedName("professionalLevel")
        val professionalLevel: ProfessionalLevel? = null

        @SerializedName("languageId")
        val languageId = 0

        @SerializedName("fullName")
        val fullName: String? = null

        @SerializedName("whoIAm")
        val whoIAm: String? = null

        @SerializedName("phoneVerification")
        val phoneVerification: String? = null

        @SerializedName("academyInstitute")
        val academyInstitute: String? = null

        @SerializedName("verificationTokens")
        val verificationTokens: List<Any>? = null

        @SerializedName("totalRewards")
        val totalRewards = 0

        @SerializedName("firstName")
        val firstName: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("userType")
        val userType: String? = null

        @SerializedName("designation")
        val designation: String? = null

        @SerializedName("sport")
        val sport: Sport? = null
    }

    class UsersItem {
        @SerializedName("lastName")
        val lastName: String? = null

        @SerializedName("role")
        val role: String? = null

        @SerializedName("gender")
        val gender: String? = null

        @SerializedName("city")
        val city: City? = null

        @SerializedName("date_of_birth")
        val dateOfBirth: String? = null

        @SerializedName("fullPhoneNo")
        val fullPhoneNo: String? = null

        @SerializedName("subscription")
        val subscription: Subscription? = null

        @SerializedName("phoneNo")
        val phoneNo: String? = null

        @SerializedName("countryPhoneCode")
        val countryPhoneCode: String? = null

        @SerializedName("countryCode")
        val countryCode: String? = null

        @SerializedName("referralCode")
        val referralCode: Any? = null

        @SerializedName("counsellingSessionPaymentStatus")
        val counsellingSessionPaymentStatus: String? = null

        @SerializedName("id")
        val id: String? = null

        @SerializedName("state")
        val state: State? = null

        @SerializedName("email")
        val email: String? = null

        @SerializedName("goals")
        val goals: List<GoalsItem>? = null

        @SerializedName("ifMinorGuardianContact")
        val ifMinorGuardianContact: String? = null

        @SerializedName("profilePic")
        val profilePic: String? = null

        @SerializedName("professionalLevel")
        val professionalLevel: ProfessionalLevel? = null

        @SerializedName("languageId")
        val languageId = 0

        @SerializedName("fullName")
        val fullName: String? = null

        @SerializedName("whoIAm")
        val whoIAm: String? = null

        @SerializedName("phoneVerification")
        val phoneVerification: String? = null

        @SerializedName("academyInstitute")
        val academyInstitute: String? = null

        @SerializedName("verificationTokens")
        val verificationTokens: List<Any>? = null

        @SerializedName("totalRewards")
        val totalRewards = 0

        @SerializedName("firstName")
        val firstName: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("userType")
        val userType: String? = null

        @SerializedName("designation")
        val designation: String? = null

        @SerializedName("sport")
        val sport: Sport? = null
    }


    class State {
        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("country_id")
        val countryId = 0
    }

    class Sport {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("status")
        val status: String? = null
    }

    class ProfessionalLevel {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0
    }

    class GoalsItem {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0
    }

    class City {
        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("state_id")
        val stateId = 0
    }

    class Subscription {
        @SerializedName("duration")
        val duration: String? = null

        @SerializedName("price")
        val price: String? = null

        @SerializedName("expiry_date")
        val expiryDate: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("id")
        val id = 0
    }
}