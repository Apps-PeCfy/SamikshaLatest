package com.samiksha.ui.home.main

import com.google.gson.annotations.SerializedName

class TrainingPathModel {
    @SerializedName("mental_skill_schedule_list")
    var mentalSkills: MentalSkillSchedule? = null

    //For Progress Module
    @SerializedName("progress_report_list")
    var progress_report_list: MentalSkillSchedule? = null



    @SerializedName("message")
    var message: String? = null

    inner class DataItem {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("reviews")
        var reviews: String? = null

        @SerializedName("module_type")
        var moduleType: String? = null

        @SerializedName("gaols")
        var gaols: List<String>? = null

        @SerializedName("days")
        var days: ArrayList<ScheduleDays> = ArrayList()

        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("schedule_id")
        var schedule_id = 0

        @SerializedName("time")
        var time: String? = null

        @SerializedName("type")
        var type: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "image = '" + image + '\'' +
                    ",reviews = '" + reviews + '\'' +
                    ",module_type = '" + moduleType + '\'' +
                    ",name = '" + name + '\'' +
                    ",description = '" + description + '\'' +
                    ",id = '" + id + '\'' +
                    ",time = '" + time + '\'' +
                    ",type = '" + type + '\'' +
                    "}"
        }
    }

    inner class MentalSkillSchedule {
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
        var data: ArrayList<DataItem> = ArrayList()

        @SerializedName("lastPage")
        var lastPage = 0

        @SerializedName("currentPage")
        var currentPage = 0

        @SerializedName("prevPageUrl")
        var prevPageUrl: Any? = null
    }

    inner class ScheduleDays {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("flag")
        var flag: String? = null

        @SerializedName("is_complete")
        var is_complete : Boolean = false

    }



}