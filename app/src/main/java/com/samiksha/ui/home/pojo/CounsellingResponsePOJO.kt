package com.samiksha.ui.home.pojo

import com.google.gson.annotations.SerializedName

class CounsellingResponsePOJO {
    @SerializedName("counselling_session_list")
    var counsellingsessionlist: CounsellingSessionList? =
        null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "CounsellingResponsePOJO{" +
                "reward_coupon_list = '" + counsellingsessionlist + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class CounsellingSessionList {
        @SerializedName("data")
        var data: List<DataItem>? =
            null

        override fun toString(): String {
            return "RewardCouponList{" +
                    "data = '" + data + '\'' +
                    "}"
        }
    }

    inner class DataItem {
        @SerializedName("regular_price")
        var regularPrice = 0

        @SerializedName("item_type")
        var itemType: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("type")
        var type: String? = null

        @SerializedName("sale_price")
        var salePrice: Any? = null

        @SerializedName("sub_item_type")
        var subItemType: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "regular_price = '" + regularPrice + '\'' +
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