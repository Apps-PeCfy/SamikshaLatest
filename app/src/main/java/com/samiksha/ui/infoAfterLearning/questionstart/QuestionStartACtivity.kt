package com.samiksha.ui.infoAfterLearning.questionstart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samiksha.R
import com.samiksha.databinding.ActivityQuestionStartBinding
import com.samiksha.ui.infoAfterLearning.feedback.LearningFeedbackActivity
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager

class QuestionStartACtivity : AppCompatActivity() {
    lateinit var binding: ActivityQuestionStartBinding
    private var context: Context = this
    private var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        supportActionBar?.hide()

        sessionManager = SessionManager.getInstance(context)
        setData()
        setListeners()
    }

    private fun setData() {
        binding.txtName.text = "Great work, ${sessionManager?.getUserModel()?.firstName}"
    }

    private fun setListeners() {
        binding.apply {
            btnGetStarted.setOnClickListener {

                if (ProjectUtils.checkInternetAvailable(this@QuestionStartACtivity)!!) {

                    val intent = Intent(applicationContext, LearningFeedbackActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    ProjectUtils.showToast(
                        this@QuestionStartACtivity,
                        getString(R.string.no_internet_connection)
                    )

                }
            }
        }
    }


    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }
}
