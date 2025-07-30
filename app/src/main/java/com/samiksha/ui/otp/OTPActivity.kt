package com.samiksha.ui.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.google.firebase.messaging.FirebaseMessaging
import com.samiksha.R
import com.samiksha.databinding.ActivityOtpBinding
import com.samiksha.receiver.SmsReceiver
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.informationScreen.chooseSport.ChooseSportActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.RegisterACtivity
import com.samiksha.ui.register.pojo.LoginResponcePOJO
import com.samiksha.ui.register.pojo.RegisterResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import kotlin.random.Random

class OTPActivity : AppCompatActivity(), IOtpView {


    /*@JvmField
    @BindView(R.id.tvBack)
    var tvBack: TextView? = null

    @JvmField
    @BindView(R.id.txtResendTimer)
    var txtResendTimer: TextView? = null

    @JvmField
    @BindView(R.id.btnOTP)
    var btnOTP: Button? = null

    @JvmField
    @BindView(R.id.otp_edit_box1)
    var otp_edit_box1: EditText? = null

    @JvmField
    @BindView(R.id.otp_edit_box2)
    var otp_edit_box2: EditText? = null

    @JvmField
    @BindView(R.id.otp_edit_box3)
    var otp_edit_box3: EditText? = null

    @JvmField
    @BindView(R.id.otp_edit_box4)
    var otp_edit_box4: EditText? = null
*/

    var btnText: String? = null
    var OTP: String? = null
    var deviceID: String? = null
    var device_token: String? = null
    var isCancel: String? = "1"

    var smsReceiver: SmsReceiver? = null

    private var mPreferencesManager: PreferencesManager? = null


    var userMobileNo: String? = null
    var loginType: String? = null
    var userFirstName: String? = null
    var userLastName: String? = null
    var userEmail: String? = null
    var userState: String? = null
    var userStateLogin: String? = null
    var userCountry_code_Login: String? = null
    var userGender: String? = null
    var userCurrentState: String? = null
    var countDownTimer: CountDownTimer? = null


