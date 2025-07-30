package com.samiksha.ui.home.main

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class TrainingCalenderModel : Serializable {
    @SerializedName("date")
    var date: Date? = null

    @SerializedName("month")
    var month: String? = null

    @SerializedName("day")
    var day: String? = null

    @SerializedName("dayDate")
    var dayDate: String? = null

    @SerializedName("monthYear")
    var monthYear: String? = null

    @SerializedName("selected")
    var selected: Boolean = false
}