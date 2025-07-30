package com.samiksha.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samiksha.ui.downloads.FeedbackAnswerModel
import com.samiksha.ui.downloads.SkillModel

/**
 * Please increase version whenever any small changes in database function
 */
@Database(
        entities = [SkillModel::class, FeedbackAnswerModel::class],
        version = 1,
        exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getLocalDao(): LocalDao?
}