package com.samiksha.ui.informationScreen.chooseUnderPressure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivityChooseUnderPressureBinding
import com.samiksha.ui.informationScreen.chooseGoals.ChooseGoalsActivity
import com.samiksha.ui.informationScreen.informationFinish.InformationFinishActivity
import com.samiksha.utils.ProjectUtils

class ChooseUnderPressureActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseUnderPressureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseUnderPressureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListners()
        supportActionBar?.hide()
    }

    private fun setListners() {
        binding.btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)

            startActivity(intent)
            finish()

        })
        binding.btnNext.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, InformationFinishActivity::class.java)

            startActivity(intent)
            finish()

        })
    }

  /*  @OnClick(R.id.btnBack)
    fun btnBack() {

        val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)

        startActivity(intent)
        finish()


    }
    @OnClick(R.id.btnNext)
    fun btnNext() {

        val intent = Intent(applicationContext, InformationFinishActivity::class.java)

        startActivity(intent)
        finish()


    }*/



    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

}
