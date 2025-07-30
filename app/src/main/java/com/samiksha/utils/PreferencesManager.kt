package com.samiksha.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.sportPsychology.UserResponsePOJO

class PreferencesManager private constructor(context: Context) {
    private val mPref: SharedPreferences
    private val cityList_key : String = "QUESTIONLIST"
    private val KEY_USER = "user"
    private val KEY_ALL = "all"
    private val SUBSCRIPTION = "subscription"
    private val USER = "userCoachOrCounsellor"
    private val USER_EXPERT = "userExpert"
    private val KEY_REFFERAL = "refferal"

    var selectedgoalsArrayListFinal: ArrayList<Int> = ArrayList<Int>()


    fun createLoginSession(userModel:  CheckOtpResponsePOJO.User?) {
        val gson = Gson()
        val jsonString = gson.toJson(userModel)
        mPref.edit().putString(KEY_USER, jsonString).apply()
    }

    fun getUserModel(): CheckOtpResponsePOJO.User? {
        val gson = Gson()
        val jsonString: String? = mPref.getString(KEY_USER, "")
        return gson.fromJson(jsonString,  CheckOtpResponsePOJO.User::class.java)
    }

    fun getAllCategoryModel(): AllCategoriesResponsePOJO? {
        val gson = Gson()
        val jsonString: String? = mPref.getString(KEY_ALL, "")
        return gson.fromJson(jsonString,  AllCategoriesResponsePOJO::class.java)
    }

    fun createSubscription(subscriptionModel: SubscriptionModel?) {
        val gson = Gson()
        val jsonString = gson.toJson(subscriptionModel)
        mPref.edit().putString(SUBSCRIPTION, jsonString).apply()
    }

    fun getSubscription():SubscriptionModel?{
        val gson = Gson()
        val jsonString: String? = mPref.getString(SUBSCRIPTION, "")
        return gson.fromJson(jsonString,  SubscriptionModel::class.java)

    }


    fun createCoachOrCounsellorSession(userModel: UserResponsePOJO?) {
        val gson = Gson()
        val jsonString = gson.toJson(userModel)
        mPref.edit().putString(USER, jsonString).apply()
    }

    fun getCoachOrCounsellorModel(): UserResponsePOJO? {
        val gson = Gson()
        val jsonString: String? = mPref.getString(USER, "")
        return gson.fromJson(jsonString,  UserResponsePOJO::class.java)
    }

 fun createExpertSession(userModel: UserResponsePOJO.DataItem?) {
        val gson = Gson()
        val jsonString = gson.toJson(userModel)
        mPref.edit().putString(USER_EXPERT, jsonString).apply()
    }

    fun getExpertCounsellorModel(): UserResponsePOJO.DataItem? {
        val gson = Gson()
        val jsonString: String? = mPref.getString(USER_EXPERT, "")
        return gson.fromJson(jsonString,  UserResponsePOJO.DataItem::class.java)
    }


    fun setStringValue(KEY_VALUE: String?, value: String?) {
        mPref.edit()
            .putString(KEY_VALUE, value)
            .apply()
    }

    fun getStringValue(KEY_VALUE: String?): String? {
        return mPref.getString(KEY_VALUE, "")
    }

    fun setIntegerValue(KEY_VALUE: String, value: Int) {
        mPref.edit()
            .putInt(KEY_VALUE, value)
            .apply()
    }

    fun getIntegerValue(KEY_VALUE: String?): Int {
        return mPref.getInt(KEY_VALUE, 0)
    }

    fun setBooleanValue(KEY_VALUE: String?, value: Boolean) {
        mPref.edit().putBoolean(KEY_VALUE, value).apply()
    }

    fun getBooleanValue(KEY_VALUE: String?, defaultValue: Boolean): Boolean {
        return mPref.getBoolean(KEY_VALUE, defaultValue)
    }

    fun remove(key: String?) {
        mPref.edit()
            .remove(key)
            .apply()
    }

    fun clear(): Boolean {
        return mPref.edit()
            .clear()
            .commit()
    }




    fun setReferalCode( value: String?) {
        mPref.edit()
            .putString(KEY_REFFERAL, value)
            .apply()
    }

    fun getReferalCode(): String? {
        return mPref.getString(KEY_REFFERAL, "")
    }




    fun setcategoryList(categortArrayList: List<AllCategoriesResponsePOJO.DataItem>) {
        val gson = Gson()
        val jsonString = gson.toJson(categortArrayList)
        mPref.edit().putString(cityList_key, jsonString).apply()

    }


    fun getcategoryList(): List<AllCategoriesResponsePOJO.DataItem> {
        var cityList: List<AllCategoriesResponsePOJO.DataItem> = ArrayList()
        val gson = Gson()
        val jsonString = mPref.getString(cityList_key, "")
        if(jsonString != null && jsonString.isNotEmpty()){
            cityList = gson.fromJson(jsonString, Array<AllCategoriesResponsePOJO.DataItem>::class.java).asList()
        }
        return cityList
    }



    companion object {
        private const val PREF_NAME = "Samiksha"
        private var sInstance: PreferencesManager? = null

        @Synchronized
        fun initializeInstance(context: Context) {
            if (sInstance == null) {
                sInstance = PreferencesManager(context)
            }
        }


        @get:Synchronized
        val instance: PreferencesManager?
            get() {
                checkNotNull(sInstance) {
                    PreferencesManager::class.java.simpleName +
                            " is not initialized, call initializeInstance(..) method first."
                }
                return sInstance
            }
    }

    init {
        mPref = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
    }






}