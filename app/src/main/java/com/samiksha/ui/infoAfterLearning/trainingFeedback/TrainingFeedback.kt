package com.samiksha.ui.infoAfterLearning.trainingFeedback

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.samiksha.R
import com.samiksha.databinding.ActivityTrainingFeedbackBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.infoAfterLearning.Learning.RewardActivity
import com.samiksha.ui.infoAfterLearning.SportInformation.ISportInformationPresenter
import com.samiksha.ui.infoAfterLearning.SportInformation.ISportInformationView
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformationModel
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformationPresenterImplementer
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class TrainingFeedback : AppCompatActivity(), ISportInformationView {
    lateinit var binding: ActivityTrainingFeedbackBinding
    private var context: Context = this
    private var mAdapter: TrainingFeedbackAdapter? = null
    private var list: List<SkillDetailsResponsePOJO.SubSkillAnswersModel> = ArrayList()
    private var trainingQuestionModel: SkillDetailsResponsePOJO.DataItem? = null
    private var selectedAnswersModel: SkillDetailsResponsePOJO.SubSkillAnswersModel? = null
    private var isAnswered: Boolean = false
    private var isLastQuestion: Boolean = false
    var token: String? = null
    var buttonClick: String? = "Yes"

    var preferencesManager: PreferencesManager? = null
    var sessionManager: SessionManager? = null

    private var iSportInformationPresenter: ISportInformationPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        supportActionBar?.hide()
        sessionManager = SessionManager.getInstance(context)
        trainingQuestionModel = sessionManager?.getSkillDetailModel()?.trainingQuestionModel

        setQuestion(trainingQuestionModel)

        PreferencesManager.initializeInstance(this)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        iSportInformationPresenter = SportInformationPresenterImplementer(this)


        setListeners()
        setRecyclerView()
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

                if (ProjectUtils.checkInternetAvailable(this@TrainingFeedback)!!) {

                    if (isAnswered) {
                        if (!isLastQuestion) {
                            iSportInformationPresenter?.submitAnswer(
                                token,
                                trainingQuestionModel?.id.toString(),
                                selectedAnswersModel?.id.toString()
                            )

                        } else {
                            iSportInformationPresenter?.submitAnswer(
                                token,
                                sessionManager?.getSkillDetailModel()?.trainingSecondQuestionModel?.id.toString(),
                                selectedAnswersModel?.id.toString()
                            )

                        }
                    } else {
                        Toast.makeText(context, "Please select your option.", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    ProjectUtils.showToast(
                        this@TrainingFeedback,
                        getString(R.string.no_internet_connection)
                    )

                }

            }
        }
    }

    /* private fun showAlertForWeekTraining() {
         val alertDialog = AlertDialog.Builder(context)
         alertDialog.setCancelable(false)
         alertDialog.setTitle(getString(R.string.app_name))
         alertDialog.setMessage("Add in Training path ?")
         alertDialog.setPositiveButton("Yes") { dialog, which ->
             dialog.dismiss()
             buttonClick="Yes"
             iSportInformationPresenter?.addToTrainingSchedule(token, trainingQuestionModel?.mental_skill_id, trainingQuestionModel?.group)
         }
         alertDialog.setNegativeButton("No") { dialog, which ->
             dialog.dismiss()
             buttonClick="No"
             iSportInformationPresenter?.addToMyTraining(token, trainingQuestionModel?.mental_skill_id)
         }

         alertDialog.show()
     }
 */


    private fun showAlertForWeekTraining() {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@TrainingFeedback)
        val customLayout: android.view.View? =
            getLayoutInflater().inflate(R.layout.custom_training_feedback_dialog, null)
        alertDialog.setView(customLayout)

        val btnYes: Button = customLayout!!.findViewById(R.id.btnYes)
        val btnNo: Button = customLayout!!.findViewById(R.id.btnNo)


        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(false)
        alert.show()


        btnYes.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()

                buttonClick = "Yes"
                iSportInformationPresenter?.addToTrainingSchedule(
                    token,
                    trainingQuestionModel?.mental_skill_id,
                    trainingQuestionModel?.group
                )


        })

        btnNo.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()

                buttonClick = "No"
                iSportInformationPresenter?.addToMyTraining(
                    token,
                    trainingQuestionModel?.mental_skill_id
                )


        })


    }


    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun submitAnswerSuccess(body: SportInformationModel?) {
        if (!isLastQuestion) {
            ProjectUtils.showProgressDialog(context)
            setQuestion(sessionManager?.getSkillDetailModel()?.trainingSecondQuestionModel)
            isLastQuestion = true
            isAnswered = false
        } else {

                if (sessionManager?.getSkillDetailModel()?.isFromTrainingPath!!) {
                    iSportInformationPresenter?.updateTrainingSchedule(
                        token,
                        trainingQuestionModel?.mental_skill_id
                    )
                } else {
                    // showAlertForWeekTraining()

                    iSportInformationPresenter?.addToTrainingSchedule(
                        token,
                        trainingQuestionModel?.mental_skill_id,
                        trainingQuestionModel?.group
                    )
                }

        }
    }

    override fun updateSkillStatus(body: SportInformationModel?) {

    }

    override fun addToMyTrainingSuccess(body: SportInformationModel?) {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.action = "RewardFragment"
        startActivity(intent)
    }

    override fun addToTrainingSchedule(body: SportInformationModel?) {
            iSportInformationPresenter?.addToMyTraining(
                token,
                trainingQuestionModel?.mental_skill_id
            )
        /*   val intent = Intent(applicationContext, HomeActivity::class.java)
           intent.action = "RewardFragment"
           startActivity(intent)
       */
    }

    override fun updateTrainingSchedule(body: SportInformationModel?) {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.action = "RewardFragment"
        startActivity(intent)

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
