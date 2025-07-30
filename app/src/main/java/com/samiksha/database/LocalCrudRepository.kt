package com.samiksha.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.samiksha.ui.downloads.FeedbackAnswerModel
import com.samiksha.ui.downloads.SkillModel


class LocalCrudRepository {
    companion object {
        private var instance: LocalCrudRepository? = null
        private var localDatabase: LocalDatabase? = null
        @Synchronized
        fun getInstance(context: Context?): LocalCrudRepository? {
            if (instance == null) { //if there is no instance available... create new one
                instance = LocalCrudRepository()

                localDatabase = Room.databaseBuilder(
                    context!!,
                    LocalDatabase::class.java, AppDatabaseConstant.DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return instance
        }
    }

    /**
     * SkillModel QUERY
     */
    fun insertSkillModel(model: SkillModel?) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                localDatabase?.getLocalDao()?.insertSkillModel(model)
                return null
            }
        }.execute()
    }

    fun updateSkillModel(model: SkillModel?) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                localDatabase?.getLocalDao()?.updateSkillModel(model)
                return null
            }
        }.execute()
    }

    fun deleteSkillModel(model: SkillModel?) {
        localDatabase?.getLocalDao()?.deleteSkillModel(model)
    }

    fun getAllSkills(userID: String?): List<SkillModel?>? {
        return localDatabase?.getLocalDao()?.getAllSkills(userID)
    }


    fun getSkillModelBySkillName(userID: String?, skillName: String?): SkillModel? {
        return localDatabase?.getLocalDao()?.getSkillModelBySkillName(userID, skillName)
    }

   /* fun updateTripInfoStatus(userID: String?) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                localDatabase?.getLocalDao()?.updateTripInfoStatus(userID)
                return null
            }
        }.execute()
    }

    fun updateTripImageStatus(userID: String?): Unit? {
        return localDatabase?.getLocalDao()?.updateTripImageStatus(userID)
    }




    fun getTripListBeforeDate(currentDate: String?, userID: String?): List<SkillModel?>? {
        return localDatabase?.getLocalDao()?.getTripListBeforeDate(currentDate, userID)
    }

    fun getUploadingTripList(userID: String?): List<SkillModel?>? {
        return localDatabase?.getLocalDao()?.getUploadingTripList(userID)
    }

    fun getUploadingTripImageList(userID: String?): List<SkillModel?>? {
        return localDatabase?.getLocalDao()?.getUploadingTripImageList(userID)
    }

    fun getSkillModelDateWise(date: String?, userID: String?): SkillModel? {
        return localDatabase?.getLocalDao()?.getSkillModelDateWise(date, userID)
    }

    fun deleteAllTripsData(currentDate: String?, userID: String?): Unit? {
        return localDatabase?.getLocalDao()?.deleteAllTripsData(currentDate, userID)
    }*/


    fun insertFeedbackAnswerModel(model: FeedbackAnswerModel?) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                localDatabase?.getLocalDao()?.insertFeedbackAnswerModel(model)
                return null
            }
        }.execute()
    }

    fun updateFeedbackAnswerModel(model: FeedbackAnswerModel?) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                localDatabase?.getLocalDao()?.updateFeedbackAnswerModel(model)
                return null
            }
        }.execute()
    }


    fun getAllFeedbackAnswerModel(userID: String?): List<FeedbackAnswerModel?>? {
        return localDatabase?.getLocalDao()?.getAllFeedbackAnswerModel(userID)
    }


    fun deleteAllFeedbackAnswerModel(userID: String?): Unit? {
        return localDatabase?.getLocalDao()?.deleteAllFeedbackAnswerModel(userID)
    }

    fun isTrainingCompleteOnDate(userID: String?, date: String?, skillName: String?): Int? {
        return localDatabase?.getLocalDao()?.isTrainingCompleteOnDate(userID, date, skillName)
    }


}