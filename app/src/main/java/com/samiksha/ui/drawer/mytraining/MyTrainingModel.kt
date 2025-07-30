package com.samiksha.ui.drawer.mytraining

import com.google.gson.annotations.SerializedName

class MyTrainingModel {
    @SerializedName("my_activity_list")
    var my_activity_list: MyActivity? = null

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

        @SerializedName("my_activity_id")
        var my_activity_id = 0

        @SerializedName("favoriteId")
        var favoriteId :String? = null

        @SerializedName("time")
        var time: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("created_at")
        var created_at: String? = null

    }

    inner class MyActivity {
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