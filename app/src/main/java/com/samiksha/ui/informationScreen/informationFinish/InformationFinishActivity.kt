package com.samiksha.ui.informationScreen.informationFinish

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.samiksha.R
import com.samiksha.databinding.ActivityInformationFinishBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.ProjectUtils


class InformationFinishActivity : AppCompatActivity() {

   /* @JvmField
    @BindView(R.id.ivLoading)
    var ivLoading: ImageView? = null
*/
   lateinit var binding: ActivityInformationFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_information_finish)
      //  ButterKnife.bind(this)
        binding = ActivityInformationFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Glide.with(this).load(R.drawable.loading).into(binding.ivLoading!!)
        Handler().postDelayed(Runnable {
            val i = Intent(this@InformationFinishActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        }, 2000)



    }


    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }
}
