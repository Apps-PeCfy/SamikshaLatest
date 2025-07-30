package com.samiksha.ui.informationScreen.chooseGoals

interface IRegisterPresenter {

    fun registerUser(
        first_name: String?,
        last_name: String?,
        email: String?,
        country_phone_code: String?,
        phone_no: String?,
        otp: String?,
        gender: String?,
        sport_id: String?,
        professional_level_id: String?,
        goal_ids: ArrayList<Int>?,
        device_id: String?,
        device_type: String?,
        device_token: String?,
        userCurrentState: String?,
        referee_code: String?,
        type: String?,
        userUserCountryCOde: String?
    )

    fun updateWebUser(
        token: String?,
        sport_id: String?,
        professional_level_id: String?,
        goal_ids: ArrayList<Int>?
    )


    fun question()

    fun updateGoals(
        token: String?,
        goal_ids: ArrayList<Int>?
    )


}