package com.samiksha.ui.infoAfterLearning.finishLearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samiksha.R
import com.samiksha.databinding.ActivityFinishLearningBinding
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager

class FinishLearningActivity : AppCompatActivity() {
    lateinit var binding: ActivityFinishLearningBinding
    private var context: Context = this
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishLearningBinding.inflate(layoutInflater)
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
        binding.txtSkillDescription.text = "It's time for you to start your training."

    }

    private fun setListeners() {
        binding.apply {
            btnBackToModule.setOnClickListener {

                if (ProjectUtils.checkInternetAvailable(this@FinishLearningActivity)!!) {

                    val intent =
                        Intent(applicationContext, DealingWithDistractionsActivity::class.java)
                    intent.putExtra(
                        "skill_id",
                        sessionManager?.getSkillDetailModel()?.learningVideoModel?.mental_skill_id
                    )
                    startActivity(intent)
                    finish()
                } else {
                    ProjectUtils.showToast(
                        this@FinishLearningActivity,
                        getString(R.string.no_internet_connection)
                    )

                }
            }

            btnStartTraining.setOnClickListener {
                if (ProjectUtils.checkInternetAvailable(this@FinishLearningActivity)!!) {

                    val intent = Intent(applicationContext, TrainingStartActivity::class.java)

                    startActivity(intent)

                } else {
                    ProjectUtils.showToast(
                        this@FinishLearningActivity,
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
