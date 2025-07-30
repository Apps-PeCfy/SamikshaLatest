package com.samiksha.ui.progressReport.progressreportweekly

import com.google.gson.annotations.SerializedName

class WeeklyReportResponsePOJO {
    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "WResponse{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class Data {
        @SerializedName("training")
        var training: List<TrainingItem>? = null

        override fun toString(): String {
            return "Data{" +
                    "training = '" + training + '\'' +
                    "}"
        }
    }

    inner class TrainingItem {
        @SerializedName("date")
        var date: String? = null

        @SerializedName("uid")
        var uid = 0

        @SerializedName("average")
        var average: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("rank")
        var rank: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("group")
        var group: String? = null

        override fun toString(): String {
            return "TrainingItem{" +
                    "date = '" + date + '\'' +
                    ",uid = '" + uid + '\'' +
                    ",average = '" + average + '\'' +
                    ",name = '" + name + '\'' +
                    ",rank = '" + rank + '\'' +
                    ",created_at = '" + createdAt + '\'' +
                    ",id = '" + id + '\'' +
                    ",group = '" + group + '\'' +
                    "}"
        }
    }
}