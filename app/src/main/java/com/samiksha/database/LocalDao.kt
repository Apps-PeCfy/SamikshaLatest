package com.samiksha.database

import androidx.room.*
import com.samiksha.ui.downloads.FeedbackAnswerModel
import com.samiksha.ui.downloads.SkillModel

@Dao
interface LocalDao {
    @Query("SELECT * FROM " + AppDatabaseConstant.MASTER_TABLE.toString() + " WHERE user_id LIKE :userID")
    fun getAllSkills(userID: String?): List<SkillModel?>?

    @Query("SELECT * FROM " + AppDatabaseConstant.MASTER_TABLE.toString() + " WHERE type LIKE :skillName AND  user_id LIKE :userID")
    fun getSkillModelBySkillName(userID: String?, skillName: String?): SkillModel?

    /*@Query("SELECT * FROM " + AppDatabaseConstant.MASTER_TABLE.toString() + " WHERE is_image_uploaded = 0 AND  user_id LIKE :userID")
    fun getUploadingTripImageList(userID: String?): List<SkillModel?>?

    @Query("SELECT * FROM " + AppDatabaseConstant.MASTER_TABLE.toString() + " WHERE DATE(uniqueDate) <  :currentDate AND user_id LIKE :userID")
    fun getTripListBeforeDate(currentDate: String?, userID: String?): List<SkillModel?>?

    @Query("SELECT * FROM " + AppDatabaseConstant.MASTER_TABLE.toString() + " WHERE uniqueDate LIKE :date AND  user_id LIKE :userID")
    fun getSkillModelDateWise(date: String?, userID: String?): SkillModel?

    @Query("DELETE FROM " + AppDatabaseConstant.MASTER_TABLE.toString()+ " WHERE DATE(uniqueDate) <  :currentDate AND  user_id LIKE :userID AND is_image_uploaded = 1 AND is_info_uploaded = 1")
    fun deleteAllTripsData(currentDate: String?, userID: String?)

    @Query("UPDATE " + AppDatabaseConstant.MASTER_TABLE.toString() + " SET is_info_uploaded = 1 WHERE user_id LIKE :userID")
    fun updateTripInfoStatus(userID: String?)

    @Query("UPDATE " + AppDatabaseConstant.MASTER_TABLE.toString() + " SET is_image_uploaded = 1 WHERE user_id LIKE :userID")
    fun updateTripImageStatus(userID: String?)*/

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    fun insertSkillModel(model: SkillModel?)

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    fun updateSkillModel(model: SkillModel?)

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    fun deleteSkillModel(model: SkillModel?)


    /**
     * PLANTATION MODULE
     */
    @Query("SELECT * FROM " + AppDatabaseConstant.FEEDBACK_ANSWER_TABLE.toString() + " WHERE user_id LIKE :userID")
    fun getAllFeedbackAnswerModel(userID: String?): List<FeedbackAnswerModel?>?

    @Insert
    fun insertFeedbackAnswerModel(model: FeedbackAnswerModel?)

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    fun updateFeedbackAnswerModel(model: FeedbackAnswerModel?)

    @Query("DELETE FROM " + AppDatabaseConstant.FEEDBACK_ANSWER_TABLE.toString()+ " WHERE user_id LIKE :userID")
    fun deleteAllFeedbackAnswerModel(userID: String?)

    @Query("SELECT * FROM " + AppDatabaseConstant.FEEDBACK_ANSWER_TABLE.toString() + " WHERE skill_name LIKE :skillName AND  user_id LIKE :userID AND  date LIKE :date")
    fun  isTrainingCompleteOnDate(userID: String?, date: String?, skillName: String?) : Int


}