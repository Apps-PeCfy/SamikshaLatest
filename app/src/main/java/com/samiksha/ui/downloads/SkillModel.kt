package com.samiksha.ui.downloads

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.samiksha.database.AppDatabaseConstant


@Entity(tableName = AppDatabaseConstant.MASTER_TABLE)
class SkillModel {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id = 0

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("fileName")
    var fileName: String? = null

    @SerializedName("jsonData")
    var jsonData: String? = null
}