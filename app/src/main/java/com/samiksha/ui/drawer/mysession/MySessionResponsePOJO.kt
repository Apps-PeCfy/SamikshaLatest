package com.samiksha.ui.drawer.mysession

import com.google.gson.annotations.SerializedName

class MySessionResponsePOJO {
    @SerializedName("data")
    var data: List<DataItem>? =
        null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "MySessionResponsePOJO{" +
                "data = '" + data + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class DataItem {
        @SerializedName("subUserSchedules")
        var subUserSchedules: List<SubUserSchedulesItem>? = null


        @SerializedName("recommendationCount")
        var recommendationCount = 0

        @SerializedName("item_id")
        var itemId = 0

        @SerializedName("endDate")
        var endDate: Any? = null

        @SerializedName("image_url")
        var imageUrl: Any? = null

        @SerializedName("item_type")
        var itemType: String? = null

        @SerializedName("link")
        var link: String? = null

        @SerializedName("schedular_scale")
        var schedularScale: String? = null

        @SerializedName("description")
        var description: Any? = null

        @SerializedName("item_name")
        var itemName: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("expertCounsellor")
        var expertCounsellor: String? = null

        @SerializedName("counsellor")
        var counsellor: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("sub_item_type")
        var subItemType: String? = null

        @SerializedName("startDate")
        var startDate: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("comment")
        var comment: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "recommendationCount = '" + recommendationCount + '\'' +
                    ",item_id = '" + itemId + '\'' +
                    ",endDate = '" + endDate + '\'' +
                    ",image_url = '" + imageUrl + '\'' +
                    ",item_type = '" + itemType + '\'' +
                    ",link = '" + link + '\'' +
                    ",schedular_scale = '" + schedularScale + '\'' +
                    ",description = '" + description + '\'' +
                    ",item_name = '" + itemName + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    ",sub_item_type = '" + subItemType + '\'' +
                    ",startDate = '" + startDate + '\'' +
                    ",status = '" + status + '\'' +
                    "}"
        }
    }

    class SubUserSchedulesItem {
        @SerializedName("recommendationCount")
        var recommendationCount = 0

        @SerializedName("goal")
        var goal: Goal? = null

        @SerializedName("item_id")
        var itemId = 0

        @SerializedName("rank")
        var rank = 0

        @SerializedName("endDate")
        var endDate: String? = null

        @SerializedName("image_url")
        var imageUrl: Any? = null

        @SerializedName("item_type")
        var itemType: String? = null

        @SerializedName("link")
        var link: String? = null

        @SerializedName("schedular_scale")
        var schedularScale: String? = null

        @SerializedName("item_name")
        var itemName: String? = null

        @SerializedName("expertCounsellor")
        var expertCounsellor: String? = null

        @SerializedName("module_name")
        var module_name: String? = null

        @SerializedName("counsellor")
        var counsellor: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("comment")
        var comment: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("sub_item_type")
        var subItemType: String? = null

        @SerializedName("startDate")
        var startDate: String? = null

        @SerializedName("status")
        var status: String? = null

        override fun toString(): String {
            return "SubUserSchedulesItem{" +
                    "recommendationCount = '" + recommendationCount + '\'' +
                    ",goal = '" + goal + '\'' +
                    ",item_id = '" + itemId + '\'' +
                    ",endDate = '" + endDate + '\'' +
                    ",image_url = '" + imageUrl + '\'' +
                    ",item_type = '" + itemType + '\'' +
                    ",link = '" + link + '\'' +
                    ",schedular_scale = '" + schedularScale + '\'' +
                    ",item_name = '" + itemName + '\'' +
                    ",expertCounsellor = '" + expertCounsellor + '\'' +
                    ",counsellor = '" + counsellor + '\'' +
                    ",name = '" + name + '\'' +
                    ",comment = '" + comment + '\'' +
                    ",id = '" + id + '\'' +
                    ",sub_item_type = '" + subItemType + '\'' +
                    ",startDate = '" + startDate + '\'' +
                    ",status = '" + status + '\'' +
                    "}"
        }
    }

    class Goal {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

        override fun toString(): String {
            return "Goal{" +
                    "image = '" + image + '\'' +
                    ",name = '" + name + '\'' +
                    ",id = '" + id + '\'' +
                    "}"
        }
    }
}