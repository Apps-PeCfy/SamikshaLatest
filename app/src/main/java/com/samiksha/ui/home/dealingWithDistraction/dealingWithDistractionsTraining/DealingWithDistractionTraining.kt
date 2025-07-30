package com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsTraining

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.samiksha.databinding.ActivityDealingWithDistractionTrainingBinding
import com.samiksha.ui.home.main.HomeActivity

class DealingWithDistractionTraining : AppCompatActivity() {
   /* @JvmField
    @BindView(R.id.toolbar)
    var mToolbar: Toolbar? = null
*/

    lateinit var binding: ActivityDealingWithDistractionTrainingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_dealing_with_distraction_training)
     //   ButterKnife.bind(this)
        binding = ActivityDealingWithDistractionTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar!!.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.hide()
        binding.rlMain.visibility = View.VISIBLE
    }



    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this@DealingWithDistractionTraining, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)

    }

}
