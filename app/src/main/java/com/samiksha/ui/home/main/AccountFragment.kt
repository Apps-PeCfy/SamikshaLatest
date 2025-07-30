package com.samiksha.ui.home.main

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentAccountBinding
import com.samiksha.networking.ApiClient.client
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.otp.IOtpPresenter
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.StateResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : Fragment(), AdapterView.OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener, IAccountView {

    private var userData: CheckOtpResponsePOJO.User? = null

  /*  @JvmField
    @BindView(R.id.iv_user)
    var iv_user: RoundedImageView? = null

    @JvmField
    @BindView(R.id.edtFirstName)
    var edtFirstName: EditText? = null

    @JvmField
    @BindView(R.id.edtLastName)
    var edtLastName: EditText? = null

    @JvmField
    @BindView(R.id.tvUserMobileNo)
    var tvUserMobileNo: TextView? = null

    @JvmField
    @BindView(R.id.tvUserEmailID)
    var tvUserEmailID: TextView? = null

    @JvmField
    @BindView(R.id.tvUserDOB)
    var tvUserDOB: TextView? = null

    @JvmField
    @BindView(R.id.tvUserSport)
    var tvUserSport: TextView? = null

    @JvmField
    @BindView(R.id.tvUserRole)
    var tvUserRole: EditText? = null

    @JvmField
    @BindView(R.id.tvUserGurdianNo)
    var tvUserGurdianNo: EditText? = null

    @JvmField
    @BindView(R.id.edtAcademyName)
    var edtAcademyName: EditText? = null

    @JvmField
    @BindView(R.id.tvState)
    var tvState: TextView? = null

    @JvmField
    @BindView(R.id.tvUserProfession)
    var tvUserProfession: TextView? = null

    @JvmField
    @BindView(R.id.edtUserCity)
    var edtUserCity: EditText? = null

    @JvmField
    @BindView(R.id.tvUserGender)
    var tvUserGender: Spinner? = null

    @JvmField
    @BindView(R.id.spnUserSPort)
    var spnUserSPort: TextView? = null

    @JvmField
    @BindView(R.id.spnUserProfession)
    var spnUserProfession: TextView? = null

    @JvmField
    @BindView(R.id.spinner_state)
    var spinner_state: Spinner? = null

    @JvmField
    @BindView(R.id.spinner_city)
    var spinner_city: Spinner? = null

    @JvmField
    @BindView(R.id.rl_spinner_city)
    var rl_spinner_city: RelativeLayout? = null

*/

    var spngen = 0
    var user_Profession = 0
    var user_Profession_id = 0
    var user_sport = 0
    var user_sport_id = 0
    var selected_state_index = 0
    var selectedStateID: String ?= null
    var selected_city_index = 0
    var selectedCityID: String ?= null
    var luggageSpinner = arrayOf<String?>("Male", "Female", "Other")
    var questionSpinner = ArrayList<String?>()
    var userProfession = ArrayList<String?>()
    var stateList = ArrayList<StateResponsePOJO.DataItem>()
    var cityList = ArrayList<StateResponsePOJO.DataItem>()

    var mday = 0
    var mmonth = 0
    var myear = 0
    var calendar: Calendar? = null
    var strMonth: String? = null
    var stDay: String? = null
    var gender: String? = null
    var token: String? = null


    private var sessionManager: SessionManager? = null
    var preferencesManager: PreferencesManager? = null

    var formatDate = SimpleDateFormat("yyyy-MM-dd")

    var formatRide = SimpleDateFormat("MMM dd, yyyy")

     var questionList= ArrayList<QuestionResponsePOJO.QuestionsItem>()

    private var iAccountPresenter: IAccountPresenter? = null

    lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val rootView = inflater.inflate(R.layout.fragment_account, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        PreferencesManager.initializeInstance(requireActivity().applicationContext)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        init()

        callSportAPI()




        return binding.root
    }



    private fun init() {


        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Edit Profile"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })


        sessionManager = SessionManager.getInstance(requireContext())

        userData = sessionManager!!.getUserModel()

        iAccountPresenter = AccountImplementer(this)

        // iAccountPresenter?.stateList(sessionManager?.getUserModel()?.countryCode)
        iAccountPresenter?.stateList("IN")

        setData()
        setListeners()
    }

    private fun setData() {
        if (userData!!.dateOfBirth!!.isNotEmpty()) {
            try {
                val formaredDate = formatDate.format(formatRide!!.parse(userData!!.dateOfBirth!!)!!)
                binding.tvUserDOB.setText(formaredDate)
            }catch (e : Exception){

            }


        } else {
            binding.tvUserDOB.setText(userData!!.dateOfBirth)

        }


        binding.edtFirstName.setText(userData!!.firstName)
        binding.edtLastName.setText(userData!!.lastName)
        binding.tvUserMobileNo.setText("+"+userData!!.countryPhoneCode+" "+userData!!.phoneNo)
        binding.tvUserEmailID.setText(userData!!.email)
        binding.tvUserRole!!.setText(userData!!.whoIAm)
        binding.tvUserGurdianNo!!.setText(userData!!.ifMinorGuardianContact)
        binding.spnUserSPort!!.setText(userData!!.sport!!.name)
        binding.spnUserProfession!!.setText(userData!!.professionalLevel!!.name)
        binding.edtAcademyName!!.setText(userData!!.academyInstitute)
        binding.tvState!!.setText(userData!!.state!!.name)
        binding.edtUserCity!!.setText(userData?.city?.name)
        binding.tvUserSport!!.setText(userData!!.sport!!.name)
        binding.tvUserProfession!!.setText(userData!!.professionalLevel!!.name)


        Glide.with(this@AccountFragment)
            .load(userData!!.profilePic)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(binding.ivUser!!)


        if ((userData!!.gender).equals("Male")) {
            spngen = 0
        } else if ((userData!!.gender).equals("Female")) {
            spngen = 1
        } else {
            spngen = 2
        }

        val spinnerLuggage: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireActivity(), R.layout.simple_item_spinner, luggageSpinner)
        spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tvUserGender?.adapter = spinnerLuggage
        binding.tvUserGender?.setSelection(spngen)
        binding.tvUserGender!!.onItemSelectedListener = this
    }

    private fun setListeners() {
        binding.spinnerState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                var stateModel = stateList[position]
                selectedStateID = stateModel.id.toString()
                if(!selectedStateID.isNullOrEmpty() && selectedStateID != "0"){
                    iAccountPresenter?.cityList(selectedStateID)
                }else{
                    selectedCityID = "0"
                    cityList.clear()
                    binding.rlSpinnerCity?.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.spinnerCity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                var cityModel = cityList[position]
                selectedCityID = cityModel.id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        binding.tvUserDOB.setOnClickListener(View.OnClickListener {
            calendar = Calendar.getInstance()
            calendar!!.add(Calendar.YEAR, -10)
            myear = calendar!!.get(Calendar.YEAR)
            mmonth = calendar!!.get(Calendar.MONTH)
            mday = calendar!!.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(requireActivity(), this@AccountFragment, myear, mmonth, mday)
            datePickerDialog.getDatePicker().setMaxDate(calendar!!.timeInMillis)
            datePickerDialog.show()


        })

        binding.btnSave.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                if (spngen == 0) {
                    gender = "Male"
                } else if (spngen == 1) {
                    gender = "Female"
                } else {
                    gender = "Other"
                }
                if(isValid()){
                    callAPI()
                }

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
    }
    /*@OnClick(R.id.tvUserDOB)
    fun dob() {

        calendar = Calendar.getInstance()
        calendar!!.add(Calendar.YEAR, -10)
        myear = calendar!!.get(Calendar.YEAR)
        mmonth = calendar!!.get(Calendar.MONTH)
        mday = calendar!!.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireActivity(), this@AccountFragment, myear, mmonth, mday)
        datePickerDialog.getDatePicker().setMaxDate(calendar!!.timeInMillis)
        datePickerDialog.show()


    }*/


    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        spngen = position
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myear = year
        mmonth = month + 1
        mday = dayOfMonth

        if ((mmonth.toString().length == 1)) {
            strMonth = ("0" + mmonth)
        } else {
            strMonth = mmonth.toString()
        }

        if ((mday.toString().length == 1)) {
            stDay = ("0" + mday)
        } else {
            stDay = mday.toString()
        }


        binding.tvUserDOB!!.text = "$myear-" + strMonth + "-" + stDay
    }


    /* @OnClick(R.id.btnSave)
     fun btnSave() {


         if (ProjectUtils.checkInternetAvailable(context)!!) {

         if (spngen == 0) {
             gender = "Male"
         } else if (spngen == 1) {
             gender = "Female"
         } else {
             gender = "Other"
         }
         if(isValid()){
             callAPI()
         }

         } else {
             ProjectUtils.showToast(
                 context,
                 getString(R.string.no_internet_connection)
             )
         }



     }*/

    private fun isValid(): Boolean {
        if (selectedStateID.isNullOrEmpty() || selectedStateID == "0"){
            Toast.makeText(context, "Please select state", Toast.LENGTH_SHORT).show()
            return false
        }else if (selectedCityID.isNullOrEmpty() || selectedCityID == "0"){
            Toast.makeText(context, "Please select city", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    /**
     * API CALLING
     */

    override fun stateListSuccess(model: StateResponsePOJO?) {
        stateList = model?.data!!
        var stateModel : StateResponsePOJO.DataItem = StateResponsePOJO().DataItem()
        stateModel.name = "Select your state"
        stateModel.id = 0
        stateModel.countryId = 0
        stateList.add(0, stateModel)
        val spinnerLuggage: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireActivity(), R.layout.simple_item_spinner,
                stateList as List<Any?>
            )
        spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState?.adapter = spinnerLuggage
        for (i in stateList.indices) {
            if(userData?.state?.name == stateList[i].name){
                selected_state_index = i
                selectedStateID = stateList[i].id.toString()
                iAccountPresenter?.cityList(selectedStateID)
            }

        }
        binding.spinnerState?.setSelection(selected_state_index)

    }

    override fun cityListSuccess(model: StateResponsePOJO?) {
        selected_city_index = 0
        cityList = model?.data!!
        binding.rlSpinnerCity?.visibility = View.VISIBLE

        var stateModel : StateResponsePOJO.DataItem = StateResponsePOJO().DataItem()
        stateModel.name = "Select your city"
        stateModel.id = 0
        stateModel.countryId = 0
        cityList.add(0, stateModel)

        val spinnerLuggage: ArrayAdapter<*> =
            ArrayAdapter<Any?>(
                requireActivity(), R.layout.simple_item_spinner,
                cityList as List<Any?>
            )
        spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity?.adapter = spinnerLuggage
        for (i in cityList.indices) {
            if(userData?.city?.name == cityList[i].name){
                selected_city_index = i
                selectedCityID = cityList[i].id.toString()
                binding.spinnerCity?.setSelection(selected_city_index)
            }

        }
        binding.spinnerCity?.setSelection(selected_city_index)
    }

    override fun getProfileSuccess(model: CheckOtpResponsePOJO?) {
        userData = model?.user
        setData()
    }

    override fun showWait() {

    }

    override fun removeWait() {

    }

    override fun onFailure(appErrorMessage: String?) {

    }

    private fun callSportAPI() {

        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        client.getQuestions()!!.enqueue(object :
            Callback<QuestionResponsePOJO?>, AdapterView.OnItemSelectedListener {
            override fun onResponse(
                call: Call<QuestionResponsePOJO?>,
                response: Response<QuestionResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {

                    questionList = response!!.body()!!.questions as ArrayList<QuestionResponsePOJO.QuestionsItem>

                    for (i in response!!.body()!!.questions?.get(0)!!.options!!.indices) {
                        if (response!!.body()!!.questions?.get(0)!!.options?.get(i)!!.status.equals("Active"))
                        {
                            questionSpinner.add(response!!.body()!!.questions?.get(0)!!.options?.get(i)!!.name) }
                    }


                    for (i in questionList.get(0)!!.options!!.indices) {
                        if(questionList.get(0).options!![i].name.equals(userData!!.sport!!.name)){
                               user_sport_id = questionList.get(0).options!![i].id
                        }

                    }

                    for (i in questionList.get(1)!!.options!!.indices) {
                        if (questionList.get(1).options!![i].name.equals(userData!!.professionalLevel!!.name)) {
                            user_Profession_id = questionList.get(1).options!![i].id
                        }

                    }


                    /*      val spinnerLuggage: ArrayAdapter<*> =
                              ArrayAdapter<Any?>(
                                  requireActivity(), R.layout.simple_item_spinner,
                                  questionSpinner as List<Any?>
                              )
                          spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                          spnUserSPort?.adapter = spinnerLuggage
                          for (i in questionSpinner!!.indices) {
                              if(userData!!.sport!!.name.equals(questionSpinner[i])){
                                  user_sport = i
                              }

                          }
                          spnUserSPort?.setSelection(user_sport)
                          spnUserSPort!!.onItemSelectedListener = this
      */

                    /*val spinnerpro: ArrayAdapter<*> =
                   ArrayAdapter<Any?>(
                       requireActivity(), R.layout.simple_item_spinner,
                       userProfession as List<Any?>
                   )
               spinnerpro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               spnUserProfession?.adapter = spinnerpro
               for (i in userProfession!!.indices) {
                   if(userData!!.professionalLevel!!.name.equals(userProfession[i])){
                       user_Profession = i
                   }

               }

               spnUserProfession?.setSelection(user_Profession)
               spnUserProfession!!.onItemSelectedListener = this
*/






                    for (i in response!!.body()!!.questions?.get(1)!!.options!!.indices) {
                        userProfession.add(response!!.body()!!.questions?.get(1)!!.options?.get(i)!!.name)
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
                            activity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        )
                            .show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        activity,
                        "Internal server error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<QuestionResponsePOJO?>,
                t: Throwable
            ) {
                progressDialog.dismiss()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p0!!.id==R.id.spnUserSPort){
                    user_sport = p2

                    for (i in questionList.get(0)!!.options!!.indices) {
                        if(questionList.get(0).options!![i].name.equals(questionSpinner[user_sport])){
                         //   user_sport_id = questionList.get(0).options!![i].id
                        }

                    }

                }else{
                    user_Profession = p2
                  //  user_Profession_id = questionList.get(1).options!![user_Profession].id

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        })
    }

    private fun callAPI() {


        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        client.updateProfile(
            "Bearer $token",
            "Mr.",
            binding.edtFirstName!!.text.toString(),
            binding.edtLastName!!.text.toString(),
            binding.tvUserEmailID!!.text.toString(),
            gender,
            binding.tvUserDOB!!.text.toString(),
            "Athlete",
            binding.tvUserGurdianNo!!.text.toString(),
            binding.edtAcademyName!!.text.toString(),
            selectedStateID?.toInt()!!,
            selectedCityID,
            user_sport_id,user_Profession_id)!!.enqueue(object :
            Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {


                    preferencesManager!!.createLoginSession(response.body()!!.user)
                    SessionManager.getInstance(requireContext())
                        .setUserModel(response.body()!!.user!!)

                    preferencesManager!!.setStringValue(
                        Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC,
                        response.body()!!.user!!.profilePic
                    )



                    Glide.with(this@AccountFragment)
                        .load(response.body()!!.user!!.profilePic)
                        .apply(
                            RequestOptions()
                                .centerCrop()
                                .dontAnimate()
                                .dontTransform()
                        ).into(binding.ivUser!!)




                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT)
                        .show()

                } else if (response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            activity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        )
                            .show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        activity,
                        "Internal server error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {
                progressDialog.dismiss()

            }
        })

    }
}