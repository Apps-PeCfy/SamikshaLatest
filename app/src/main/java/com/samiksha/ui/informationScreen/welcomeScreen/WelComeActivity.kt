package com.samiksha.ui.informationScreen.welcomeScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivityWelComeBinding
import com.samiksha.ui.informationScreen.chooseSport.ChooseSportActivity
import com.samiksha.ui.register.RegisterACtivity
import com.samiksha.utils.ProjectUtils

class WelComeActivity : AppCompatActivity() {

    var userMobileNo: String? = null
    var loginType: String? = null
    var userFirstName: String? = null
    var userLastName: String? = null
    var userEmail: String? = null
    var userState: String? = null
    var userGender: String? = null
    var userOTP: String? = null
    var userCurrentState: String? = null
    var userUserCountryCOde: String? = null



  /*  @JvmField
    @BindView(R.id.tvWelCome)
    var tvWelCome: TextView? = null

 @JvmField
    @BindView(R.id.btnNotUser)
    var btnNotUser: Button? = null*/


    lateinit var binding: ActivityWelComeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_wel_come)
      //  ButterKnife.bind(this)
        binding = ActivityWelComeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        loginType = intent.getStringExtra("LoginType")
        userMobileNo = intent.getStringExtra("userMobileNo")
        userFirstName = intent.getStringExtra("userFirstName")
        userLastName = intent.getStringExtra("userLastName")
        userEmail = intent.getStringExtra("userEmail")
        userState = intent.getStringExtra("userState")
        userGender = intent.getStringExtra("userGender")
        userOTP = intent.getStringExtra("userOTP")
        userCurrentState = intent.getStringExtra("userCurrentState")
        userUserCountryCOde = intent.getStringExtra("userUserCountryCOde")

        binding.tvWelCome.text ="Welcome, "+userFirstName
        binding.btnNotUser.text ="Not "+userFirstName+"? Login as a different user"


        setListners()

    }
    private fun setListners() {
        binding.btnGetStarted.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ChooseSportActivity::class.java)

            intent.putExtra("LoginType", loginType)
            intent.putExtra("userFirstName", userFirstName)
            intent.putExtra("userLastName", userLastName)
            intent.putExtra("userEmail", userEmail)
            intent.putExtra("userMobileNo", userMobileNo)
            intent.putExtra("userState", userState)
            intent.putExtra("userGender", userGender)
            intent.putExtra("userOTP", userOTP)
            intent.putExtra("userCurrentState", userCurrentState)
            intent.putExtra("userUserCountryCOde", userUserCountryCOde)
            startActivity(intent)
            finish()
        })
        binding.btnNotUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            intent.putExtra("btnText", "WelCome")
            startActivity(intent)
            finish()
        })
    }



   /* @OnClick(R.id.btnGetStarted)
    fun btnOTP() {

        val intent = Intent(applicationContext, ChooseSportActivity::class.java)

        intent.putExtra("LoginType", loginType)
        intent.putExtra("userFirstName", userFirstName)
        intent.putExtra("userLastName", userLastName)
        intent.putExtra("userEmail", userEmail)
        intent.putExtra("userMobileNo", userMobileNo)
        intent.putExtra("userState", userState)
        intent.putExtra("userGender", userGender)
        intent.putExtra("userOTP", userOTP)
        intent.putExtra("userCurrentState", userCurrentState)
        intent.putExtra("userUserCountryCOde", userUserCountryCOde)
        startActivity(intent)
        finish()


    }



    @OnClick(R.id.btnNotUser)
    fun btnNotUser() {

        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        intent.putExtra("btnText", "WelCome")
        startActivity(intent)
        finish()


    }
*/



    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

}
