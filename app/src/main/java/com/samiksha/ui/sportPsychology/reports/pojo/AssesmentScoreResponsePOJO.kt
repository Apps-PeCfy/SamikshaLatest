package com.samiksha.ui.sportPsychology.reports.pojo

import com.google.gson.annotations.SerializedName

class AssesmentScoreResponsePOJO {
    @SerializedName("scores")
    var scores: Scores? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "AssesmentScoreResponsePOJO{" +
                "scores = '" + scores + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class Scores {
        @SerializedName("path")
        var path: String? = null

        @SerializedName("lastPageUrl")
        var lastPageUrl: String? = null

        @SerializedName("total")
        var total = 0

        @SerializedName("firstPageUrl")
        var firstPageUrl: String? = null

        @SerializedName("nextPageUrl")
        var nextPageUrl: Any? = null

        @SerializedName("perPage")
        var perPage = 0

        @SerializedName("data")
        var data: List<DataItem>? =
            null

        @SerializedName("lastPage")
        var lastPage = 0

        @SerializedName("currentPage")
        var currentPage = 0

        @SerializedName("prevPageUrl")
        var prevPageUrl: Any? = null

        override fun toString(): String {
            return "Scores{" +
                    "path = '" + path + '\'' +
                    ",lastPageUrl = '" + lastPageUrl + '\'' +
                    ",total = '" + total + '\'' +
                    ",firstPageUrl = '" + firstPageUrl + '\'' +
                    ",nextPageUrl = '" + nextPageUrl + '\'' +
                    ",perPage = '" + perPage + '\'' +
                    ",data = '" + data + '\'' +
                    ",lastPage = '" + lastPage + '\'' +
                    ",currentPage = '" + currentPage + '\'' +
                    ",prevPageUrl = '" + prevPageUrl + '\'' +
                    "}"
        }
    }

    inner class DataItem {
        @SerializedName("score")
        var score = 0

        @SerializedName("url_link")
        var urlLink: String? = null

        @SerializedName("pdf")
        var pdf: String? = null

        @SerializedName("craetedAt")
        var craetedAt: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("iconUrl")
        var iconUrl: String? = null

        @SerializedName("email")
        var email: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "score = '" + score + '\'' +
                    ",url_link = '" + urlLink + '\'' +
                    ",pdf = '" + pdf + '\'' +
                    ",craetedAt = '" + craetedAt + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    ",iconUrl = '" + iconUrl + '\'' +
                    ",email = '" + email + '\'' +
                    "}"
        }
    }
}