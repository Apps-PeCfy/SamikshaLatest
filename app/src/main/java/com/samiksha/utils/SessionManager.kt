package com.samiksha.utils

import android.content.Context
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.sportPsychology.UserResponsePOJO

open class SessionManager(context: Context) {
    private var mContext: Context = context
    private var playerPosition: Long = 0
    private var skillDetailModel: SkillDetailModel? = null
    private var userModel: CheckOtpResponsePOJO.User? = null
    private var allCategoriesResponsePOJO: AllCategoriesResponsePOJO? = null
    private var userSubscriptionModel: SubscriptionModel? = null
    private var coachUserModel: UserResponsePOJO? = null
    private var expertModel: UserResponsePOJO.DataItem? = null


    open fun getUserModel(): CheckOtpResponsePOJO.User? {
        if (userModel == null) {
            PreferencesManager.initializeInstance(context = mContext)
            userModel = PreferencesManager.instance?.getUserModel()
        }
        return userModel
    }

    open fun setUserModel(userModel: CheckOtpResponsePOJO.User) {
        this.userModel = userModel
    }


 open fun getAllCategories(): AllCategoriesResponsePOJO? {
        if (allCategoriesResponsePOJO == null) {
            PreferencesManager.initializeInstance(context = mContext)
            allCategoriesResponsePOJO = PreferencesManager.instance?.getAllCategoryModel()
        }
        return allCategoriesResponsePOJO
    }

    open fun setAllCategories(allCategoriesResponsePOJO: AllCategoriesResponsePOJO) {
        this.allCategoriesResponsePOJO = allCategoriesResponsePOJO
    }

 open fun getUserSubscription(): SubscriptionModel? {
        if (userSubscriptionModel == null) {
            PreferencesManager.initializeInstance(context = mContext)
            userSubscriptionModel = PreferencesManager.instance?.getSubscription()
        }
        return userSubscriptionModel
    }

    open fun setUserSubscription(userSubscriptionModel: SubscriptionModel) {
        this.userSubscriptionModel = userSubscriptionModel
    }












    open fun getCoachOrCounsellorModel(): UserResponsePOJO? {
        if (coachUserModel == null) {
            PreferencesManager.initializeInstance(context = mContext)
            coachUserModel = PreferencesManager.instance?.getCoachOrCounsellorModel()
        }
        return coachUserModel
    }

    open fun setCoachOrCounsellorModel(coachUserModel: UserResponsePOJO?) {
        this.coachUserModel = coachUserModel
    }





    open fun getExpertModel(): UserResponsePOJO.DataItem? {
        if (expertModel == null) {
            PreferencesManager.initializeInstance(context = mContext)
            expertModel = PreferencesManager.instance?.getExpertCounsellorModel()
        }
        return expertModel
    }

    open fun setExpertModel(expertModel: UserResponsePOJO.DataItem) {
        this.expertModel = expertModel
    }







    open fun getSkillDetailModel(): SkillDetailModel? {
        return skillDetailModel
    }

    open fun setSkillDetailModel(skillDetailModel: SkillDetailModel) {
        this.skillDetailModel = skillDetailModel
    }

    open fun getPlayerPosition(): Long {
        return playerPosition
    }

    open fun setPlayerPosition(playerPosition: Long) {
        this.playerPosition = playerPosition
    }

    companion object {
        private var sInstance: SessionManager? = null

        @Synchronized
        fun getInstance(context: Context): SessionManager {
            if (sInstance == null) {
                sInstance = SessionManager(context)
            }
            return sInstance as SessionManager
        }
    }


}