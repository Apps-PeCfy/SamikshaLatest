package com.samiksha.ui.drawer.notification.pojo

import com.google.gson.annotations.SerializedName

class NotificationResponsePOJO {
    @SerializedName("notification_list")
    var notificationList: NotificationList? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "NotificationResponsePOJO{" +
                "notification_list = '" + notificationList + '\'' +
                ",message = '" + message + '\'' +
                "}"
    }

    inner class NotificationList {
        @SerializedName("data")
        var data: List<DataItem>? =
            null

        override fun toString(): String {
            return "NotificationList{" +
                    "data = '" + data + '\'' +
                    "}"
        }
    }

    inner class DataItem {
        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("type")
        var type: String? = null

        @SerializedName("message")
        var message: String? = null

        @SerializedName("status")
        var status: String? = null

        override fun toString(): String {
            return "DataItem{" +
                    "created_at = '" + createdAt + '\'' +
                    ",id = '" + id + '\'' +
                    ",type = '" + type + '\'' +
                    ",message = '" + message + '\'' +
                    ",status = '" + status + '\'' +
                    "}"
        }
    }
}