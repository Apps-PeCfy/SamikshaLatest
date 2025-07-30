package com.samiksha.ui.infoAfterLearning.SportInformation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.samiksha.R
import com.samiksha.databinding.ActivitySportInformationBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.infoAfterLearning.Learning.LearningActivity
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager

class SportInformation : AppCompatActivity(), ISportInformationView {
    lateinit var binding: ActivitySportInformationBinding
    private var context: Context = this
    private var mAdapter: SportsInformationAdapter? = null
    private var list: List<SkillDetailsResponsePOJO.SubSkillAnswersModel> = ArrayList()
    private var learningModel: SkillDetailsResponsePOJO.DataItem? = null
    private var selectedAnswersModel: SkillDetailsResponsePOJO.SubSkillAnswersModel? = null
    private var isAnswered: Boolean = false
    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    private var iSportInformationPresenter: ISportInformationPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySportInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        supportActionBar?.hide()
        learningModel = Gson().fromJson(
            intent.getStringExtra("data"),
            SkillDetailsResponsePOJO.DataItem::class.java
        )

        if (learningModel != null) {
            list = learningModel?.subSkillAnswers!!

            binding.txtQuestion.visibility = View.GONE
            binding.txtQuestion.text = learningModel?.name
            binding.txtQuestion.textSize = 25F
        }

        PreferencesManager.initializeInstance(this)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        iSportInformationPresenter = SportInformationPresenterImplementer(this)

        setListeners()
        setRecyclerView()
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
                    selectedAnswersModel = model
                    isAnswered = true
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
            btnCancel.setOnClickListener {
                if (ProjectUtils.checkInternetAvailable(this@SportInformation)!!) {
                    finish()
                } else {
                    ProjectUtils.showToast(
                        this@SportInformation,
                        getString(R.string.no_internet_connection)
                    )

                }
            }

            btnSubmit.setOnClickListener {
                if (ProjectUtils.checkInternetAvailable(this@SportInformation)!!) {

                    if (isAnswered) {
                        iSportInformationPresenter?.submitAnswer(
                            token,
                            learningModel?.id.toString(),
                            selectedAnswersModel?.id.toString()
                        )


                    } else {
                        Toast.makeText(context, "Please give answer.", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    ProjectUtils.showToast(
                        this@SportInformation,
                        getString(R.string.no_internet_connection)
                    )

                }

            }
        }
    }


    override fun onBackPressed() {
        if (ProjectUtils.checkInternetAvailable(this@SportInformation)!!) {

            ProjectUtils.showAlertDialog(this)

        } else {
            ProjectUtils.showToast(
                this@SportInformation,
                getString(R.string.no_internet_connection)
            )

        }
    }

    override fun submitAnswerSuccess(body: SportInformationModel?) {
        SessionManager.getInstance(context).setPlayerPosition(0L)
        val intent = Intent(applicationContext, LearningActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun updateSkillStatus(body: SportInformationModel?) {

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
