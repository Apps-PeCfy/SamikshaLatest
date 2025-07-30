package com.samiksha.ui.drawer.readempoints.pojo

import com.google.gson.annotations.SerializedName

class CoupanListResponsePOJO {
    @SerializedName("reward_coupon_list")
    var rewardCouponList: RewardCouponList? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "CoupanListResponsePOJO{" +
                "reward_coupon_list = '" + rewardCouponList + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class RewardCouponList {
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
        @SerializedName("id")
        var id: Int? = 0

        @SerializedName("amount")
        var amount: Int? = 0


        @SerializedName("couponName")
        var couponName: String? = null

       @SerializedName("discountType")
        var discountType: String? = null

        @SerializedName("discountId")
        var discountId: Int? = 0


        @SerializedName("usage")
        var usage: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("expireAt")
        var expireAt: String? = null

        @SerializedName("rewardPoint")
        var rewardPoint: String? = null

        @SerializedName("companyName")
        var companyName: String? = null

        @SerializedName("companyIcon")
        var companyIcon: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("appDescription")
        var appDescription: String? = null


        override fun toString(): String {
            return "DataItem{" +
                    "id = '" + id + '\'' +
                    "}"
        }
    }
}