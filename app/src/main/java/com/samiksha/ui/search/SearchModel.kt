package com.samiksha.ui.search

import com.google.gson.annotations.SerializedName

class SearchModel {
    @SerializedName("skills")
    var skills: Skills? = null

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



        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("isSubscription")
        var isSubscription: Boolean? = false

        @SerializedName("isLearningComplete")
        var isLearningComplete: Boolean? = false

        @SerializedName("isTrainingComplete")
        var isTrainingComplete: Boolean? = false

        @SerializedName("isFavorite")
        var isFavorite: Boolean = false

        @SerializedName("id")
        var id = 0

        @SerializedName("favoriteId")
        var favoriteId :String? = null

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

    inner class Skills {
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


}