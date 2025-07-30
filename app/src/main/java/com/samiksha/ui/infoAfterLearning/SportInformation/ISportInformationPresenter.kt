package com.samiksha.ui.infoAfterLearning.SportInformation

interface ISportInformationPresenter {
    fun submitAnswer(
        token: String?,
        sub_skill_id: String?,
        answer_id: String?
    )

    fun updateSkillStatus(
        token: String?,
        sub_skill_id: String?,
        context_type: String?
    )

    fun addToMyTraining(
        token: String?,
        skill_id: String?
    )

    fun progressQuiz(
        token: String?,
        rewardsPoint: Int?,
        mental_skill_id: String?,

        )

    fun addToTrainingSchedule(
        token: String?,
        skill_id: String?,
        group: String?
    )

    fun updateTrainingSchedule(
        token: String?,
        skill_id: String?
    )
}