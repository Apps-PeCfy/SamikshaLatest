package com.samiksha.ui.sportPsychology

import com.google.gson.annotations.SerializedName

class UserResponsePOJO {
    @SerializedName("data")
    var data: ArrayList<DataItem>? =
        null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "UserResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("lastName")
        var lastName: String? = null

        @SerializedName("role")
        var role: String? = null

        @SerializedName("gender")
        var gender: String? = null

        @SerializedName("city")
        var city: State? = null

        @SerializedName("registerDate")
        var registerDate: String? = null


        @SerializedName("date_of_birth")
        var dateOfBirth: String? = null

        @SerializedName("fullPhoneNo")
        var fullPhoneNo: String? = null

        @SerializedName("subscription")
        var subscription: Subscription? =
            null

        @SerializedName("phoneNo")
        var phoneNo: String? = null

        @SerializedName("referralCode")
        var referralCode: Any? = null

        @SerializedName("id")
        var id: String? = null

        @SerializedName("state")
        var state: State? = null


        @SerializedName("goals")
        var goals: List<GoalsItem>? = null


        @SerializedName("email")
        var email: String? = null

        @SerializedName("ifMinorGuardianContact")
        var ifMinorGuardianContact: String? = null

        @SerializedName("profilePic")
        var profilePic: String? = null

        @SerializedName("professionalLevel")
        var professionalLevel: ProfessionalLevel? =
            null

        @SerializedName("languageId")
        var languageId = 0

        @SerializedName("fullName")
        var fullName: String? = null

        @SerializedName("whoIAm")
        var whoIAm: String? = null

        @SerializedName("phoneVerification")
        var phoneVerification: String? = null

        @SerializedName("academyInstitute")
        var academyInstitute: String? = null

        @SerializedName("verificationTokens")
        var verificationTokens: List<Any>? = null

        @SerializedName("totalRewards")
        var totalRewards = 0

        @SerializedName("firstName")
        var firstName: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("designation")
        var designation: String? = null

        @SerializedName("sport")
        var sport: Sport? = null

        override fun toString(): String {
            return "DataItem{" +
                    "lastName = '" + lastName + '\'' +
                    ",role = '" + role + '\'' +
                    ",gender = '" + gender + '\'' +
                    ",city = '" + city + '\'' +
                    ",date_of_birth = '" + dateOfBirth + '\'' +
                    ",fullPhoneNo = '" + fullPhoneNo + '\'' +
                    ",subscription = '" + subscription + '\'' +
                    ",phoneNo = '" + phoneNo + '\'' +
                    ",referralCode = '" + referralCode + '\'' +
                    ",id = '" + id + '\'' +
                    ",state = '" + state + '\'' +
                    ",email = '" + email + '\'' +
                    ",ifMinorGuardianContact = '" + ifMinorGuardianContact + '\'' +
                    ",profilePic = '" + profilePic + '\'' +
                    ",professionalLevel = '" + professionalLevel + '\'' +
                    ",languageId = '" + languageId + '\'' +
                    ",fullName = '" + fullName + '\'' +
                    ",whoIAm = '" + whoIAm + '\'' +
                    ",phoneVerification = '" + phoneVerification + '\'' +
                    ",academyInstitute = '" + academyInstitute + '\'' +
                    ",verificationTokens = '" + verificationTokens + '\'' +
                    ",totalRewards = '" + totalRewards + '\'' +
                    ",firstName = '" + firstName + '\'' +
                    ",name = '" + name + '\'' +
                    ",designation = '" + designation + '\'' +
                    ",sport = '" + sport + '\'' +
                    "}"
        }
    }

    inner class ProfessionalLevel {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "ProfessionalLevel{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }

    inner class Sport {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "Sport{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
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
        @SerializedName("duration")
        var duration: String? = null

        @SerializedName("price")
        var price: String? = null

        @SerializedName("expiry_date")
        var expiryDate: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "Subscription{" +
                    "duration = '" + duration + '\'' +
                    ",price = '" + price + '\'' +
                    ",expiry_date = '" + expiryDate + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }

    class GoalsItem {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "GoalsItem{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }
}