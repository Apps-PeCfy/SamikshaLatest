package com.samiksha.ui.home.dealingWithDistraction.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivitySubscriptionLearningBinding
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformation
import com.samiksha.utils.ProjectUtils

class SubscriptionLearningACtivity : AppCompatActivity() {
/*

    @JvmField
    @BindView(R.id.toolbar)
    var mToolbar: Toolbar? = null
*/

    lateinit var binding: ActivitySubscriptionLearningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_subscription_learning)
        //  ButterKnife.bind(this)
        binding = ActivitySubscriptionLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.hide()

        setListners()
    }
    private fun setListners() {
        binding.rlLearning.setOnClickListener(View.OnClickListener {
            ProjectUtils.s1 ="Paid"


            val intent = Intent(applicationContext, SportInformation::class.java)
            startActivity(intent)

        })
    }
/*

    @OnClick(R.id.rlLearning)
    fun rlLearning() {

        ProjectUtils.s1 ="Paid"


        val intent = Intent(applicationContext, SportInformation::class.java)
        startActivity(intent)


    }

    @OnClick(R.id.rlTraining)
    fun rlTraining() {



    }
*/

}
