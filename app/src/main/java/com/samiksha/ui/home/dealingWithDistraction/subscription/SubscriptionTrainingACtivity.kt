package com.samiksha.ui.home.dealingWithDistraction.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivitySubscriptionTrainingBinding
import com.samiksha.ui.home.main.HomeActivity

class SubscriptionTrainingACtivity : AppCompatActivity() {

   /* @JvmField
    @BindView(R.id.toolbar)
    var mToolbar: Toolbar? = null*/

    lateinit var binding: ActivitySubscriptionTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_subscription_training)
     //   ButterKnife.bind(this)
        binding = ActivitySubscriptionTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.hide()
    }



    override fun onBackPressed() {
        val intent = Intent(this@SubscriptionTrainingACtivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)

    }

}