    var iOtpPresenter: IOtpPresenter? = null
    lateinit var binding: ActivityOtpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_otp)
      //  ButterKnife.bind(this)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initView()
        iOtpPresenter = OtpPresenterImplmenter(this)

        btnText = intent.getStringExtra("btnText")

        loginType = intent.getStringExtra("LoginType")
        userMobileNo = intent.getStringExtra("userMobileNo")
        userFirstName = intent.getStringExtra("userFirstName")
        userLastName = intent.getStringExtra("userLastName")
        userEmail = intent.getStringExtra("userEmail")
        userState = intent.getStringExtra("userState")
        userStateLogin = intent.getStringExtra("userStateLogin")
        userCountry_code_Login = intent.getStringExtra("UserCountryCodeLogin")
        userGender = intent.getStringExtra("userGender")
        userCurrentState = intent.getStringExtra("userCurrentState")

        binding.tvOTPTxt.text =
            "We just sent an One Time Password (OTP) to " + userMobileNo + ". Please enter OTP to continue."

        callResendOTPAPI()
    }


    private fun initView() {

        startCounter()

        //  registerReceiver()


        PreferencesManager.initializeInstance(this@OTPActivity)
        mPreferencesManager = PreferencesManager.instance

        deviceID = String.format("%08d", Random.nextInt(100000000))

        device_token = FirebaseMessaging.getInstance().token.toString()
        setListners()
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                device_token = it.result.toString()
                // DO your thing with your firebase token
            }
        }




        binding.otpEditBox1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (binding.otpEditBox1.length() == 1) {
                    binding.otpEditBox2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.otpEditBox2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (binding.otpEditBox2.length() == 1) {
                    binding.otpEditBox3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.otpEditBox3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (binding.otpEditBox3.length() == 1) {
                    binding.otpEditBox4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.otpEditBox4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (binding.otpEditBox4.length() == 1) {
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })


        binding.otpEditBox1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                binding.otpEditBox1.setText("")
            }
            false
        })
        binding.otpEditBox2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.otpEditBox2.getText().toString().isEmpty()) {
                    binding.otpEditBox1.setText("")
                    binding.otpEditBox1.requestFocus()
                } else {
                    binding.otpEditBox2.setText("")
                }
            }
            false
        })
        binding.otpEditBox3.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.otpEditBox3.getText().toString().isEmpty()) {
                    binding.otpEditBox2.setText("")
                    binding.otpEditBox2.requestFocus()
                } else {
                    binding.otpEditBox3.setText("")
                }
            }
            false
        })
        binding.otpEditBox4.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.otpEditBox4.getText().toString().isEmpty()) {
                    binding.otpEditBox3.setText("")
                    binding.otpEditBox3.requestFocus()
                } else {
                    binding.otpEditBox4.setText("")
                }
            }
            false
        })



        binding.txtResendTimer.setOnClickListener {
            if (binding.txtResendTimer.text?.equals("Resend OTP")!!) {
                if (ProjectUtils.checkInternetAvailable(this@OTPActivity)!!) {
                    callResendOTPAPI()
                } else {
                    ProjectUtils.showToast(
                        this@OTPActivity,
                        getString(R.string.no_internet_connection)
                    )

                }

            }
        }
    }

    private fun setListners() {
        binding.tvBack.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(this@OTPActivity)!!) {

                countDownTimer!!.cancel()

                if (loginType.equals("Login")) {

                    val intent = Intent(applicationContext, RegisterACtivity::class.java)
                    intent.putExtra("btnText", btnText)
                    intent.putExtra("otpMobileNo", userMobileNo)
                    intent.putExtra("otpCountryCode", userStateLogin!!.toInt())
                    startActivity(intent)
                    finish()


                } else
                {

                    val intent = Intent(applicationContext, RegisterACtivity::class.java)
                    intent.putExtra("btnText", btnText)
                    intent.putExtra("otpCountryCode", userState!!.toInt())
                    intent.putExtra("otpMobileNo", userMobileNo)
                    startActivity(intent)
                    finish()


                }

            } else {
                ProjectUtils.showToast(
                    this@OTPActivity,
                    getString(R.string.no_internet_connection)
                )

            }
        })
        binding.btnOTP.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(this@OTPActivity)!!) {

                successOtpFlow()
            } else {
                ProjectUtils.showToast(
                    this@OTPActivity,
                    getString(R.string.no_internet_connection)
                )

            }
        })
    }
    private fun callResendOTPAPI() {


            if (loginType.equals("Register")) {
                iOtpPresenter!!.resendOtp(userMobileNo, loginType, userState)

                /*iOtpPresenter!!.register(
                loginType,
                userFirstName,
                userLastName,
                userEmail,
                userState,
                userMobileNo,
                userGender,
                userCurrentState
            )*/
            } else {
                iOtpPresenter!!.login(userMobileNo, loginType, userStateLogin)

            }


    }

    /* private fun registerReceiver() {
         val client = SmsRetriever.getClient(this)
         val task = client.startSmsRetriever()
         task.addOnSuccessListener {
             smsReceiver = SmsReceiver()
             registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
             val otpListener: SmsReceiver.OTPListener = object : SmsReceiver.OTPListener {
                 @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                 override fun onOTPReceived(otpData: String?) {
                     val arr: CharArray = otpData?.toCharArray()!!
                     if (arr.size == 4) {
                         otp_edit_box1?.setText(arr[0].toString())
                         otp_edit_box2?.setText(arr[1].toString())
                         otp_edit_box3?.setText(arr[2].toString())
                         otp_edit_box4?.setText(arr[3].toString())

                     }

                     OTP = otpData
                     successOtpFlow()
                 }

                 override fun onOTPTimeOut() {
                     OTP = ""
                     otp_edit_box1?.setText("")
                     otp_edit_box2?.setText("")
                     otp_edit_box3?.setText("")
                     otp_edit_box4?.setText("")
                     Toast.makeText(this@OTPActivity, "TimeOut", Toast.LENGTH_SHORT).show()
                 }
             }
             smsReceiver!!.injectOTPListener(otpListener)
         }
         task.addOnFailureListener { // Failed to start retriever, inspect Exception for more details
             Toast.makeText(this, "Problem to start listener", Toast.LENGTH_SHORT).show()
         }
     }*/

    private fun successOtpFlow() {
        val W1: String = binding.otpEditBox1.text.toString()
        val W2: String = binding.otpEditBox2.text.toString()
        val W3: String = binding.otpEditBox3.text.toString()
        val W4: String = binding.otpEditBox4.text.toString()


        OTP = W1 + W2 + W3 + W4


        if (OTP.isNullOrEmpty()) {
            Toast.makeText(this@OTPActivity, "Please enter OTP", Toast.LENGTH_SHORT).show()

        } else {


                if (loginType.equals("Register")) {
                    iOtpPresenter!!.checkOtp(userState, userMobileNo, OTP)
                } else {
                    iOtpPresenter!!.verifyOTP(
                        "Login", userStateLogin, userMobileNo,
                        OTP, deviceID, "Android", device_token, userCountry_code_Login
                    )
                }


        }
    }

    private fun startCounter() {


        countDownTimer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if ((millisUntilFinished / 1000).toString().length == 1) {
                    binding.txtResendTimer.setText("Send OTP again in " + millisUntilFinished / 1000 + "" + " sec")

                } else {
                    binding.txtResendTimer.setText("Send OTP again in " + millisUntilFinished / 1000 + "" + " sec")

                }
            }

            override fun onFinish() {
                binding.txtResendTimer.text = "Resend OTP"
            }
        }

        countDownTimer?.start()

    }

   /* @OnClick(R.id.tvBack)
    fun tvBack() {

        if (ProjectUtils.checkInternetAvailable(this@OTPActivity)!!) {

            countDownTimer!!.cancel()

        if (loginType.equals("Login")) {

            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            intent.putExtra("btnText", btnText)
            intent.putExtra("otpMobileNo", userMobileNo)
            intent.putExtra("otpCountryCode", userStateLogin!!.toInt())
            startActivity(intent)
            finish()


        } else
        {

            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            intent.putExtra("btnText", btnText)
            intent.putExtra("otpCountryCode", userState!!.toInt())
            intent.putExtra("otpMobileNo", userMobileNo)
            startActivity(intent)
            finish()


        }

        } else {
            ProjectUtils.showToast(
                this@OTPActivity,
                getString(R.string.no_internet_connection)
            )

        }

    }

    @OnClick(R.id.btnOTP)
    fun btnOTP() {
        if (ProjectUtils.checkInternetAvailable(this@OTPActivity)!!) {

        successOtpFlow()
        } else {
            ProjectUtils.showToast(
                this@OTPActivity,
                getString(R.string.no_internet_connection)
            )

        }



    }*/

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun otpSuccess(body: CheckOtpResponsePOJO?) {
        countDownTimer!!.cancel()
        mPreferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_DEVICEID,
            deviceID
        )
        mPreferencesManager!!.createLoginSession(body?.user)
        SessionManager.getInstance(this).setUserModel(body?.user!!)


        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        intent.putExtra("btnText", "Register")
        intent.putExtra("otpRegister", OTP)
        intent.putExtra("otpRegisterCountryCode", userState)
        intent.putExtra("RegisterCountry_Code", userCountry_code_Login)
        intent.putExtra("userMobileNo", userMobileNo)

        startActivity(intent)
        finish()


        /*val intent = Intent(applicationContext, WelComeActivity::class.java)
        intent.putExtra("LoginType", loginType)
        intent.putExtra("userFirstName", userFirstName)
        intent.putExtra("userLastName", userLastName)
        intent.putExtra("userEmail", userEmail)
        intent.putExtra("userMobileNo", userMobileNo)
        intent.putExtra("userState", userState)
        intent.putExtra("userGender", userGender)
        intent.putExtra("userOTP", OTP)
        intent.putExtra("userCurrentState", userCurrentState)

        startActivity(intent)
        finish()
*/
    }

    override fun verifyotpSuccess(checkOtpResponsePOJO: CheckOtpResponsePOJO?) {

        countDownTimer!!.cancel()
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
            if (checkOtpResponsePOJO!!.user!!.userType.equals("Web")) {

                val intent = Intent(applicationContext, ChooseSportActivity::class.java)

                intent.putExtra("LoginType", loginType)
                intent.putExtra("userFirstName", checkOtpResponsePOJO!!.user!!.firstName)
                intent.putExtra("userLastName", checkOtpResponsePOJO!!.user!!.lastName)
                intent.putExtra("userEmail", checkOtpResponsePOJO!!.user!!.email)
                intent.putExtra("userMobileNo", userMobileNo)
                intent.putExtra("userState", "")
                intent.putExtra("userGender", checkOtpResponsePOJO!!.user!!.gender)
                intent.putExtra("userOTP", OTP)
                intent.putExtra("userCurrentState", "")
                intent.putExtra("userUserCountryCOde", userCountry_code_Login)
                intent.putExtra("isBackButton", "NotVisible")
                startActivity(intent)
                finish()


            } else {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()

            }


        } else {
            val intent = Intent(applicationContext, AcademyMembersActivity::class.java)
            startActivity(intent)
            finish()

        }

    }


    override fun validationError(validationResponse: ValidationResponsePOJO?) {
        Toast.makeText(
            this@OTPActivity,
            validationResponse!!.errors!![0].message,
            Toast.LENGTH_LONG
        ).show()


    }

    override fun onLoginSUccess(loginResponsepojo: LoginResponcePOJO?) {
        Toast.makeText(this@OTPActivity, loginResponsepojo!!.message, Toast.LENGTH_LONG).show()
        startCounter()
    }

    override fun onRegisterSuccess(registerResponsePOJO: RegisterResponsePOJO?) {
        Toast.makeText(this@OTPActivity, registerResponsePOJO!!.message, Toast.LENGTH_LONG).show()
        startCounter()
    }


    override fun showWait() {
        ProjectUtils.showProgressDialog(this@OTPActivity)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(this@OTPActivity, appErrorMessage, Toast.LENGTH_LONG).show()
    }

}
