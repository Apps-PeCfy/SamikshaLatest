package com.samiksha.ui.home.pojo

import com.google.gson.annotations.SerializedName

class TestimonialsResponsePOJO {
    @SerializedName("testimonials")
    var testimonials: List<TestimonialsItem>? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "TestimonialsResponsePOJO{" +
                "testimonials = '" + testimonials + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class TestimonialsItem {
        @SerializedName("user_name")
        var userName: String? = null

        @SerializedName("review")
        var review: String? = null

        @SerializedName("by")
        var by: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("user_profile_pic")
        var userProfilePic: String? = null

        @SerializedName("sport")
        var sport: Sport? = null


        override fun toString(): String {
            return "TestimonialsItem{" +
                    "user_name = '" + userName + '\'' +
                    ",review = '" + review + '\'' +
                    ",id = '" + id + '\'' +
                    ",user_profile_pic = '" + userProfilePic + '\'' +
                    "}"
        }
    }


    inner class Sport {
        @SerializedName("name")
        var name: String? = null
    }


    }