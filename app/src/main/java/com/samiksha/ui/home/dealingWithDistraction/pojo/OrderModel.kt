package com.samiksha.ui.home.dealingWithDistraction.pojo

import com.google.gson.annotations.SerializedName

class OrderModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: Data ?= null

    inner class Data {

        @SerializedName("razorpayOrderId")
        var razorpayOrderId: String? = null

        @SerializedName("order")
        var order: Order? = null
    }

    inner class Order {
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("user_id")
        var user_id: String? = null

        @SerializedName("gst_no")
        var gst_no: String? = null

        @SerializedName("selling_price")
        var sellingPrice: String? = null

        @SerializedName("total_amount")
        var total_amount: Double?= null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("order_date")
        var order_date: String? = null

        @SerializedName("invoice_no")
        var invoice_no: String? = null

        @SerializedName("updated_at")
        var updated_at: String?= null

        @SerializedName("created_at")
        var created_at: String? = null

    }


}


