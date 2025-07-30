package com.samiksha.ui.downloads

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.samiksha.database.LocalCrudRepository
import com.samiksha.databinding.ActivityDownloadedTrainingFeedbackBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformationPresenterImplementer
import com.samiksha.ui.infoAfterLearning.trainingFeedback.TrainingFeedbackAdapter
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager

class DownloadedTrainingFeedbackActivity : AppCompatActivity() {
    lateinit var binding: ActivityDownloadedTrainingFeedbackBinding
    private var context: Context = this
    private var mAdapter: TrainingFeedbackAdapter? = null
    private var skillModel : SkillModel ?= null
    private var skillDetail : SkillDetailsResponsePOJO.Data ?= null
    private var trainingFirstQuestionModel: SkillDetailsResponsePOJO.DataItem? = null
    private var trainingSecondQuestionModel: SkillDetailsResponsePOJO.DataItem? = null
    private var list: List<SkillDetailsResponsePOJO.SubSkillAnswersModel> = ArrayList()
    private var isAnswered: Boolean = false
    private var isLastQuestion: Boolean = false
    private var selectedAnswersModel: SkillDetailsResponsePOJO.SubSkillAnswersModel? = null

    private var localCrudRepository: LocalCrudRepository? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadedTrainingFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        supportActionBar?.hide()

        localCrudRepository = LocalCrudRepository.getInstance(context)
        sessionManager = SessionManager.getInstance(context)

        skillModel = Gson().fromJson(intent.getStringExtra("skill_model"), SkillModel::class.java)
        skillDetail = Gson().fromJson(skillModel?.jsonData, SkillDetailsResponsePOJO.Data::class.java)

        getModels()


        setListeners()
        setRecyclerView()
    }

    private fun getModels() {
        if (skillDetail?.subSkiils?.size!! > 1 && skillDetail?.subSkiils!![1].data!!.isNotEmpty() && skillDetail?.subSkiils!![1].data?.size!! > 2) {
            trainingFirstQuestionModel = skillDetail?.subSkiils!![1].data?.get(1)
            trainingSecondQuestionModel = skillDetail?.subSkiils!![1].data?.get(2)
            setQuestion(trainingFirstQuestionModel)
        }
    }

    private fun setQuestion(model: SkillDetailsResponsePOJO.DataItem?) {
        list = model?.subSkillAnswers!!
        binding.txtQuestion.text = model.name
        mAdapter?.updateAdapter(list)
        ProjectUtils.dismissProgressDialog()
    }

    private fun setRecyclerView() {
        mAdapter = TrainingFeedbackAdapter(
            context,
            list,
            object : TrainingFeedbackAdapter.TrainingFeedbackAdapterInterface {
                override fun onItemSelected(
                    position: Int,
                    model: SkillDetailsResponsePOJO.SubSkillAnswersModel
                ) {
                    selectedAnswersModel = model
                    isAnswered = true
                    changeColorLogic(model)
                }

            })


        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
    }

    private fun changeColorLogic(model: SkillDetailsResponsePOJO.SubSkillAnswersModel) {
        for (answerModel in list) {
            answerModel.checked = model.id == answerModel.id
        }
        mAdapter?.updateAdapter(list)
    }

    private fun setListeners() {
        binding.apply {
            btnSubmit.setOnClickListener {

                if (isAnswered) {
                    if (!isLastQuestion) {
                       var feedbackAnswerModel : FeedbackAnswerModel = FeedbackAnswerModel()
                        feedbackAnswerModel.sub_skill_id = trainingFirstQuestionModel?.id.toString()
                        feedbackAnswerModel.answer_id =  selectedAnswersModel?.id.toString()
                        feedbackAnswerModel.user_id = sessionManager?.getUserModel()?.id
                        feedbackAnswerModel.date = ProjectUtils.getTodayDate("yyyy-MM-dd")
                        feedbackAnswerModel.skill_name = skillDetail?.name
                        localCrudRepository?.insertFeedbackAnswerModel(feedbackAnswerModel)

                        isLastQuestion = true
                        isAnswered = false
                        setQuestion(trainingSecondQuestionModel)
                    } else {

                        var feedbackAnswerModel : FeedbackAnswerModel = FeedbackAnswerModel()
                        feedbackAnswerModel.sub_skill_id = trainingSecondQuestionModel?.id.toString()
                        feedbackAnswerModel.answer_id =  selectedAnswersModel?.id.toString()
                        feedbackAnswerModel.user_id = sessionManager?.getUserModel()?.id
                        feedbackAnswerModel.date = ProjectUtils.getTodayDate("yyyy-MM-dd")
                        feedbackAnswerModel.skill_name = skillDetail?.name
                        localCrudRepository?.insertFeedbackAnswerModel(feedbackAnswerModel)

                        Constants.SHOULD_RELOAD = true
                        finish()

                    }
                } else {
                    Toast.makeText(context, "Please select your option.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    override fun onBackPressed() {
        if (isLastQuestion && !isAnswered) {
            Toast.makeText(context, "Please give second feedback answer too.", Toast.LENGTH_SHORT)
                .show()
        }else{
            super.onBackPressed()
        }

    }
}