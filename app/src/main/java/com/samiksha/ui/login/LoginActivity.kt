package com.samiksha.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.samiksha.R
import com.samiksha.databinding.ActivityLoginBinding
import com.samiksha.ui.drawer.privacypolicy.ContentPageActivity
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.RegisterACtivity
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.ui.totorial.MainScreen
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils


class LoginActivity : AppCompatActivity() {

    var isLogin: String? = null
    var userRole: String? = null



  /*  @JvmField
    @BindView(R.id.btnLogin)
    var btnLogin: Button? = null

    @JvmField
    @BindView(R.id.txtContinueWithoutLogin)
    var txtContinueWithoutLogin: TextView? = null

    @JvmField
    @BindView(R.id.btnCreateAccount)
    var btnCreateAccount: Button? = null*/

    private var mPreferencesManager: PreferencesManager? = null

    private lateinit var referrerClient: InstallReferrerClient
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_login)
      //  ButterKnife.bind(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        PreferencesManager.initializeInstance(this@LoginActivity)
        mPreferencesManager = PreferencesManager.instance

     //   refferClient()

        setListners()
        if (mPreferencesManager!!.getBooleanValue(
                Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                true
            )
        ) {


            Handler().postDelayed(Runnable {
                val intent = Intent(applicationContext, MainScreen::class.java)
                startActivity(intent)
                finish()
            }, 2000)


        } else {

            Handler().postDelayed(Runnable {
                val i = Intent(this@LoginActivity, RegisterACtivity::class.java)
                startActivity(i)
                finish()
            }, 1000)
            /*isLogin = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_ISLOGIN)
            userRole = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)

            if(isLogin == "true" && userRole.equals("MasterUser") ){
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else if(isLogin == "true"
                && userRole.equals("Coach")
                || userRole.equals("Counsellor")
                || userRole.equals("SuperCounsellor")){

                val intent = Intent(applicationContext, AcademyMembersActivity::class.java)
                startActivity(intent)
                finish()

            }*/

        }


    }
    private fun setListners() {
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            startActivity(intent)
        })
        binding.btnCreateAccount.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            startActivity(intent)
        })
        binding.tvPrivacy.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ContentPageActivity::class.java)
            intent.putExtra("ContentPageData", "Privacy Policy")
            startActivity(intent)
        })
        binding.tvTerms.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ContentPageActivity::class.java)
            intent.putExtra("ContentPageData", "Terms of Use")
            startActivity(intent)
        })
    }

    private fun refferClient() {
        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        Log.d("InstallReferrTest", "OK")
                        val response: ReferrerDetails = referrerClient.installReferrer
                        val referrerUrl: String = response.installReferrer
                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
                        val appInstallTime: Long = response.installBeginTimestampSeconds
                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam

                        Log.d(
                            "InstallReferrTest1",
                            referrerUrl + " " + referrerClickTime + " " + appInstallTime + " " + instantExperienceLaunched
                        )

                        // Connection established.
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d("InstallReferrTest", "FEATURE_NOT_SUPPORTED")
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Log.d("InstallReferrTest", "SERVICE_UNAVAILABLE")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }


   /* @OnClick(R.id.btnLogin)
    fun btnLogin() {

        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        startActivity(intent)


    }

    @OnClick(R.id.btnCreateAccount)
    fun btnCreateAccount() {

        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        startActivity(intent)


    }


    @OnClick(R.id.tvPrivacy)
    fun tvPrivacy() {

        val intent = Intent(applicationContext, ContentPageActivity::class.java)
        intent.putExtra("ContentPageData", "Privacy Policy")
        startActivity(intent)


    }

    @OnClick(R.id.tvTerms)
    fun tvTerms() {

        val intent = Intent(applicationContext, ContentPageActivity::class.java)
        intent.putExtra("ContentPageData", "Terms of Use")
        startActivity(intent)


    }*/


    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this@LoginActivity)
    }
}