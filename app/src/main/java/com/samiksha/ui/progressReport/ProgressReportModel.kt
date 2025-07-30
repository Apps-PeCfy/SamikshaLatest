package com.samiksha.ui.progressReport

import com.google.gson.annotations.SerializedName

class ProgressReportModel {
    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    inner class DataItem {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("average")
        var average: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("rank")
        var rank: String? = null

        @SerializedName("message")
        var message: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("icon")
        var icon: String? = null

    }

    inner class Data {
        @SerializedName("learning")
        var learning: ArrayList<DataItem> = ArrayList()

        @SerializedName("training")
        var training: ArrayList<DataItem> = ArrayList()

    }


}