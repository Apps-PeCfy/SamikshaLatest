package com.samiksha.ui.informationScreen.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QuestionResponsePOJO {
    @SerializedName("questions")
    var questions: List<QuestionsItem>? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "QuestionResponsePOJO{" +
                "questions = '" + questions + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class QuestionsItem : Serializable {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("options")
        var options: List<OptionsItem>? =
            null

        override fun toString(): String {
            return "QuestionsItem{" +
                    "name = '" + name + '\'' +
                    ",options = '" + options + '\'' +
                    "}"
        }
    }

    inner class OptionsItem:Serializable {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("isSelected")
        var isSelected: String? = "N"

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "OptionsItem{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }
}