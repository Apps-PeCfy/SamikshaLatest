package com.samiksha.ui.infoAfterLearning.Learning

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.samiksha.R
import com.samiksha.databinding.ActivityRewardBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.ProjectUtils

class RewardActivity : AppCompatActivity() {

   /* @JvmField
    @BindView(R.id.toolbarReward)
    var toolbarReward: Toolbar? = null

    @JvmField
    @BindView(R.id.imgReward)
    var imgReward: ImageView? = null

    @JvmField
    @BindView(R.id.btnRedeemPoint)
    var btnTeainingPath: Button? = null

    @JvmField
    @BindView(R.id.btnExploreMental)
    var btnExploreMental: Button? = null
*/
    var AlertClick: String? = null
    lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_reward)
       // ButterKnife.bind(this)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarGradiant(this)
        AlertClick = intent.getStringExtra("AlertClick")
      /*  if (AlertClick.equals("Yes")) {
            btnTeainingPath!!.visibility = View.GONE
        } else {
            btnTeainingPath!!.visibility = View.GONE
        }
*/
        Glide.with(this).load(R.drawable.img_reward).into(binding.imgReward!!)

        initView()


    }

    private fun setStatusBarGradiant(activity: RewardActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }


    private fun initView() {

        binding.toolbarReward!!.title = ""
        binding.toolbarReward!!.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbarReward)
        binding.toolbarReward!!.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)
        setListners()

    }

    private fun setListners() {
        binding.actionHome.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        })
        binding.btnRedeemPoint.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent!!.action = "MyAccountBack"
            startActivity(intent)

        })
        binding.btnExploreMental.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        })
    }



    /* @OnClick(R.id.actionHome)
     fun actionHome() {
         val intent = Intent(this, HomeActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
         startActivity(intent)

     }

     @OnClick(R.id.btnRedeemPoint)
     fun btnTeainingPath() {
         val intent = Intent(this, HomeActivity::class.java)
         intent!!.action = "MyAccountBack"
         startActivity(intent)


     }

 @OnClick(R.id.btnExploreMental)
     fun btnExploreMental() {
     val intent = Intent(this, HomeActivity::class.java)
     intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
     startActivity(intent)

     }
 */

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }


}
