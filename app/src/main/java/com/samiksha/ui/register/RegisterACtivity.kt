package com.samiksha.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.google.gson.GsonBuilder
import com.hbb20.CountryCodePicker
import com.samiksha.R
import com.samiksha.databinding.ActivityRegisterActivityBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.informationScreen.welcomeScreen.WelComeActivity
import com.samiksha.ui.otp.OTPActivity
import com.samiksha.ui.register.pojo.*
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.ui.totorial.MainScreen
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.regex.Pattern


class RegisterACtivity : AppCompatActivity(), ILoginView {


    private val emailRegex = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )


  /*  @JvmField
    @BindView(R.id.radioButtonmale)
    var radioButtonmale: RadioButton? = null

    @JvmField
    @BindView(R.id.radioButtonfemale)
    var radioButtonfemale: RadioButton? = null

    @JvmField
    @BindView(R.id.edtUserMobileNo)
    var edtUserMobileNo: EditText? = null


    @JvmField
    @BindView(R.id.tvHeading)
    var tvHeading: TextView? = null

    @JvmField
    @BindView(R.id.txtcreateAccount)
    var txtcreateAccount: TextView? = null

    @JvmField
    @BindView(R.id.otpVerification)
    var otpVerification: TextView? = null

    @JvmField
    @BindView(R.id.btnLogin)
    var btnLogin: Button? = null

    @JvmField
    @BindView(R.id.tvUserDecision)
    var tvUserDecision: TextView? = null

    @JvmField
    @BindView(R.id.registerbox)
    var registerbox: LinearLayout? = null

    @JvmField
    @BindView(R.id.loginBox)
    var loginBox: LinearLayout? = null


    @JvmField
    @BindView(R.id.ccp)
    var ccp: CountryCodePicker? = null

    @JvmField
    @BindView(R.id.ccp1)
    var ccp1: CountryCodePicker? = null
*/

    var btnText: String? = null
    var otpRegister: String? = null
    var userMobileNo: String? = null
    var otpRegisterCountryCode: String? = null
    var otpRegisterCountry_Code: String? = null
    var gender: String? = null
    var UserCountryISDCode: String? = null
    var UserCountryISDCodeLogin: String? = null
    var UserCountryCOdeLogin: String? = null
    var selectedStateID: Int? = 9999


    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()

    private var onlyStateList: ArrayList<String>? = ArrayList()

    var iLoginPresenter: ILoginPresenter? = null
    private var mPreferencesManager: PreferencesManager? = null
    var isLogin: String? = null
    var userRole: String? = null
    lateinit var binding: ActivityRegisterActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_register_activity)
      //  ButterKnife.bind(this)
        binding = ActivityRegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        PreferencesManager.initializeInstance(this@RegisterACtivity)
        mPreferencesManager = PreferencesManager.instance
        setListners()
        if (mPreferencesManager!!.getBooleanValue(
                Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                true
            )
        ) {

            val intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intent)
            finish()

        } else {
            isLogin = mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_ISLOGIN)
            userRole =
                mPreferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)

            if (isLogin == "true" && userRole.equals("MasterUser")) {
                val intent = Intent(this@RegisterACtivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else if (isLogin == "true"
                && userRole.equals("Coach")
                || userRole.equals("Counsellor")
                || userRole.equals("SuperCounsellor")
            ) {

                val intent = Intent(applicationContext, AcademyMembersActivity::class.java)
                startActivity(intent)
                finish()

            }

        }





        iLoginPresenter = LoginPresenterImplementer(this)
        binding.tvHeading.text = intent.getStringExtra("btnText")
        otpRegister = intent.getStringExtra("otpRegister")
        otpRegisterCountryCode = intent.getStringExtra("otpRegisterCountryCode")
        otpRegisterCountry_Code = intent.getStringExtra("RegisterCountry_Code")
        userMobileNo = intent.getStringExtra("userMobileNo")
        iLoginPresenter!!.state("IN")
        if (binding.tvHeading.text!!.contains("Register")) {
            iLoginPresenter!!.state("IN")
            binding.loginBox.visibility = View.GONE
            binding.tvReward.visibility = View.GONE
            binding.tvHeading.visibility = View.VISIBLE
            binding.tvUserDecision.visibility = View.GONE
            binding.registerbox.visibility = View.VISIBLE

        }
        else if (binding.tvHeading?.text!!.contains("Create")) {

            binding.txtcreateAccount.text = "An OTP will be sent to this number to create an account."

            if (intent.getStringExtra("otpMobileNo") != null) {

                binding.edtUserMobileNo.setText(
                    intent.getStringExtra("otpMobileNo"),
                    TextView.BufferType.EDITABLE
                )
                binding.ccp1.setCountryForPhoneCode(intent.getIntExtra("otpCountryCode", 0))


            }
            binding.loginBox.visibility = View.VISIBLE
            binding.registerbox!!.visibility = View.GONE

            binding.tvHeading?.text = "Create an account"
            binding.tvReward!!.visibility = View.VISIBLE
            binding.tvMobileNoTxt?.text = "Enter your mobile number to begin"
            binding.btnLogin?.text = "Continue"
            binding.tvUserDecision.text = "Already a member? Login here"


        }
        else {

            binding.edtUserMobileNo.requestFocus()
            binding.loginBox.visibility = View.VISIBLE
            binding.registerbox!!.visibility = View.GONE
            binding.txtcreateAccount!!.text =
                "An OTP will be sent to this number to sign up/login to your account."

            if (intent.getStringExtra("otpMobileNo") != null) {

                binding.edtUserMobileNo.setText(
                    intent.getStringExtra("otpMobileNo"),
                    TextView.BufferType.EDITABLE
                )
                binding.ccp1.setCountryForPhoneCode(intent.getIntExtra("otpCountryCode", 0))


            }


            binding.tvHeading?.text = "Sign Up / Login"
            binding.tvReward!!.visibility = View.VISIBLE
            binding.tvMobileNoTxt?.text = "Enter your mobile number"
            binding.btnLogin?.text = "Continue"
            binding.tvUserDecision.text = "New user? Create a new account"


        }

        binding.ccp.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener {
            binding.edtState.setText("")
            intent.removeExtra("otpuserFirstName")
            iLoginPresenter!!.state(binding.ccp.selectedCountryNameCode.toString())


        })

    }

    private fun setListners() {
        binding.tvUserDecision.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterACtivity::class.java)
            intent.putExtra("btnText", binding.tvUserDecision.text)
            startActivity(intent)
            finish()
        })

        binding.edtName.addTextChangedListener { editable ->
            if (binding.tvPhoneNumberError.visibility == View.VISIBLE) {
                binding.tvPhoneNumberError.visibility = View.GONE
                return@addTextChangedListener
            }
        }
        binding.edtName.addTextChangedListener { editable ->
            if (binding.tvedtNameError.visibility == View.VISIBLE) {
                binding.tvedtNameError.visibility = View.GONE
                return@addTextChangedListener
            }
        }
        binding.edtlastName.addTextChangedListener { editable ->
            if (binding.tvedtlastNameError.visibility == View.VISIBLE) {
                binding.tvedtlastNameError.visibility = View.GONE
                return@addTextChangedListener
            }
        }
        binding.edtEmailId.addTextChangedListener { editable ->
            if (binding.tvedtEmailIdError.visibility == View.VISIBLE) {
                binding.tvedtEmailIdError.visibility = View.GONE
                return@addTextChangedListener
            }
        }
        binding.edtphonenumber.addTextChangedListener { editable ->
            if (binding.tvedtphonenumberError.visibility == View.VISIBLE) {
                binding.tvedtphonenumberError.visibility = View.GONE
                return@addTextChangedListener
            }
        }
        binding.edtState.addTextChangedListener { editable ->
            if (binding.tvedtStateError.visibility == View.VISIBLE) {
                binding.tvedtStateError.visibility = View.GONE
                return@addTextChangedListener
            }
        }

        binding.edtState.setOnClickListener(View.OnClickListener {
            stateDialog()
        })
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(this@RegisterACtivity)!!) {

                if (binding.tvHeading.text!!.contains("Register")) {


                    if (binding.radioButtonmale!!.isChecked) {
                        gender = "Male"
                    } else if (binding.radioButtonfemale!!.isChecked) {
                        gender = "Female"
                    } else {
                        gender = "Other"
                    }



                    if (binding.edtName!!.text!!.length == 0) {
                        binding.tvedtNameError.text = "Enter First Name"
                        binding.tvedtNameError.visibility = View.VISIBLE

                    } else if (binding.edtlastName!!.text!!.length == 0) {
                        binding.tvedtlastNameError.text = "Enter Last Name"
                        binding.tvedtlastNameError.visibility = View.VISIBLE

                    } else if (binding.edtEmailId!!.text!!.length == 0) {

                        binding.tvedtEmailIdError.text = "Enter Email ID"
                        binding.tvedtEmailIdError.visibility = View.VISIBLE

                    } else if (binding.edtEmailId!!.text.toString()
                            .isNotEmpty() && (!emailRegex.matcher(binding.edtEmailId!!.text.toString())
                            .matches())
                    ) {

                        binding.tvedtEmailIdError.text = "Invalid Email ID."
                        binding.tvedtEmailIdError.visibility = View.VISIBLE

                    } else {

                        callCheckUserExit(binding.edtEmailId!!.text.toString())


                    }


                } else if (binding.tvHeading.text!!!!.contains("Create"))
                {

                    UserCountryISDCodeLogin = binding.ccp1.selectedCountryCode
                    UserCountryCOdeLogin = binding.ccp1.selectedCountryNameCode
                    UserCountryISDCode = binding.ccp1.selectedCountryCode


                    if (binding.edtUserMobileNo.text!!.length == 0) {

                        binding.tvPhoneNumberError.text = "Enter Mobile Number"
                        binding.tvPhoneNumberError.visibility = View.VISIBLE

                    } else if (binding.edtUserMobileNo.text!!.length > 0) {
                        binding.ccp1.registerCarrierNumberEditText(binding.edtUserMobileNo)

                        if (binding.ccp1.isValidFullNumber == false) {
                            binding.tvPhoneNumberError.text = "Invalid Mobile number. Please check the number."
                            binding.tvPhoneNumberError.visibility = View.VISIBLE

                        } else {

                            iLoginPresenter!!.login(
                                binding.edtUserMobileNo.text.toString(),
                                "Login",
                                UserCountryISDCode, UserCountryCOdeLogin
                            )

                        }

                    }


                } else
                {

                    UserCountryISDCodeLogin = binding.ccp1.selectedCountryCode
                    UserCountryCOdeLogin = binding.ccp1.selectedCountryNameCode
                    UserCountryISDCode = binding.ccp1.selectedCountryCode



                    if (binding.edtUserMobileNo.text!!.length == 0) {

                        binding.tvPhoneNumberError.text = "Enter Mobile Number"
                        binding.tvPhoneNumberError.visibility = View.VISIBLE

                    } else if (binding.edtUserMobileNo.text!!.length > 0) {
                        binding.ccp1.registerCarrierNumberEditText(binding.edtUserMobileNo)

                        if (binding.ccp1.isValidFullNumber == false) {
                            binding.tvPhoneNumberError.text = "Invalid Mobile number. Please check the number."
                            binding.tvPhoneNumberError.visibility = View.VISIBLE

                        } else {


                            iLoginPresenter!!.login(
                                binding.edtUserMobileNo.text.toString(),
                                "Login",
                                UserCountryISDCodeLogin,
                                UserCountryCOdeLogin

                            )


                        }

                    }


                }

            } else {
                ProjectUtils.showToast(
                    this@RegisterACtivity,
                    getString(R.string.no_internet_connection)
                )

            }
        })
    }


   /* @OnClick(R.id.tvUserDecision)
    fun tvUserDecision() {

        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        intent.putExtra("btnText", tvUserDecision?.text)
        startActivity(intent)
        finish()


    }


    @OnTextChanged(R.id.edtUserMobileNo)
    fun editPhone() {
        if (tvPhoneNumberError!!.visibility == View.VISIBLE) {
            tvPhoneNumberError!!.visibility = View.GONE
            return
        }
    }

    @OnTextChanged(R.id.edtName)
    fun edtName() {
        if (tvedtNameError!!.visibility == View.VISIBLE) {
            tvedtNameError!!.visibility = View.GONE
            return
        }
    }

    @OnTextChanged(R.id.edtlastName)
    fun edtlastName() {
        if (tvedtlastNameError!!.visibility == View.VISIBLE) {
            tvedtlastNameError!!.visibility = View.GONE
            return
        }
    }

    @OnTextChanged(R.id.edtEmailId)
    fun edtEmailId() {
        if (tvedtEmailIdError!!.visibility == View.VISIBLE) {
            tvedtEmailIdError!!.visibility = View.GONE
            return
        }
    }

    @OnTextChanged(R.id.edtphonenumber)
    fun edtphonenumber() {
        if (tvedtphonenumberError!!.visibility == View.VISIBLE) {
            tvedtphonenumberError!!.visibility = View.GONE
            return
        }
    }

    @OnTextChanged(R.id.edtState)
    fun edtState() {
        if (tvedtStateError!!.visibility == View.VISIBLE) {
            tvedtStateError!!.visibility = View.GONE
            return
        }
    }


    @OnClick(R.id.edtState)
    fun edtstate() {
        stateDialog()
    }
*/
   private fun stateDialog() {

       val builder = AlertDialog.Builder(this)
       builder.setTitle("Choose any State")


       val dataAdapter =
           ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, onlyStateList!!)
       builder.setAdapter(dataAdapter)
       { dialog, which ->
           selectedStateID = stateList[which].id
           binding.edtState.setText(stateList[which].name)

       }
       val dialog = builder.create()
       dialog.show()


   }

    /* @OnClick(R.id.btnLogin)
    fun btnLogin() {
        if (ProjectUtils.checkInternetAvailable(this@RegisterACtivity)!!) {

            if (tvHeading!!.text!!.contains("Register")) {


            if (radioButtonmale!!.isChecked) {
                gender = "Male"
            } else if (radioButtonfemale!!.isChecked) {
                gender = "Female"
            } else {
                gender = "Other"
            }



            if (edtName!!.text!!.length == 0) {
                tvedtNameError!!.text = "Enter First Name"
                tvedtNameError!!.visibility = View.VISIBLE

            } else if (edtlastName!!.text!!.length == 0) {
                tvedtlastNameError!!.text = "Enter Last Name"
                tvedtlastNameError!!.visibility = View.VISIBLE

            } else if (edtEmailId!!.text!!.length == 0) {

                tvedtEmailIdError!!.text = "Enter Email ID"
                tvedtEmailIdError!!.visibility = View.VISIBLE

            } else if (edtEmailId!!.text.toString()
                    .isNotEmpty() && (!emailRegex.matcher(edtEmailId!!.text.toString())
                    .matches())
            ) {

                tvedtEmailIdError!!.text = "Invalid Email ID."
                tvedtEmailIdError!!.visibility = View.VISIBLE

            } else {

                callCheckUserExit(edtEmailId!!.text.toString())


            }


        } else if (tvHeading!!.text!!!!.contains("Create"))
        {

            UserCountryISDCodeLogin = ccp1!!.selectedCountryCode
            UserCountryCOdeLogin = ccp1!!.selectedCountryNameCode
            UserCountryISDCode = ccp1!!.selectedCountryCode


            if (edtUserMobileNo!!.text!!.length == 0) {

                tvPhoneNumberError!!.text = "Enter Mobile Number"
                tvPhoneNumberError!!.visibility = View.VISIBLE

            } else if (edtUserMobileNo!!.text!!.length > 0) {
                ccp1!!.registerCarrierNumberEditText(edtUserMobileNo)

                if (ccp1!!.isValidFullNumber == false) {
                    tvPhoneNumberError!!.text = "Invalid Mobile number. Please check the number."
                    tvPhoneNumberError!!.visibility = View.VISIBLE

                } else {

                    iLoginPresenter!!.login(
                        edtUserMobileNo!!.text.toString(),
                        "Login",
                        UserCountryISDCode, UserCountryCOdeLogin
                    )

                }

            }


        } else
        {

            UserCountryISDCodeLogin = ccp1!!.selectedCountryCode
            UserCountryCOdeLogin = ccp1!!.selectedCountryNameCode
            UserCountryISDCode = ccp1!!.selectedCountryCode



            if (edtUserMobileNo!!.text!!.length == 0) {

                tvPhoneNumberError!!.text = "Enter Mobile Number"
                tvPhoneNumberError!!.visibility = View.VISIBLE

            } else if (edtUserMobileNo!!.text!!.length > 0) {
                ccp1!!.registerCarrierNumberEditText(edtUserMobileNo)

                if (ccp1!!.isValidFullNumber == false) {
                    tvPhoneNumberError!!.text = "Invalid Mobile number. Please check the number."
                    tvPhoneNumberError!!.visibility = View.VISIBLE

                } else {


                    iLoginPresenter!!.login(
                        edtUserMobileNo!!.text.toString(),
                        "Login",
                        UserCountryISDCodeLogin,
                        UserCountryCOdeLogin

                    )


                }

            }


        }

        } else {
            ProjectUtils.showToast(
                this@RegisterACtivity,
                getString(R.string.no_internet_connection)
            )

        }


    }
*/
   private fun callCheckUserExit(email: String) {
       ApiClient.client.isUserExist(email).enqueue(object : Callback<IsUserExistResponsePOJO?> {
           override fun onResponse(
               call: Call<IsUserExistResponsePOJO?>,
               response: Response<IsUserExistResponsePOJO?>
           ) {
               if (response.code() == 200) {
                   if (response.body() != null) {
                       if(response.body()!!.isUserExist){
                           binding.tvedtEmailIdError.text = "Email ID already registered"
                           binding.tvedtEmailIdError.visibility = View.VISIBLE

                       }else{
                           if (otpRegisterCountryCode.isNullOrEmpty()) {
                               UserCountryISDCode = binding.ccp!!.selectedCountryCode
                           } else {
                               UserCountryISDCode = otpRegisterCountryCode
                               UserCountryCOdeLogin = otpRegisterCountry_Code
                           }


                           val intent = Intent(applicationContext, WelComeActivity::class.java)
                           intent.putExtra("LoginType", "Register")
                           intent.putExtra("userFirstName", binding.edtName!!.text.toString())
                           intent.putExtra("userLastName", binding.edtlastName!!.text.toString())
                           intent.putExtra("userEmail", binding.edtEmailId!!.text.toString())
                           intent.putExtra("userMobileNo", userMobileNo)
                           intent.putExtra("userState", UserCountryISDCode)
                           intent.putExtra("userUserCountryCOde", UserCountryCOdeLogin)
                           intent.putExtra("userGender", gender)
                           intent.putExtra("userOTP", otpRegister)

                           intent.putExtra("userCurrentState", "")
                           startActivity(intent)
                           finish()

                       }

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
                           this@RegisterACtivity,
                           pojo.errors!!.get(0).message,
                           Toast.LENGTH_LONG
                       ).show()


                   } catch (exception: IOException) {
                   }

               } else {
                   Toast.makeText(
                       this@RegisterACtivity,
                       "Internal Server Error",
                       Toast.LENGTH_LONG
                   ).show()

               }
           }

           override fun onFailure(
               call: Call<IsUserExistResponsePOJO?>, t: Throwable
           ) {
               Toast.makeText(
                   this@RegisterACtivity,
                   t.message,
                   Toast.LENGTH_LONG
               )
                   .show()
           }
       })

   }

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun onLoginSUccess(loginResponsepojo: LoginResponcePOJO?) {

        if (loginResponsepojo!!.redirectTo.equals("Register")) {


            val intent = Intent(applicationContext, OTPActivity::class.java)
            intent.putExtra("btnText", binding.tvHeading.text)
            intent.putExtra("userMobileNo", binding.edtUserMobileNo.text.toString())
            intent.putExtra("userState", UserCountryISDCode)
            intent.putExtra("UserCountryCodeLogin", UserCountryCOdeLogin)

            intent.putExtra("LoginType", "Register")

            startActivity(intent)
            finish()


        } else {

            if (loginResponsepojo!!.redirectTo.equals("Otp")) {
                val intent = Intent(applicationContext, OTPActivity::class.java)
                intent.putExtra("btnText", binding.tvHeading.text)
                intent.putExtra("userMobileNo", binding.edtUserMobileNo.text.toString())
                intent.putExtra("userStateLogin", UserCountryISDCodeLogin)
                intent.putExtra("UserCountryCodeLogin", UserCountryCOdeLogin)
                intent.putExtra("LoginType", "Login")

                startActivity(intent)
                finish()

            } else {

                binding.tvPhoneNumberError.text = loginResponsepojo.message
                binding.tvPhoneNumberError.visibility = View.VISIBLE

                // Toast.makeText(this@RegisterACtivity, loginResponsepojo!!.message, Toast.LENGTH_LONG).show()

            }


        }

    }

    override fun onRegisterSuccess(registerResponsePOJO: RegisterResponsePOJO?) {


        Toast.makeText(this@RegisterACtivity, registerResponsePOJO!!.message, Toast.LENGTH_LONG)
            .show()
        val intent = Intent(applicationContext, OTPActivity::class.java)
        intent.putExtra("btnText", binding.btnLogin!!.text)
        intent.putExtra("LoginType", "Register")
        intent.putExtra("userFirstName", binding.edtName!!.text.toString())
        intent.putExtra("userLastName", binding.edtlastName!!.text.toString())
        intent.putExtra("userEmail", binding.edtEmailId!!.text.toString())
        intent.putExtra("userMobileNo", binding.edtphonenumber!!.text.toString())
        intent.putExtra("userState", UserCountryISDCode)
        intent.putExtra("userGender", gender)
        intent.putExtra("userCurrentState", selectedStateID.toString())
        startActivity(intent)
        finish()

    }

    override fun validationError(validationResponse: ValidationResponsePOJO?) {

        if (validationResponse?.errors!![0]?.key.equals("email")) {
            binding.tvedtEmailIdError.text = validationResponse!!.errors!![0].message
            binding.tvedtEmailIdError.visibility = View.VISIBLE

        } else if (validationResponse?.errors!![0]?.key.equals("phone_no")) {
            binding.tvedtphonenumberError.text = validationResponse!!.errors!![0].message
            binding.tvedtphonenumberError.visibility = View.VISIBLE


        } else {

            Toast.makeText(
                this@RegisterACtivity,
                validationResponse!!.errors!![0].message,
                Toast.LENGTH_LONG
            ).show()
        }


    }
    override fun validationErrorEmpty(position: Int?) {

    }

    override fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?) {

        onlyStateList!!.clear()

        for (i in stateResponcePOJO!!.data!!.indices) {
            onlyStateList!!.add(stateResponcePOJO!!.data!![i].name!!)
        }

        stateList = stateResponcePOJO!!.data!!

        if (intent.getStringExtra("otpuserFirstName") != null) {

            selectedStateID = (intent.getIntExtra("otpuserCurrentState", 0))
            binding.edtState.setText(stateList[(intent.getIntExtra("otpuserCurrentState", 0)) - 1].name)


        }


    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(this@RegisterACtivity)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(this@RegisterACtivity, appErrorMessage, Toast.LENGTH_LONG).show()
    }

}
