package com.samiksha.ui.sportPsychology.reports.pojo

import com.google.gson.annotations.SerializedName

class AssesmentReportResponsePOJO {
    @SerializedName("assessments")
    var assessments: Assessments? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "AssesmentReportResponsePOJO{" +
                "assessments = '" + assessments + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class Assessments {
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
            return "Assessments{" +
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
        @SerializedName("coming_soon")
        var isComingSoon = false

        @SerializedName("regular_price")
        var regularPrice: Any? = null

        @SerializedName("image_url")
        var imageUrl: Any? = null

        @SerializedName("item_type")
        var itemType: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: Any? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("type")
        var type: Any? = null

        @SerializedName("sale_price")
        var salePrice: Any? = null

        @SerializedName("sub_item_type")
        var subItemType: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "coming_soon = '" + isComingSoon + '\'' +
                    ",regular_price = '" + regularPrice + '\'' +
                    ",image_url = '" + imageUrl + '\'' +
                    ",item_type = '" + itemType + '\'' +
                    ",name = '" + name + '\'' +
                    ",description = '" + description + '\'' +
                    ",id = '" + id + '\'' +
                    ",type = '" + type + '\'' +
                    ",sale_price = '" + salePrice + '\'' +
                    ",sub_item_type = '" + subItemType + '\'' +
                    "}"
        }
    }
}