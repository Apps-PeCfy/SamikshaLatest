package com.samiksha.ui.informationScreen.chooseGoals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.samiksha.R
import com.samiksha.databinding.ActivityChooseGoalsBinding
import com.samiksha.ui.informationScreen.chooseGoals.pojo.VerifyOTPResponse
import com.samiksha.ui.informationScreen.chooseProfession.ChooseProfessionActivity
import com.samiksha.ui.informationScreen.informationFinish.InformationFinishActivity
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class ChooseGoalsActivity : AppCompatActivity(), ChooseGoalsADapter.IClickListenerChooseGoals,
    IRegisterView {

    private var chooseGoalsList = ArrayList<QuestionResponsePOJO.QuestionsItem>()


    var chooseGoalsADapter: ChooseGoalsADapter? = null
    var goalsArrayListFinal: ArrayList<Int> = ArrayList<Int>()

   /* @JvmField
    @BindView(R.id.recycler_goals)
    var recycler_goals: RecyclerView? = null*/
    var selectedSportId: String? = null
    var selectedProfessionId: String? = null
    var iRegisterPresenter: IRegisterPresenter? = null


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
    var isHomeClicked: String? = null

    var deviceID: String? = null
    var device_token: String? = null
    var refferCode: String? = null



    private var mPreferencesManager: PreferencesManager? = null
    private lateinit var referrerClient: InstallReferrerClient
    var sessionManager: SessionManager? = null
    lateinit var binding: ActivityChooseGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_choose_goals)
     //   ButterKnife.bind(this)

        binding = ActivityChooseGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initView()
        refferClient()


        val redString = getResources().getString(R.string.any_three)
        binding.txtAnyThree!!.setText(Html.fromHtml(redString))


    }

    private fun refferClient() {
        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        Log.d("InstallReferrTest","OK")
                        val response: ReferrerDetails = referrerClient.installReferrer
                        refferCode = response.installReferrer


                        // Connection established.
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d("InstallReferrTest","FEATURE_NOT_SUPPORTED")
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Log.d("InstallReferrTest","SERVICE_UNAVAILABLE")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }


    private fun initView() {
        sessionManager = SessionManager.getInstance(this@ChooseGoalsActivity)

        deviceID = String.format("%08d", Random.nextInt(100000000))

        device_token = FirebaseMessaging.getInstance().token.toString()


        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                device_token = it.result.toString()
                // DO your thing with your firebase token
            }
        }



        PreferencesManager.initializeInstance(this@ChooseGoalsActivity)
        mPreferencesManager = PreferencesManager.instance
        iRegisterPresenter = RegisterImplementer(this)

        setListners()
        isHomeClicked = intent.getStringExtra("HomeSort")

        loginType = intent.getStringExtra("LoginType")
        userMobileNo = intent.getStringExtra("userMobileNo")
        userFirstName = intent.getStringExtra("userFirstName")
        userLastName = intent.getStringExtra("userLastName")
        userEmail = intent.getStringExtra("userEmail")
        userState = intent.getStringExtra("userState")
        userGender = intent.getStringExtra("userGender")
        userOTP = intent.getStringExtra("userOTP")
        userCurrentState = intent.getStringExtra("userCurrentState")
        selectedSportId = intent.getStringExtra("selectedSportId")
        selectedProfessionId = intent.getStringExtra("professionID")
        userUserCountryCOde = intent.getStringExtra("userUserCountryCOde")



        if (isHomeClicked.equals("IsCLicked")) {
            binding.btnBack.visibility = View.GONE

                iRegisterPresenter!!.question()


        } else {
            Log.d("wasnensmwsms", "" + selectedProfessionId + "   " + selectedSportId)


            chooseGoalsList = intent.getSerializableExtra("GoalsList")
                    as ArrayList<QuestionResponsePOJO.QuestionsItem>
            binding.tvGoalsHeading!!.setText(chooseGoalsList[2].name)


            binding.recyclerGoals!!.layoutManager =
                LinearLayoutManager(this@ChooseGoalsActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerGoals!!.itemAnimator = DefaultItemAnimator()

            chooseGoalsADapter = ChooseGoalsADapter(
                this@ChooseGoalsActivity,
                chooseGoalsList.get(2).options
            )
            val spanCount = 1 // 3 columns
            binding.recyclerGoals!!.setLayoutManager(
                GridLayoutManager(
                    this@ChooseGoalsActivity,
                    spanCount
                )
            )
            val spacing = 10 // 50px

            val includeEdge = true
            binding.recyclerGoals!!.addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount,
                    spacing,
                    includeEdge
                )
            )
            binding.recyclerGoals!!.setAdapter(chooseGoalsADapter)
            chooseGoalsADapter!!.notifyDataSetChanged()
            chooseGoalsADapter!!.setClickListener(this)


        }


    }

    private fun setListners() {
        binding.btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ChooseProfessionActivity::class.java)
            intent.putExtra("ChooseProfessionList", chooseGoalsList)
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
            intent.putExtra("professionID", selectedProfessionId)
            intent.putExtra("selectedSportID", selectedSportId)


            startActivity(intent)
            finish()
        })
        binding.btnFinish.setOnClickListener(View.OnClickListener {
            if (goalsArrayListFinal.size >= 3) {

                if (sessionManager!!.getUserModel()!!.userType.equals("App")) {
                    if (isHomeClicked.equals("IsCLicked")) {
                        iRegisterPresenter!!.updateGoals(
                            mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN),
                            goalsArrayListFinal
                        )

                    } else {
                        iRegisterPresenter!!.registerUser(

                            userFirstName,
                            userLastName,
                            userEmail,
                            userState,
                            userMobileNo,
                            userOTP,
                            userGender,
                            selectedSportId,
                            selectedProfessionId,
                            goalsArrayListFinal,
                            deviceID,
                            "Android",
                            device_token,
                            userCurrentState,
                            refferCode,
                            loginType, userUserCountryCOde
                        )

                    }

                } else {

                    iRegisterPresenter!!.updateWebUser(
                        mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN),
                        selectedSportId,
                        selectedProfessionId,
                        goalsArrayListFinal
                    )

                }


            } else {


                Toast.makeText(
                    this@ChooseGoalsActivity,
                    "Please select any three goals",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

 /*   @OnClick(R.id.btnBack)
    fun btnBack() {

        val intent = Intent(applicationContext, ChooseProfessionActivity::class.java)
        intent.putExtra("ChooseProfessionList", chooseGoalsList)
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
        intent.putExtra("professionID", selectedProfessionId)
        intent.putExtra("selectedSportID", selectedSportId)


        startActivity(intent)
        finish()


    }

    @OnClick(R.id.btnFinish)
    fun btnFinish() {



        if (goalsArrayListFinal.size >= 3) {

            if (sessionManager!!.getUserModel()!!.userType.equals("App")) {
                if (isHomeClicked.equals("IsCLicked")) {
                    iRegisterPresenter!!.updateGoals(
                        mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN),
                        goalsArrayListFinal
                    )

                } else {
                    iRegisterPresenter!!.registerUser(

                        userFirstName,
                        userLastName,
                        userEmail,
                        userState,
                        userMobileNo,
                        userOTP,
                        userGender,
                        selectedSportId,
                        selectedProfessionId,
                        goalsArrayListFinal,
                        deviceID,
                        "Android",
                        device_token,
                        userCurrentState,
                        refferCode,
                        loginType, userUserCountryCOde
                    )

                }

            } else {

                iRegisterPresenter!!.updateWebUser(
                    mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN),
                    selectedSportId,
                    selectedProfessionId,
                    goalsArrayListFinal
                )

            }





        } else {


            Toast.makeText(
                this@ChooseGoalsActivity,
                "Please select any three goals",
                Toast.LENGTH_SHORT
            ).show()
        }


    }*/

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }


    override fun selectedSport(goalsArrayList: ArrayList<Int>) {

        goalsArrayListFinal = goalsArrayList
    }

    override fun onRegisterSuccess(body: CheckOtpResponsePOJO?) {

        mPreferencesManager!!.createLoginSession(body?.user)
        SessionManager.getInstance(this).setUserModel(body?.user!!)



        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_TOKEN,
            body.token
        )

        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS,
            body!!.user!!.counsellingSessionPaymentStatus
        )

        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC,
            body.user!!.profilePic
        )


        mPreferencesManager!!.setIntegerValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID,
            body.user!!.languageId!!
        )


        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_REWARD,
            body.user!!.totalRewards.toString()
        )

        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_ISLOGIN,
            "true"
        )


        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE,
            body.user!!.role
        )


        fireEvent(body)


        val intent = Intent(applicationContext, InformationFinishActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onUpdateWebUserSuccess(body: CheckOtpResponsePOJO?) {
        mPreferencesManager!!.createLoginSession(body?.user)
        SessionManager.getInstance(this).setUserModel(body?.user!!)


        val intent = Intent(applicationContext, InformationFinishActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onUpdateUserSuccess(body: VerifyOTPResponse?) {

     //   Toast.makeText(this@ChooseGoalsActivity, body!!.message, Toast.LENGTH_LONG).show()


        val intent = Intent(applicationContext, InformationFinishActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun validationError(validationResponse: ValidationResponsePOJO?) {
        Toast.makeText(
            this@ChooseGoalsActivity,
            validationResponse!!.errors!![0].message,
            Toast.LENGTH_LONG
        ).show()

    }

    override fun validationErrorEmpty(position: Int?) {
    }

    override fun onQuestionSuccess(questionResponsePOJO: QuestionResponsePOJO?) {


        val questionList: java.util.ArrayList<QuestionResponsePOJO.OptionsItem> =
            java.util.ArrayList<QuestionResponsePOJO.OptionsItem>()


        for (i in questionResponsePOJO!!.questions?.get(2)!!.options!!.indices) {
            questionList.add(questionResponsePOJO.questions?.get(2)!!.options?.get(i)!!)
        }


        for (j in mPreferencesManager!!.getcategoryList().indices) {

            if (mPreferencesManager!!.getcategoryList()[j].attribute.equals("goal_id")) {
                var pos = (mPreferencesManager!!.getcategoryList().get(j).name)

                for (k in questionList!!.indices) {
                    if(pos!!.equals(questionList[k].name)){
                        questionList[k].isSelected = "yes"
                    }

                }

              //
            }
        }


        binding.tvGoalsHeading!!.setText(questionResponsePOJO.questions!![2].name)


        binding.recyclerGoals!!.layoutManager =
            LinearLayoutManager(this@ChooseGoalsActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerGoals!!.itemAnimator = DefaultItemAnimator()

        chooseGoalsADapter = ChooseGoalsADapter(
            this@ChooseGoalsActivity,
            questionList
        )
        val spanCount = 1 // 3 columns
        binding.recyclerGoals!!.setLayoutManager(GridLayoutManager(this@ChooseGoalsActivity, spanCount))
        val spacing = 10 // 50px

        val includeEdge = true
        binding.recyclerGoals!!.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerGoals!!.setAdapter(chooseGoalsADapter)
        chooseGoalsADapter!!.notifyDataSetChanged()
        chooseGoalsADapter!!.setClickListener(this)


    }

    override fun showWait() {
    }

    override fun removeWait() {
    }

    override fun onFailure(appErrorMessage: String?) {
    }


    private fun fireEvent(body: CheckOtpResponsePOJO) {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, body.user?.id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, body.user?.name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)

    }
}
