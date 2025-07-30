package com.samiksha.ui.home.pojo

import com.google.gson.annotations.SerializedName

class CategoriesResponsePOJO {
    @SerializedName("mental_skills")
    var mentalSkills: MentalSkills? = null

    @SerializedName("more_skills")
    var moreSkills: MoreSkills? = null

    @SerializedName("my_activity_list ")
    var myActivityList: MyActivityList? = null


    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "CategoriesResponsePOJO{" +
                "mental_skills = '" + mentalSkills + '\'' +
                ",more_skills = '" + moreSkills + '\'' +
                ",my_activity_list = '" + myActivityList + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("reviews")
        var reviews: String? = null

        @SerializedName("module_type")
        var moduleType: String? = null

        @SerializedName("gaols")
        var gaols: List<String>? = null

        @SerializedName("isFavorite")
        var isFavorite: Boolean = false

        @SerializedName("isSubscription")
        var isSubscription: Boolean = false

        @SerializedName("isAccessible")
        var isAccessible: Boolean = false


        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("favoriteId")
        var favoriteId: String? = null


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

    inner class MentalSkills {
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
            return "MentalSkills{" +
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

    inner class MoreSkills {
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
            return "MoreSkills{" +
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

    inner class MyActivityList {
        @SerializedName("data")
        var data: List<DataItem>? =
            null

    }
}