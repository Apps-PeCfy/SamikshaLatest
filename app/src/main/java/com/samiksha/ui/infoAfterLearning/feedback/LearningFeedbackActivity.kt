package com.samiksha.ui.infoAfterLearning.feedback

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.samiksha.R
import com.samiksha.databinding.ActivityLearningFeedbackBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.infoAfterLearning.SportInformation.*
import com.samiksha.ui.infoAfterLearning.finishLearning.FinishLearningActivity
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager

class LearningFeedbackActivity : AppCompatActivity(), ISportInformationView {
    lateinit var binding: ActivityLearningFeedbackBinding
    private var context: Context = this
    private var mAdapter: SportsInformationAdapter? = null
    private var list: List<SkillDetailsResponsePOJO.SubSkillAnswersModel> = ArrayList()
    private var sessionManager: SessionManager? = null
    private var isLastQuestion: Boolean = false
    private var isAnswered: Boolean = false
    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    private var selectedAnswersModel: SkillDetailsResponsePOJO.SubSkillAnswersModel? = null

    private var iSportInformationPresenter: ISportInformationPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        supportActionBar?.hide()

        sessionManager = SessionManager.getInstance(context)
        setListeners()
        setRecyclerView()
        if (sessionManager?.getSkillDetailModel() != null && sessionManager?.getSkillDetailModel()?.learningSecondQuestionModel != null) {
            setQuestion(sessionManager?.getSkillDetailModel()?.learningSecondQuestionModel)
        }

        PreferencesManager.initializeInstance(this)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        iSportInformationPresenter = SportInformationPresenterImplementer(this)

    }

    private fun setQuestion(model: SkillDetailsResponsePOJO.DataItem?) {
        list = model?.subSkillAnswers!!
        binding.txtQuestion.text = model.name
        binding.txtQuestion.textSize =25F

        mAdapter?.updateAdapter(list)
        ProjectUtils.dismissProgressDialog()
    }

    private fun setRecyclerView() {
        mAdapter = SportsInformationAdapter(
            context,
            list,
            object : SportsInformationAdapter.SportsInformationAdapterInterface {
                override fun onItemSelected(
                    position: Int,
                    model: SkillDetailsResponsePOJO.SubSkillAnswersModel
                ) {
                    isAnswered = true
                    selectedAnswersModel = model
                    changeColorLogic(model)
                }

            })

        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun changeColorLogic(model: SkillDetailsResponsePOJO.SubSkillAnswersModel) {
        for (answerModel in list) {
            answerModel.checked = model.id == answerModel.id
        }
        mAdapter?.updateAdapter(list)
    }

    private fun setListeners() {
        binding.apply {

            btnNext.setOnClickListener {
                if (ProjectUtils.checkInternetAvailable(this@LearningFeedbackActivity)!!) {

                    if (isAnswered) {
                    if (!isLastQuestion) {
                        if (selectedAnswersModel!!.weightage.equals("10")) {
                            showAlertForWeekTraining(selectedAnswersModel!!.answerFeedback)
                        } else {

                                iSportInformationPresenter?.submitAnswer(
                                    token,
                                    sessionManager?.getSkillDetailModel()?.learningSecondQuestionModel?.id.toString(),
                                    selectedAnswersModel?.id.toString()
                                )


                        }

                    } else {
                        showAlertForWeekTraining(selectedAnswersModel!!.answerFeedback)


                    }
                } else {
                    Toast.makeText(context, "Please give answer.", Toast.LENGTH_SHORT).show()
                }
                } else {
                    ProjectUtils.showToast(
                        this@LearningFeedbackActivity,
                        getString(R.string.no_internet_connection)
                    )

                }

            }
        }
    }


    private fun showAlertForWeekTraining(answerFeedback: String?) {
        val alertDialog = AlertDialog.Builder(this@LearningFeedbackActivity)
        val customLayout: View? =
            getLayoutInflater().inflate(R.layout.custom_answer_feedback, null)
        alertDialog.setView(customLayout)

        val btnNo: Button = customLayout!!.findViewById(R.id.btnNo)
        val txtAnswerFeedback: TextView = customLayout!!.findViewById(R.id.txtAnswerFeedback)


        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(false)
        binding.rlMain.alpha = 0.1f
        alert.show()
        txtAnswerFeedback.text = answerFeedback


        btnNo.setOnClickListener(View.OnClickListener {
            binding.rlMain.alpha = 1.0f

            if (ProjectUtils.checkInternetAvailable(this@LearningFeedbackActivity)!!) {

                alert.dismiss()
            alert.cancel()


                if (!isLastQuestion) {
                //1 10//

                    preferencesManager!!.setIntegerValue(Constants.USER_LEARNING_POINT.toString(),10)
                iSportInformationPresenter?.submitAnswer(token, sessionManager?.getSkillDetailModel()?.learningSecondQuestionModel?.id.toString(), selectedAnswersModel?.id.toString())
                iSportInformationPresenter?.progressQuiz(token,10,sessionManager!!.getSkillDetailModel()?.learningVideoModel!!.mental_skill_id)
            } else
            {
                    //2  5//
                    preferencesManager!!.setIntegerValue(
                        Constants.USER_LEARNING_POINT.toString(),
                        5
                    )

                    iSportInformationPresenter?.submitAnswer(
                        token,
                        sessionManager?.getSkillDetailModel()?.learningThirdQuestionModel?.id.toString(),
                        selectedAnswersModel?.id.toString()
                    )

                    iSportInformationPresenter?.progressQuiz(
                        token,
                        5,
                        sessionManager!!.getSkillDetailModel()?.learningVideoModel!!.mental_skill_id
                    )

                }

            } else {
                ProjectUtils.showToast(
                    this@LearningFeedbackActivity,
                    getString(R.string.no_internet_connection)
                )

            }



        })


    }

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun submitAnswerSuccess(body: SportInformationModel?) {
        if (!isLastQuestion && !selectedAnswersModel?.weightage.equals("10", ignoreCase = true)) {
            ProjectUtils.showProgressDialog(context)
            setQuestion(sessionManager?.getSkillDetailModel()?.learningThirdQuestionModel)
            isLastQuestion = true
            isAnswered = false
        } else {

                iSportInformationPresenter?.updateSkillStatus(
                    token,
                    sessionManager?.getSkillDetailModel()?.learningThirdQuestionModel?.id.toString(),
                    "SubSkill"
                )


        }
    }

    override fun updateSkillStatus(body: SportInformationModel?) {
        val intent = Intent(applicationContext, FinishLearningActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun addToMyTrainingSuccess(body: SportInformationModel?) {

    }

    override fun addToTrainingSchedule(body: SportInformationModel?) {

    }

    override fun updateTrainingSchedule(body: SportInformationModel?) {

    }

    override fun progressQuizSuccess(body: OnlyMessageResponsePOJO?) {
    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(this)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(this, appErrorMessage, Toast.LENGTH_LONG)
            .show()
    }
}
