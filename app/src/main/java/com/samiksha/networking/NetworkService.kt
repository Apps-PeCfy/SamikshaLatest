package com.samiksha.networking

import com.samiksha.ui.drawer.faqs.POJO.FAQResponsePOJO
import com.samiksha.ui.drawer.favourites.FavouritesModel
import com.samiksha.ui.drawer.mypoints.pojo.MyRewardsResponsePOJO
import com.samiksha.ui.drawer.mysession.MySessionResponsePOJO
import com.samiksha.ui.drawer.mytraining.MyTrainingModel
import com.samiksha.ui.drawer.notification.pojo.NotificationResponsePOJO
import com.samiksha.ui.drawer.privacypolicy.ContentResponsePOJO
import com.samiksha.ui.drawer.readempoints.pojo.CoupanListResponsePOJO
import com.samiksha.ui.drawer.settings.pojo.SettingResponsePojo
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.home.pojo.*
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformationModel
import com.samiksha.ui.informationScreen.chooseGoals.pojo.VerifyOTPResponse
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.progressReport.ProgressReportModel
import com.samiksha.ui.progressReport.progressreportweekly.WeeklyReportResponsePOJO
import com.samiksha.ui.register.pojo.IsUserExistResponsePOJO
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import com.samiksha.ui.search.SearchModel
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentReportResponsePOJO
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentScoreResponsePOJO
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.ui.sportPsychology.session.pojo.GoalResponsePOJO
import com.samiksha.ui.sportPsychology.session.pojo.SessionResponsePOJO
import com.samiksha.ui.sportPsychology.session.pojo.VendorResponsePOJO
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @FormUrlEncoded
    @POST("sendOtp")
    fun login(
        @Field("phone_no") phoneno: String?,
        @Field("type") type: String?,
        @Field("country_phone_code") logcountry_phone_codein_type: String?
    ): Call<LoginResponcePOJO?>?

    @FormUrlEncoded
    @POST("sendOtpRegister")
    fun sendotpRegister(
        @Field("phone_no") phoneno: String?,
        @Field("type") type: String?,
        @Field("country_phone_code") logcountry_phone_codein_type: String?
    ): Call<LoginResponcePOJO?>?

    @FormUrlEncoded
    @POST("sendOtp")
    fun register(
        @Field("type") phoneno: String?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("country_phone_code") country_phone_code: String?,
        @Field("phone_no") phone_no: String?,
        @Field("gender") gender: String?,
        @Field("state_id") state: String?
    ): Call<RegisterResponsePOJO?>?


    @FormUrlEncoded
    @POST("checkOtp")
    fun checkOtp(
        @Field("country_phone_code") phoneno: String?,
        @Field("phone_no") logcountry_phone_codein_type: String?,
        @Field("otp") type: String?

    ): Call<CheckOtpResponsePOJO?>?


    @FormUrlEncoded
    @POST("verifyOtp")
    fun verifyOtp(
        @Field("type") type: String?,
        @Field("country_phone_code") country_phone_code: String?,
        @Field("phone_no") phone_no: String?,
        @Field("otp") otp: String?,
        @Field("device_id") device_id: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?,
        @Field("country_code") country_code: String?

    ): Call<CheckOtpResponsePOJO?>?

    @FormUrlEncoded
    @POST("verifyOtp")
    fun registerUser(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("country_phone_code") country_phone_code: String?,
        @Field("phone_no") phone_no: String?,
        @Field("otp") otp: String?,
        @Field("gender") gender: String?,
        @Field("sport_id") sport_id: String?,
        @Field("professional_level_id") professional_level_id: String?,
        @Field("goal_ids[]") goal_ids: ArrayList<Int>?,
        @Field("device_id") device_id: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?,
        @Field("state_id") userCurrentState: String?,
        @Field("referee_code") referee_code: String?,
        @Field("type") type: String?,
        @Field("country_code") userUserCountryCOde: String?


    ): Call<CheckOtpResponsePOJO?>?

    @FormUrlEncoded
    @POST("updateUserGoals")
    fun updateUserGoals(
        @Header("Authorization") header: String?,
        @Field("goal_ids[]") goal_ids: ArrayList<Int>?

    ): Call<VerifyOTPResponse?>?

    @FormUrlEncoded
    @POST("updateWebUser")
    fun updateWebUser(
        @Header("Authorization") header: String?,
        @Field("sport_id") sport_id: String?,
        @Field("professional_level_id") professional_level_id: String?,
        @Field("goal_ids[]") goal_ids: ArrayList<Int>?,

        ): Call<CheckOtpResponsePOJO?>?


    @GET("getQuestions")
    fun getQuestions(): Call<QuestionResponsePOJO?>?

    @GET("getAllGoals")
    fun getAllGoals(): Call<GoalResponsePOJO?>?


    @GET("getMentalSkillCategories")
    fun getMentalSkillCategories(@Header("Authorization") header: String?): Call<AllCategoriesResponsePOJO?>?

    @GET("getMentalSkills")
    fun getMentalSkills(@Header("Authorization") header: String?,
                        @Query("category") category: String?
    ): Call<CategoriesResponsePOJO?>?

    @GET("getSessionMentalSkills")
    fun getSessionMentalSkills(@Header("Authorization") header: String?): Call<SessionResponsePOJO?>?

    @GET("getUserSchedule")
    fun getTrainingSkillList(
        @Header("Authorization") header: String?,
        @Query("start_date") startDate: String?
    ): Call<TrainingPathModel?>?

    @GET("getUserSchedule")
    fun getTrainingSkillListNext(
        @Header("Authorization") header: String?,
        @Query("start_date") startDate: String?,
        @Query("page") currentPage: Int?
    ): Call<TrainingPathModel?>?

    @GET("getUserSchedule")
    fun getTrainingSkillListWithID(
        @Header("Authorization") header: String?,
        @Query("user_id") user_id: String?,
        @Query("start_date") startDate: String?
    ): Call<TrainingPathModel?>?

    @GET("getProgressReportList")
    fun getProgressList(
        @Header("Authorization") header: String?,
        @Query("date") startDate: String?
    ): Call<TrainingPathModel?>?

    @GET("getProgressReportList")
    fun getProgressListwithID(
        @Header("Authorization") header: String?,
        @Query("user_id") id: String?,
        @Query("date") startDate: String?
    ): Call<TrainingPathModel?>?

    @POST("reportSummary")
    fun progressReportSummary(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") scheduleID: String?,
        @Query("date") startDate: String?
    ): Call<ProgressReportModel?>?

    @POST("reportSummary")
    fun progressReportSummaryWithID(
        @Header("Authorization") header: String?,
        @Query("user_id") user_id: String?,
        @Query("mental_skill_id") scheduleID: String?,
        @Query("date") startDate: String?
    ): Call<ProgressReportModel?>?

    @POST("repeatSchedule")
    fun repeatSchedule(
        @Header("Authorization") header: String?,
        @Query("schedule_id") scheduleID: String?
    ): Call<TrainingPathModel?>?

    @POST("deleteSchedule")
    fun deleteSchedule(
        @Header("Authorization") header: String?,
        @Query("schedule_id") scheduleID: String?
    ): Call<TrainingPathModel?>?

    @GET("getMentalSkills")
    fun getMentalSkillsSelected(
        @Header("Authorization") header: String?,
        @Query("goal_id") ride_id: String?
    ): Call<CategoriesResponsePOJO?>?

    @GET("getMentalSkills")
    fun yogaGetMentalSkillsSelected(
        @Header("Authorization") header: String?,
        @Query("module_type") selectedCategory: String?
    ): Call<CategoriesResponsePOJO?>?

    @POST("addToFavorites")
    fun addToFavorite(
        @Header("Authorization") header: String?,
        @Query("context_type") subscription_id: String?,
        @Query("context_id") contextID: String?
    ): Call<SkillDetailsResponsePOJO?>?

    @POST("deleteFromFavorites")
    fun deleteFromFavorites(
        @Header("Authorization") header: String?,
        @Query("favorite_id") skillID: String?
    ): Call<SkillDetailsResponsePOJO?>?

    @GET("favorites")
    fun getFavoritesSkills(
        @Header("Authorization") header: String?,
        @Query("module_type") selectedCategory: String?,
        @Query("page") page: Int?
    ): Call<FavouritesModel?>?

    @GET("getAppVersion")
    fun getAppVersion(
        @Query("type") type: String?,
    ): Call<AppVersionPOJO?>?

    @GET("searchSkill")
    fun getSearchSkills(
        @Header("Authorization") header: String?,
        @Query("search_key") searchKey: String?,
        @Query("page") page: Int?
    ): Call<SearchModel?>?


    @GET("getMentalSkillDetails")
    fun getMentalSkillDetails(
        @Header("Authorization") header: String?,
        @Query("skill_id") skill_id: String?
    ): Call<SkillDetailsResponsePOJO?>?

    @POST("userSubscription")
    fun updateSubscription(
        @Header("Authorization") header: String?,
        @Query("subscription_id") subscription_id: String?
    ): Call<SubscriptionModel?>?

    @POST("submitUserAnswer")
    fun submitUserAnswer(
        @Header("Authorization") header: String?,
        @Query("sub_skill_id") sub_skill_id: String?,
        @Query("answer_id") answer_id: String?,
        @Query("timezone") timezone: String?,
        @Query("created_date") created_date: String?
    ): Call<SportInformationModel?>?

    @POST("skillComplete")
    fun updateSkillStatus(
        @Header("Authorization") header: String?,
        @Query("sub_skill_id") sub_skill_id: String?,
        @Query("context_type") context_type: String?,
        @Query("timezone") timezone: String?,
        @Query("created_date") created_date: String?
    ): Call<SportInformationModel?>?

    @POST("addUserSchedule")
    fun addToTrainingSchedule(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") sub_skill_id: String?,
        @Query("group") context_type: String?
    ): Call<SportInformationModel?>?

    @POST("updateDaySchedule")
    fun updateTrainingSchedule(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") sub_skill_id: String?
    ): Call<SportInformationModel?>?


    @POST("addMyActivity")
    fun addToMyTraining(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") sub_skill_id: String?,
        @Query("timezone") timezone: String?,
        @Query("created_date") created_date: String?
    ): Call<SportInformationModel?>?

    @GET("getMyActivity")
    fun getMyActivities(
        @Header("Authorization") header: String?,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
        @Query("page") page: Int?
    ): Call<MyTrainingModel?>?

    @GET("subscriptions")
    fun getSubscriptionList(
        @Header("Authorization") header: String?
    ): Call<SubscriptionModel?>?


    @GET("getTestimonials")
    fun getTestimonials(@Header("Authorization") header: String?): Call<TestimonialsResponsePOJO?>?


    @GET("states")
    fun getState(
        @Query("country_id") country_id: String?
    ): Call<StateResponsePOJO?>?

    @GET("cities")
    fun getCity(
        @Query("state_id") country_id: String?
    ): Call<StateResponsePOJO?>?

    @GET("getProfileDetails")
    fun getProfile(
        @Header("Authorization") header: String?
    ): Call<CheckOtpResponsePOJO?>?


    @FormUrlEncoded
    @POST("signOut")
    fun signOut(
        @Header("Authorization") header: String?,
        @Field("device_id") phoneno: String?,
        @Field("device_type") type: String?

    ): Call<SignOutResponsePOJO?>?


    @FormUrlEncoded
    @POST("deleteAccount")
    fun deleteAccount(
        @Header("Authorization") header: String?,
        @Field("device_id") phoneno: String?,
        @Field("device_type") type: String?

    ): Call<SignOutResponsePOJO?>?


    @FormUrlEncoded
    @POST("switchUser")
    fun switchUser(
        @Header("Authorization") header: String?,
        @Field("country_phone_code") country_phone_code: String?,
        @Field("phone_no") phone_no: String?

    ): Call<CheckOtpResponsePOJO?>?


    @GET("faqs")
    fun getFaqs(@Header("Authorization") header: String?): Call<FAQResponsePOJO?>?


    @FormUrlEncoded
    @POST("updateProfile")
    fun updateProfile(
        @Header("Authorization") header: String?,
        @Field("title") title: String?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("gender") gender: String?,
        @Field("date_of_birth") date_of_birth: String?,
        @Field("role") role: String?,
        @Field("if_minor_guardian_contact") if_minor_guardian_contact: String?,
        @Field("academy_institute") academy_institute: String?,
        @Field("state_id") state_id: Int,
        @Field("city_id") city: String?,
        @Field("sport_id") sport_id: Int,
        @Field("professional_level_id") professional_level_id: Int

    ): Call<CheckOtpResponsePOJO?>?


    @GET("userRewards")
    fun getuserRewards(@Header("Authorization") header: String?): Call<MyRewardsResponsePOJO?>?

    @GET("users")
    fun getuser(@Header("Authorization") header: String?): Call<UserResponsePOJO?>?

    @GET("users")
    fun getuserWithID(
        @Header("Authorization") header: String?,
        @Query("user_id") points: String?
    ): Call<UserResponsePOJO?>?

  @GET("searchUsers")
    fun getSearchUsers(
        @Header("Authorization") header: String?,
        @Query("search_key") points: CharSequence?
    ): Call<UserResponsePOJO?>?


    @GET("getCounsellingSessionsList")
    fun getCounsellingSessionsList(@Header("Authorization") header: String?): Call<CounsellingResponsePOJO?>?


    @GET("pageContents")
    fun pageContents(@Query("page_name") page_name: String?): Call<ContentResponsePOJO?>?

    @GET("getLanguageList")
    fun getUserSetting(@Header("Authorization") header: String?): Call<SettingResponsePojo?>?


    @FormUrlEncoded
    @POST("updateLanguage")
    fun updateLanguage(
        @Header("Authorization") header: String?,
        @Field("id") id: String?

    ): Call<SettingResponsePojo?>?


    @GET("getRewardCouponList")
    fun getRewardCouponList(
        @Header("Authorization") header: String?,
        @Query("points") points: String?
    ): Call<CoupanListResponsePOJO?>?

    @GET("getRewardCouponList")
    fun getRewardCouponListWithOutParameter(
        @Header("Authorization") header: String?
    ): Call<CoupanListResponsePOJO?>?


    @GET("notificationList")
    fun getnotificationList(@Header("Authorization") header: String?): Call<NotificationResponsePOJO?>?


    @GET("getUserSchedules")
    fun getSessionList(
        @Header("Authorization") header: String?,
        @Query("user_id") points: String?
    ): Call<MySessionResponsePOJO?>?


    @POST("addUserSubScheduleComment")
    fun updateComment(
        @Header("Authorization") header: String?,
        @Query("schedule_id") schedule_id: Int,
        @Query("comment") comment: String?

    ): Call<OnlyMessageResponsePOJO?>?


    @POST("updateUserState")
    fun updateUserState(
        @Header("Authorization") header: String?,
        @Query("state_id") points: Int?
    ): Call<OnlyMessageResponsePOJO?>?


    @POST("weeklyReportSummary")
    fun weeklyReport(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") mental_skill_id: String?,
        @Query("from_date") from_date: String?,
        @Query("to_date") to_date: String?
    ): Call<WeeklyReportResponsePOJO>?


    @POST("weeklyReportSummary")
    fun weeklyReportWithID(
        @Header("Authorization") header: String?,
        @Query("mental_skill_id") mental_skill_id: String?,
        @Query("from_date") from_date: String?,
        @Query("to_date") to_date: String?,
        @Query("user_id") user_id: String?
    ): Call<WeeklyReportResponsePOJO>?


    @GET("getAssessments")
    fun getAssesmentList(
        @Header("Authorization") header: String?,
        @Query("user_id") user_id: String?
    ): Call<AssesmentReportResponsePOJO?>?

    @GET("getAssessmentScores")
    fun getAssesmentScore(
        @Header("Authorization") header: String?,
        @Query("user_id") user_id: String?,
        @Query("assessment_id") assessment_id: String?
    ): Call<AssesmentScoreResponsePOJO?>?


    @FormUrlEncoded
    @POST("createOrder")
    fun counsellingSessionPayment(
        @Header("Authorization") header: String?,
        @Field("context_type") context_type: String?,
        @Field("context_id") context_id: Int,
        @Field("item_name") item_name: String?,
        @Field("gst_no") gst_no: String?,
        @Field("status") status: String?,
        @Field("total_amount") total_amount: Int?,
        @Field("transaction_id") transaction_id: String?,
        @Field("transaction_type") transaction_type: String?

    ): Call<OnlyMessageResponsePOJO?>?

    @FormUrlEncoded
    @POST("createOrderNew")
    fun createOrderPayment(
        @Header("Authorization") header: String?,
        @Field("context_type") context_type: String?,
        @Field("context_id") context_id: String?,
        @Field("item_name") item_name: String?,
        @Field("gst_no") gst_no: String?,
        @Field("total_amount") total_amount: Double?

    ): Call<OrderModel?>?

    @FormUrlEncoded
    @POST("updateOrder")
    fun updateOrderPayment(
        @Header("Authorization") header: String?,
        @Field("context_type") context_type: String?,
        @Field("context_id") context_id: String?,
        @Field("order_id") order_id: String?,
        @Field("status") status: String?,
        @Field("transaction_id") total_amount: String?,
        @Field("discount_coupon_id") discount_coupon_id: String?

    ): Call<OnlyMessageResponsePOJO?>?

    @FormUrlEncoded
    @POST("failedOrder")
    fun failedOrderStatus(
        @Header("Authorization") header: String?,
        @Field("order_id") order_id: String?,
        @Field("status") status: String?,
        @Field("transaction_id") transaction_id: String?,
        @Field("status_code") status_code: String?,
        @Field("description") description: String?

    ): Call<OnlyMessageResponsePOJO?>?

    @FormUrlEncoded
    @POST("addUserRewardCoupon")
    fun userRewardCoupon(
        @Header("Authorization") header: String?,
        @Field("discount_coupon_id") context_id: Int
    ): Call<OnlyMessageResponsePOJO?>?


    @FormUrlEncoded
    @POST("createEnquiry")
    fun contactUs(
        @Header("Authorization") header: String?,
        @Field("message") context_id: String
    ): Call<OnlyMessageResponsePOJO?>?

    @FormUrlEncoded
    @POST("addUserReward")
    fun addUserReward(
        @Header("Authorization") header: String?,
        @Field("context_type") context_type: String,
        @Field("context_id") context_id: String,
        @Field("type") type: String,
        @Field("point") point: Int
    ): Call<OnlyMessageResponsePOJO?>?


    @POST("updateUserSubSchedules")
    fun updateUserSubSchedules(
        @Header("Authorization") header: String?,
        @Query("user_sub_schedule_id") schedule_id: Int,
        @Query("name") name: String?,
        @Query("module_id") module_id: Int,
        @Query("counsellor_id") counsellor_id: String?,
        @Query("goal_id") goal_id: Int,
        @Query("start_date") start_date: String?,
        @Query("link") link: String?,
        @Query("status") status: String?,
        @Query("comment") comment: String?,
        @Query("rank") rank: String?


    ): Call<OnlyMessageResponsePOJO?>?


    @GET("getVendors")
    fun getVendors(
        @Header("Authorization") header: String?,
        @Query("role") startDate: String?
    ): Call<VendorResponsePOJO?>?


    @GET("isUserExist")
    fun isUserExist(
        @Query("email") email: String
    ): Call<IsUserExistResponsePOJO?>



    @FormUrlEncoded
    @POST("updateOrderStatus")
    fun checkOrderStatus(
        @Header("Authorization") header: String?,
        @Field("order_id") order_id: Int?,
    ):Call<OnlyMessageResponsePOJO?>


}