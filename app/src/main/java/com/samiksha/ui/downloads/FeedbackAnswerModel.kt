package com.samiksha.ui.downloads

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.samiksha.database.AppDatabaseConstant

@Entity(tableName = AppDatabaseConstant.FEEDBACK_ANSWER_TABLE)
class FeedbackAnswerModel {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id = 0

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("skill_name")
    var skill_name: String? = null

    @SerializedName("sub_skill_id")
    var sub_skill_id: String? = null

    @SerializedName("answer_id")
    var answer_id: String? = null
}