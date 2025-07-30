package com.samiksha.ui.home.dealingWithDistraction.pojo

import com.google.gson.annotations.SerializedName

class SkillDetailsResponsePOJO {
    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "SkillDetailsResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class Data {

        @SerializedName("benefits")
        var benefits: List<String> = ArrayList()


        @SerializedName("image")
        var image: String? = null

        @SerializedName("module_type")
        var moduleType: String? = null

        @SerializedName("reviews")
        var reviews: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("favoriteId")
        var favoriteId :String? = null

        @SerializedName("time")
        var time: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("isSubscription")
        var isSubscription: Boolean? = false

        @SerializedName("isAccessible")
        var isAccessible: Boolean? = false

        @SerializedName("isLearningComplete")
        var isLearningComplete: Boolean? = false

        @SerializedName("isTrainingComplete")
        var isTrainingComplete: Boolean? = false

        @SerializedName("isFavorite")
        var isFavorite: Boolean = false


        @SerializedName("subSkiils")
        var subSkiils: List<SubSkiilsItem>? = null

        override fun toString(): String {
            return "Data{" +
                    "image = '" + image + '\'' +
                    ",moduleType = '" + moduleType + '\'' +
                    ",reviews = '" + reviews + '\'' +
                    ",name = '" + name + '\'' +
                    ",description = '" + description + '\'' +
                    ",id = '" + id + '\'' +
                    ",time = '" + time + '\'' +
                    ",type = '" + type + '\'' +
                    ",subSkiils = '" + subSkiils + '\'' +
                    "}"
        }
    }

    inner class DataItem {
        @SerializedName("duration")
        var duration: String? = null

        @SerializedName("file")
        var file: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("mental_skill_id")
        var mental_skill_id: String? = null

        @SerializedName("rank")
        var rank: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("benefits")
        var benefits: List<String> = ArrayList()



        @SerializedName("language")
        var language: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("type")
        var type: String? = null

        @SerializedName("group")
        var group: String? = null

        @SerializedName("SubSkillAnswers")
        var subSkillAnswers: List<SubSkillAnswersModel>? = null

        override fun toString(): String {
            return "DataItem{" +
                    "duration = '" + duration + '\'' +
                    ",file = '" + file + '\'' +
                    ",name = '" + name + '\'' +
                    ",rank = '" + rank + '\'' +
                    ",description = '" + description + '\'' +
                    ",language = '" + language + '\'' +
                    ",id = '" + id + '\'' +
                    ",type = '" + type + '\'' +
                    ",group = '" + group + '\'' +
                    ",subSkillAnswers = '" + subSkillAnswers + '\'' +
                    "}"
        }
    }

    inner class SubSkiilsItem {
        @SerializedName("data")
        var data: List<DataItem>? =
            null

        @SerializedName("name")
        var name: String? = null

        override fun toString(): String {
            return "SubSkiilsItem{" +
                    "data = '" + data + '\'' +
                    ",name = '" + name + '\'' +
                    "}"
        }
    }

    inner class SubSkillAnswersModel {
        @SerializedName("id")
        var id = 0

        @SerializedName("answer")
        var answer: String? = null

        @SerializedName("icon")
        var icon: String? = null

        @SerializedName("weightage")
        var weightage: String? = null

        @SerializedName("answerFeedback")
        var answerFeedback: String? = null

        @SerializedName("checked")
        var checked: Boolean = false

    }
}