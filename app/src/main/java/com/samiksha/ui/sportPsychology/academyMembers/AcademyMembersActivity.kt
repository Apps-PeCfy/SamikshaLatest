package com.samiksha.ui.sportPsychology.academyMembers

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.ActivityAcademyMembersBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.home.pojo.SignOutResponsePOJO
import com.samiksha.ui.login.LoginActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.ExpertMembers.ExpertMemberFragment
import com.samiksha.ui.sportPsychology.TrainingPath.TrainingPathFragmentCoach
import com.samiksha.ui.sportPsychology.profileDetails.ProfileDetailsFragment
import com.samiksha.ui.sportPsychology.progress.ProgressFragmentCoach
import com.samiksha.ui.sportPsychology.reports.ReportsFragment
import com.samiksha.ui.sportPsychology.session.EditSessionFragment
import com.samiksha.ui.sportPsychology.session.MySessionCoach
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class AcademyMembersActivity : AppCompatActivity() {

    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null

  /*  @JvmField
    @BindView(R.id.toolbar_home)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.tvLogout)
    var tvLogout: ImageView? = null

    @JvmField
    @BindView(R.id.ivSwap)
    var ivSwap: ImageView? = null

    @JvmField
    @BindView(R.id.bottomLayout)
    var bottomLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.rlTrainingPath)
    var rlTrainingPath: RelativeLayout? = null




    @JvmField
    @BindView(R.id.tvCommunity)
    var tvCommunity: TextView? = null

    @JvmField
    @BindView(R.id.tvEvent)
    var tvEvent: TextView? = null

    @JvmField
    @BindView(R.id.tvProgress)
    var tvProgress: TextView? = null

    @JvmField
    @BindView(R.id.tvReport)
    var tvReport: TextView? = null

   */

    var userRole: String? = null
    var token: String? = null
    var deviceID: String? = null

    private var mPreferencesManager: PreferencesManager? = null
    private var sessionManager: SessionManager? = null
    lateinit var binding: ActivityAcademyMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_academy_members)
      //  ButterKnife.bind(this)
        binding = ActivityAcademyMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarGradiant(this)
        PreferencesManager.initializeInstance(this@AcademyMembersActivity)
        mPreferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(this)
        userRole = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)



        init()


    }


    private fun setStatusBarGradiant(activity: AcademyMembersActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }

    private fun init() {

        if (userRole.equals("Coach")) {
            binding.bottomLayout.visibility = View.GONE
            binding.toolbarHome.title = "Players"
        } else if (userRole.equals("SuperCounsellor")) {
            binding.bottomLayout.visibility = View.GONE
            binding.toolbarHome.title = "Counselors"

        } else {
            binding.bottomLayout.visibility = View.GONE
            binding.toolbarHome.title = "Players"

        }


        binding.toolbarHome.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbarHome)
        binding.toolbarHome.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)




        val action = intent.action

        if (action == "AcademyMembers") {
            binding.toolbarHome.title = "Profile Details"
            binding.bottomLayout.visibility = View.VISIBLE
            binding.tvLogout.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            replaceFragment(ProfileDetailsFragment())


        } else if (action == "ExpertMember") {
            binding.toolbarHome.title = "Players"
            binding.tvLogout.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            replaceFragment(AcademyMembersFragment())


        } else if (action == "EditSession") {
            binding.toolbarHome.title = " Session "
            binding.tvLogout.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            replaceFragment(EditSessionFragment())

        } else if (action == "Sessions") {
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.tvReport.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.title = "Sessions"

            binding.tvLogout.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            replaceFragment(MySessionCoach())

        } else if (userRole.equals("SuperCounsellor")) {
            binding.toolbarHome.title = "Counselors"
            binding.tvLogout.visibility = View.VISIBLE
            checkUserType()
            replaceFragment(ExpertMemberFragment())

        } else {
            binding.tvLogout.visibility = View.VISIBLE
            checkUserType()
            replaceFragment(AcademyMembersFragment())

        }

        setListeners()
    }


    private fun setListeners() {
        binding.ivSwap.setOnClickListener {
            if (ProjectUtils.checkInternetAvailable(this@AcademyMembersActivity)!!) {
                switchUser()
            } else {
                ProjectUtils.showToast(
                    this@AcademyMembersActivity,
                    getString(R.string.no_internet_connection)
                )
            }
        }

        binding.tvLogout.setOnClickListener(View.OnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setCancelable(false)
            alertDialog.setTitle(this.getString(R.string.app_name))
            alertDialog.setMessage("Are you sure, you want to logout?")
            alertDialog.setPositiveButton("No") { dialog, which -> dialog.cancel() }
            alertDialog.setNegativeButton("Yes") { dialog, which ->
                deviceID =
                    mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
                token = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


                val progressDialogLogout = ProgressDialog(this)
                progressDialogLogout.setCancelable(false) // set cancelable to false
                progressDialogLogout.setMessage("Please Wait") // set message
                progressDialogLogout.show() // show progress dialog

                ApiClient.client.signOut("Bearer $token", deviceID, "Android")!!
                    .enqueue(object : Callback<SignOutResponsePOJO?> {
                        override fun onResponse(
                            call: Call<SignOutResponsePOJO?>,
                            response: Response<SignOutResponsePOJO?>
                        ) {
                            progressDialogLogout.dismiss()
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    mPreferencesManager!!.clear()


                                    mPreferencesManager!!.setBooleanValue(
                                        Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                                        false
                                    )


                                    val intent =
                                        Intent(this@AcademyMembersActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                            } else if (response.code() == 422) {
                                val gson = GsonBuilder().create()
                                var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                                try {
                                    pojo = gson.fromJson(
                                        response.errorBody()!!.string(),
                                        ValidationResponsePOJO::class.java
                                    )
                                    Toast.makeText(
                                        this@AcademyMembersActivity,
                                        pojo.errors!!.get(0).message,
                                        Toast.LENGTH_LONG
                                    ).show()


                                } catch (exception: IOException) {
                                }

                            } else {
                                Toast.makeText(
                                    this@AcademyMembersActivity,
                                    "Internal Server Error",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }

                        override fun onFailure(
                            call: Call<SignOutResponsePOJO?>, t: Throwable
                        ) {
                            progressDialogLogout.dismiss()
                            Toast.makeText(
                                this@AcademyMembersActivity,
                                t.message,
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }
                    })
            }





            alertDialog.show()

        })

        binding.btnProgress.setOnClickListener(View.OnClickListener {
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvReport.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.title = "Progress"
            replaceFragment(ProgressFragmentCoach())

        })
        binding.btnReports.setOnClickListener(View.OnClickListener {

            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.tvReport.setTextColor(Color.parseColor("#FC6D2D"))
            binding.toolbarHome.title = "Reports"
            replaceFragment(ReportsFragment())

        })
        binding.btnAccount.setOnClickListener(View.OnClickListener {

            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.tvReport.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.title = "Sessions"
            replaceFragment(MySessionCoach())
        })
        binding.btnTrainingPath.setOnClickListener(View.OnClickListener {
            binding.tvCommunity.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.tvReport.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.title = "Training Path"
            replaceFragment(TrainingPathFragmentCoach())

        })
    }


    fun replaceFragment(fragment: Fragment?) {
        if (ProjectUtils.checkInternetAvailable(this@AcademyMembersActivity)!!) {

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.container_framelayout, fragment!!)
        fragmentTransaction!!.commit()
        } else {
            ProjectUtils.showToast(
                this@AcademyMembersActivity,
                getString(R.string.no_internet_connection)
            )
        }
    }


  /*  @OnClick(R.id.tvLogout)
    fun btnLogout() {


        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(this.getString(R.string.app_name))
        alertDialog.setMessage("Are you sure, you want to logout?")
        alertDialog.setPositiveButton("No") { dialog, which -> dialog.cancel() }
        alertDialog.setNegativeButton("Yes") { dialog, which ->
            deviceID =
                mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
            token = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


            val progressDialogLogout = ProgressDialog(this)
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog

            ApiClient.client.signOut("Bearer $token", deviceID, "Android")!!
                .enqueue(object : Callback<SignOutResponsePOJO?> {
                    override fun onResponse(
                        call: Call<SignOutResponsePOJO?>,
                        response: Response<SignOutResponsePOJO?>
                    ) {
                        progressDialogLogout.dismiss()
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                mPreferencesManager!!.clear()


                                mPreferencesManager!!.setBooleanValue(
                                    Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                                    false
                                )


                                val intent =
                                    Intent(this@AcademyMembersActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                        } else if (response.code() == 422) {
                            val gson = GsonBuilder().create()
                            var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                            try {
                                pojo = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ValidationResponsePOJO::class.java
                                )
                                Toast.makeText(
                                    this@AcademyMembersActivity,
                                    pojo.errors!!.get(0).message,
                                    Toast.LENGTH_LONG
                                ).show()


                            } catch (exception: IOException) {
                            }

                        } else {
                            Toast.makeText(
                                this@AcademyMembersActivity,
                                "Internal Server Error",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onFailure(
                        call: Call<SignOutResponsePOJO?>, t: Throwable
                    ) {
                        progressDialogLogout.dismiss()
                        Toast.makeText(
                            this@AcademyMembersActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
                })
        }





        alertDialog.show()


    }*/


   /* @OnClick(R.id.btnProgress)
    fun btnProgress() {

            tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
            tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
            tvProgress!!.setTextColor(Color.parseColor("#FC6D2D"))
            tvReport!!.setTextColor(Color.parseColor("#ffffff"))
            toolbar?.title = "Progress"
            replaceFragment(ProgressFragmentCoach())



    }

    @OnClick(R.id.btnReports)
    fun btnReports() {
            tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
            tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
            tvProgress!!.setTextColor(Color.parseColor("#ffffff"))
            tvReport!!.setTextColor(Color.parseColor("#FC6D2D"))
            toolbar?.title = "Reports"
            replaceFragment(ReportsFragment())



    }

    @OnClick(R.id.btnAccount)
    fun btnAccount() {
            tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
            tvEvent!!.setTextColor(Color.parseColor("#FC6D2D"))
            tvProgress!!.setTextColor(Color.parseColor("#ffffff"))
            tvReport!!.setTextColor(Color.parseColor("#ffffff"))
            toolbar?.title = "Sessions"
            replaceFragment(MySessionCoach())


    }

    @OnClick(R.id.btnTrainingPath)
    fun btnTrainingPath() {
            tvCommunity!!.setTextColor(Color.parseColor("#FC6D2D"))
            tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
            tvProgress!!.setTextColor(Color.parseColor("#ffffff"))
            tvReport!!.setTextColor(Color.parseColor("#ffffff"))
            toolbar?.title = "Training Path"
            replaceFragment(TrainingPathFragmentCoach())



    }
*/

    override fun onBackPressed() {


        if (userRole.equals("SuperCounsellor") && ((binding.toolbarHome.title.equals("Profile Details")) ||
                    (binding.toolbarHome.title.equals("Players")))
        ) {
            val intent = Intent(this@AcademyMembersActivity, AcademyMembersActivity::class.java)
            intent!!.action = "SuperCounsellor"
            startActivity(intent)

        } else


            if ((binding.toolbarHome.title.equals("Profile Details")) || (binding.toolbarHome.title.equals("Progress"))
                || (binding.toolbarHome.title.equals("Sessions")) || (binding.toolbarHome.title.equals("Training Path"))
            ) {

                val intent =
                    Intent(this@AcademyMembersActivity, AcademyMembersActivity::class.java)
                intent!!.action = "AcademyMembersMain"
                startActivity(intent)

            } else if (binding.toolbarHome.title.equals("Edit Sessions")) {
                binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
                binding.tvEvent.setTextColor(Color.parseColor("#FC6D2D"))
                binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
                binding.tvReport.setTextColor(Color.parseColor("#ffffff"))
                binding.toolbarHome.title = "Sessions"

                replaceFragment(MySessionCoach())


            } else {
                ProjectUtils.showAlertDialog(this)

            }
    }


    /**
     * Switch User Functionality
     */

    private fun switchUser() {

        deviceID = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
        token = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        val progressDialogLogout = ProgressDialog(this@AcademyMembersActivity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.switchUser("Bearer $token", sessionManager?.getUserModel()?.switchUserCountryCode, sessionManager?.getUserModel()?.switchUserMobile)!!
            .enqueue(object : Callback<CheckOtpResponsePOJO?> {
                override fun onResponse(
                    call: Call<CheckOtpResponsePOJO?>,
                    response: Response<CheckOtpResponsePOJO?>
                ) {
                    progressDialogLogout.dismiss()
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            switchUserFunction(response.body())
                        }
                    } else if (response.code() == 422) {
                        val gson = GsonBuilder().create()
                        var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                        try {
                            pojo = gson.fromJson(
                                response.errorBody()!!.string(),
                                ValidationResponsePOJO::class.java
                            )
                            Toast.makeText(
                                this@AcademyMembersActivity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            this@AcademyMembersActivity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(
                    call: Call<CheckOtpResponsePOJO?>, t: Throwable
                ) {
                    progressDialogLogout.dismiss()
                    Toast.makeText(
                        this@AcademyMembersActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
            })

    }

    private fun switchUserFunction(checkOtpResponsePOJO: CheckOtpResponsePOJO?) {
        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_TOKEN,
            checkOtpResponsePOJO!!.token
        )


        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC,
            checkOtpResponsePOJO!!.user!!.profilePic
        )

        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_STATE,
            checkOtpResponsePOJO!!.user!!.state!!.name
        )

        mPreferencesManager!!.setIntegerValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID,
            checkOtpResponsePOJO!!.user!!.languageId!!
        )




        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_REWARD,
            checkOtpResponsePOJO!!.user!!.totalRewards.toString()
        )



        mPreferencesManager!!.createLoginSession(checkOtpResponsePOJO?.user)
        SessionManager.getInstance(this).setUserModel(checkOtpResponsePOJO?.user!!)

        if (checkOtpResponsePOJO!!.user!!.userType.equals("App") && checkOtpResponsePOJO.redirectTo.equals(
                "Home"
            )
        ) {
            mPreferencesManager!!.setStringValue(Constants.SHARED_PREFERENCE_ISLOGIN, "true")
        }

        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS,
            checkOtpResponsePOJO!!.user!!.counsellingSessionPaymentStatus
        )


        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_DEVICEID,
            deviceID
        )
        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE,
            checkOtpResponsePOJO.user!!.role
        )


        if (checkOtpResponsePOJO.user!!.role.equals("MasterUser")) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(applicationContext, AcademyMembersActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun checkUserType() {
        if (sessionManager?.getUserModel()?.switchUserMobile.isNullOrEmpty()){
            binding.ivSwap.visibility = View.GONE
        }else{
            binding.ivSwap.visibility = View.VISIBLE
        }
    }


}
