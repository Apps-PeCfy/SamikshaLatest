package com.samiksha.ui.sportPsychology.session

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
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
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentEditSessionsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.ui.sportPsychology.session.pojo.GoalResponsePOJO
import com.samiksha.ui.sportPsychology.session.pojo.SessionResponsePOJO
import com.samiksha.ui.sportPsychology.session.pojo.VendorResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EditSessionFragment : Fragment(), AdapterView.OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener {
   /* @JvmField
    @BindView(R.id.edtFirstName)
    var edtFirstName: EditText? = null

  @JvmField
    @BindView(R.id.edtRank)
    var edtRank: EditText? = null


    @JvmField
    @BindView(R.id.edtSessionLink)
    var edtSessionLink: EditText? = null

    @JvmField
    @BindView(R.id.edtComment)
    var edtComment: EditText? = null

    @JvmField
    @BindView(R.id.tvStartDate)
    var tvStartDate: TextView? = null

    @JvmField
    @BindView(R.id.toolbar_home)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.tvUserGender)
    var tvUserGender: Spinner? = null

    @JvmField
    @BindView(R.id.spnUserModel)
    var spnUserModel: Spinner? = null

    @JvmField
    @BindView(R.id.spnVender)
    var spnVender: Spinner? = null

    @JvmField
    @BindView(R.id.spnUserGoals)
    var spnUserGoals: Spinner? = null
*/
    private var sessionManager: SessionManager? = null
    var spngen = 0
    var spnuserModel = 0
    var spnuserGoal = 0
    var spnuser = 0
    var spnUserModelName: String? = ""
    var spnUserGoalName: String? = ""
    var spnUserName: String? = ""


    var statusList =
        arrayOf<String?>("Unscheduled", "Scheduled", "Completed", "Rescheduled", "Cancelled")
    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var questionList = ArrayList<SessionResponsePOJO.DataItem>()
    var userGoalList = ArrayList<GoalResponsePOJO.DataItem>()
    var venderList = ArrayList<VendorResponsePOJO.DataItem>()
    var modelList = ArrayList<String?>()
    var userGoals = ArrayList<String?>()
    var venders = ArrayList<String?>()

    var subSchdulID = 0
    var moduleID = 0
    var goalID = 0
    var userID = 0
    var updatedStatus: String? = ""

    var mday = 0
    var mmonth = 0
    var myear = 0
    var mHour = 0
    var mMinute = 0
    var calendar: Calendar? = null
    var strMonth: String? = null
    var stDay: String? = null
    var edtSessionDate: String? = null

    var isDateChanged: Boolean? = false
    lateinit var binding: FragmentEditSessionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_edit_sessions, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentEditSessionsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
    }

    private fun initView() {

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        sessionManager = SessionManager.getInstance(requireContext())
        setListners()

        val bundle = this.arguments
        if (bundle != null) {
            val myInt = bundle.getString("EditSessionData")
            var obj = JSONObject(myInt)

            binding.edtFirstName!!.isEnabled = false
            if (myInt!!.contains("name")) {
                binding.edtFirstName!!.setText(obj.getString("name"))
            } else {
                binding.edtFirstName!!.setText("")
            }

            if (myInt!!.contains("rank")) {
                binding.edtRank!!.setText(obj.getString("rank"))

            } else {
                binding.edtRank!!.setText("")

            }

            if (myInt!!.contains("module_name")) {
                spnUserModelName = obj.getString("module_name")
            } else {
                spnUserModelName = ""
            }

            if (myInt!!.contains("counsellor")) {
                spnUserName = obj.getString("counsellor")
            } else {
                spnUserName = ""
            }

            if (myInt!!.contains("goal")) {
                var obj1 = JSONObject(obj.getString("goal"))

                spnUserGoalName = obj1.getString("name")
            } else {
                spnUserGoalName = ""
            }




            if (myInt!!.contains("link")) {
                binding.edtSessionLink!!.setText(obj.getString("link"))
            } else {
                binding.edtSessionLink!!.setText("")
            }

            subSchdulID = obj.getString("id").toInt()

            if (myInt!!.contains("comment")) {
                binding.edtComment!!.setText(obj.getString("comment"))
            } else {
                binding.edtComment!!.setText("")

            }
            if (myInt!!.contains("startDate")) {
                binding.tvStartDate!!.text = obj.getString("startDate")
                edtSessionDate = obj.getString("startDate")
            } else {
                binding.tvStartDate!!.text = ""
                edtSessionDate = ""
            }
            var status: String? = ""
            if (myInt!!.contains("status")) {
                status = obj.getString("status")
            } else {
                status = ""
            }

            for (k in statusList!!.indices) {
                if (statusList[k].equals(status)) {
                    spngen = k
                    break

                }

            }
            val spinnerLuggage: ArrayAdapter<*> =
                ArrayAdapter<Any?>(requireActivity(), R.layout.simple_item_spinner, statusList)
            spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.tvUserGender?.adapter = spinnerLuggage
            binding.tvUserGender?.setSelection(spngen)
            binding.tvUserGender!!.onItemSelectedListener = this

            callVender()
            callGoals()
            callSessionMentalSkill()


        }
    }

    private fun callVender() {
        ApiClient.client.getVendors("Bearer $token", "`Counsellor`")!!.enqueue(object :
            Callback<VendorResponsePOJO?>, AdapterView.OnItemSelectedListener {
            override fun onResponse(
                call: Call<VendorResponsePOJO?>,
                response: Response<VendorResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    venderList =
                        response.body()!!.data as ArrayList<VendorResponsePOJO.DataItem>
                    venders.add(0, "Select Psychologist")

                    for (i in venderList.indices) {
                        venders.add(i + 1, venderList[i].firstName + " " + venderList[i].lastName)
                    }

                    for (k in venders!!.indices) {
                        if (venders[k].equals(spnUserName)) {
                            spnuser = k
                            break

                        }else{
                            spnuser = 0
                        }

                    }

                    val spinnerVender: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireActivity(), R.layout.simple_item_spinner,
                            venders as List<Any?>
                        )
                    spinnerVender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnVender?.adapter = spinnerVender
                    binding.spnVender?.setSelection(spnuser)
                    binding.spnVender!!.onItemSelectedListener = this


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
                call: Call<VendorResponsePOJO?>,
                t: Throwable
            ) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position > 0) {
                    userID = venderList[position - 1].id
                } else {
                    userID = 0

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        })

    }

    private fun callGoals() {
        ApiClient.client.getAllGoals()!!.enqueue(object :
            Callback<GoalResponsePOJO?>, AdapterView.OnItemSelectedListener {
            override fun onResponse(
                call: Call<GoalResponsePOJO?>,
                response: Response<GoalResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    userGoalList =
                        response.body()?.data as ArrayList<GoalResponsePOJO.DataItem>
                    for (i in userGoalList.indices) {
                        userGoals.add(userGoalList.get(i).name)
                    }


                    for (k in userGoals!!.indices) {
                        if (userGoals[k].equals(spnUserGoalName)) {
                            spnuserGoal = k
                            break

                        }

                    }

                    val spinnerUserGoals: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireActivity(), R.layout.simple_item_spinner,
                            userGoals as List<Any?>
                        )
                    spinnerUserGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnUserGoals?.adapter = spinnerUserGoals
                    binding.spnUserGoals?.setSelection(spnuserGoal)
                    binding.spnUserGoals!!.onItemSelectedListener = this


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
                call: Call<GoalResponsePOJO?>,
                t: Throwable
            ) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (p0!!.id == R.id.spnUserGoals) {
                    Log.d("onItemSelectedGoals", userGoalList[position].id.toString())
                    goalID = userGoalList[position].id
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        })
    }

    private fun callSessionMentalSkill() {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        ApiClient.client.getSessionMentalSkills("Bearer $token")!!.enqueue(object :
            Callback<SessionResponsePOJO?>, AdapterView.OnItemSelectedListener {
            override fun onResponse(
                call: Call<SessionResponsePOJO?>,
                response: Response<SessionResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {
                    questionList = response.body()!!.data as ArrayList<SessionResponsePOJO.DataItem>
                    for (i in questionList.indices) {
                        modelList.add(questionList.get(i).name)
                    }

                    for (k in modelList!!.indices) {
                        if (modelList[k].equals(spnUserModelName)) {
                            spnuserModel = k
                            break

                        }

                    }


                    val spinnerLuggage: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(
                            requireActivity(), R.layout.simple_item_spinner,
                            modelList as List<Any?>
                        )
                    spinnerLuggage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnUserModel?.adapter = spinnerLuggage
                    binding.spnUserModel?.setSelection(spnuserModel)
                    binding.spnUserModel!!.onItemSelectedListener = this


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
                call: Call<SessionResponsePOJO?>,
                t: Throwable
            ) {
                progressDialog.dismiss()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d("onItemSelectedModule", questionList[position].id.toString())
                moduleID = questionList[position].id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        })
    }


    fun onBackPressed() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(context, AcademyMembersActivity::class.java)
        intent!!.action = "Sessions"
        requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (p0!!.id == R.id.tvUserGender) {
            Log.d("onItemSelectedstatus", statusList[position]!!)
            updatedStatus = statusList[position]!!

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
    private fun setListners() {
        binding.btnSaveEditSession.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                if (isDateChanged == false) {
                    if (edtSessionDate!!.isNotEmpty()) {


                        var oldFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a")
                        var newFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
                        val newformaredDate = newFormat.format(oldFormat!!.parse(edtSessionDate)!!)
                        Log.d("waqsqss", newformaredDate)
                        binding.tvStartDate!!.setText(newformaredDate)
                    } else {
                        binding.tvStartDate!!.setText("")

                    }


                }


                var counsellorID: String? = ""
                if (userID == 0) {
                    counsellorID = ""
                } else {
                    counsellorID = userID.toString()
                }


                val progressDialog = ProgressDialog(activity)
                progressDialog.setCancelable(false) // set cancelable to false
                progressDialog.setMessage("Please Wait") // set message
                progressDialog.show() // show progress dialog

                ApiClient.client.updateUserSubSchedules(
                    "Bearer $token",
                    subSchdulID,
                    binding.edtFirstName!!.text.toString(),
                    moduleID,
                    counsellorID,
                    goalID,
                    binding.tvStartDate!!.text.toString(),
                    binding.edtSessionLink!!.text.toString(),
                    updatedStatus,
                    binding.edtComment!!.text.toString(),
                    binding.edtRank!!.text.toString()



                )!!.enqueue(object :
                    Callback<OnlyMessageResponsePOJO?> {
                    override fun onResponse(
                        call: Call<OnlyMessageResponsePOJO?>,
                        response: Response<OnlyMessageResponsePOJO?>
                    ) {
                        progressDialog.dismiss()
                        if (response.code() == 200) {
                            val intent = Intent(context, AcademyMembersActivity::class.java)
                            intent!!.action = "Sessions"
                            requireActivity().startActivity(intent)
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
                        call: Call<OnlyMessageResponsePOJO?>,
                        t: Throwable
                    ) {
                        progressDialog.dismiss()

                    }

                })

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.btnSaveEditSession.setOnClickListener(View.OnClickListener {
            calendar = Calendar.getInstance()
            myear = calendar!!.get(Calendar.YEAR)
            mmonth = calendar!!.get(Calendar.MONTH)
            mHour = calendar!!.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar!!.get(Calendar.MINUTE)
            mday = calendar!!.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(), this@EditSessionFragment, myear, mmonth, mday)
            datePickerDialog.datePicker.minDate = calendar!!.timeInMillis
            datePickerDialog.show()
        })
    }

   /* @OnClick(R.id.btnSaveEditSession)
    fun btnSaveEditSession() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (isDateChanged == false) {
            if (edtSessionDate!!.isNotEmpty()) {


                var oldFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a")
                var newFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
                val newformaredDate = newFormat.format(oldFormat!!.parse(edtSessionDate)!!)
                Log.d("waqsqss", newformaredDate)
                tvStartDate!!.setText(newformaredDate)
            } else {
                tvStartDate!!.setText("")

            }


        }


        var counsellorID: String? = ""
        if (userID == 0) {
            counsellorID = ""
        } else {
            counsellorID = userID.toString()
        }


        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        ApiClient.client.updateUserSubSchedules(
            "Bearer $token",
            subSchdulID,
            edtFirstName!!.text.toString(),
            moduleID,
            counsellorID,
            goalID,
            tvStartDate!!.text.toString(),
            edtSessionLink!!.text.toString(),
            updatedStatus,
            edtComment!!.text.toString(),
            edtRank!!.text.toString()



            )!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {
                    val intent = Intent(context, AcademyMembersActivity::class.java)
                    intent!!.action = "Sessions"
                    requireActivity().startActivity(intent)
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
                call: Call<OnlyMessageResponsePOJO?>,
                t: Throwable
            ) {
                progressDialog.dismiss()

            }

        })

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.tvStartDate)
    fun tvStartDate() {
        calendar = Calendar.getInstance()
        myear = calendar!!.get(Calendar.YEAR)
        mmonth = calendar!!.get(Calendar.MONTH)
        mHour = calendar!!.get(Calendar.HOUR_OF_DAY)
        mMinute = calendar!!.get(Calendar.MINUTE)
        mday = calendar!!.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireActivity(), this@EditSessionFragment, myear, mmonth, mday)
        datePickerDialog.datePicker.minDate = calendar!!.timeInMillis
        datePickerDialog.show()


    }
*/
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
       var formatRide = SimpleDateFormat("yyyy-MM-dd")
       var formatDate = SimpleDateFormat("MMM dd, yyyy")
       // val formaredDate = formatDate.format(formatRide!!.parse("$myear-" + strMonth + "-" + stDay)!!)
       val formaredDate = "$myear-" + strMonth + "-" + stDay

       val timePickerDialog = TimePickerDialog(
           requireActivity(), { view, hourOfDay, minute ->

               var AM_PM: String? = null
               var hour: String? = ""
               var mMinutes: String? = ""


               if (hourOfDay < 13) {
                   hour = hourOfDay.toString()
                   mMinutes = minute.toString()
                   if (hourOfDay == 12) {
                       AM_PM = "PM"

                   } else {
                       if (hourOfDay == 0) {
                           hour = "12"

                       }
                       AM_PM = "AM"

                   }

                   if (hour.length == 1) {
                       hour = ("0" + hour)
                   }
                   if (mMinutes.length == 1) {
                       mMinutes = ("0" + mMinutes)
                   }

               } else {
                   AM_PM = "PM"
                   hour = (hourOfDay - 12).toString()
                   mMinutes = minute.toString()
                   if (mMinutes.length == 1) {
                       mMinutes = ("0" + mMinutes)

                   }


                   if (hour.length == 1) {
                       hour = ("0" + hour)
                   }


               }

               val checkOldDate:Calendar = Calendar.getInstance()

               val currentTime:Calendar = Calendar.getInstance()
               val sdf = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
               val parsedDate = sdf.parse(currentTime.time.toString())
               val print = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
               var cTime = print.format(parsedDate)

               if(currentTime.get(Calendar.AM_PM)==Calendar.AM)
               {
                   cTime = cTime+" AM"
               }else{
                   cTime = cTime+" PM"
               }

               var sTime = formaredDate+" "+hourOfDay.toString()+":"+mMinutes+":"+"00 "+AM_PM

               try {
                   val current_Time = print.parse(cTime)
                   val selected_Time = print.parse(sTime)

                   if(selected_Time!!.equals(currentTime)){
                       isDateChanged = true
                       binding.tvStartDate!!.setText(formaredDate + " " + hour + ":" + mMinutes + ":00" + " " + AM_PM)
                   }else if(selected_Time!!.after(current_Time)){
                       Log.d("qwesaed","AFTER")
                       isDateChanged = true
                       binding.tvStartDate!!.setText(formaredDate + " " + hour + ":" + mMinutes + ":00" + " " + AM_PM)
                   }else{
                       Log.d("qwesaed","BEFORE")
                       binding.tvStartDate!!.text = ""
                       Toast.makeText(activity, "Please Select valid date & time.", Toast.LENGTH_LONG).show()

                   }


               } catch (e: ParseException) {
                   e.printStackTrace()
               }





               if(hourOfDay>=checkOldDate.get(Calendar.HOUR_OF_DAY)  && minute>=checkOldDate.get(Calendar.MINUTE)){

                   Log.d("ddsds", hourOfDay.toString())

                   // tvStartDate!!.setText(formaredDate)
               }else{
               }



           },
           mHour,
           mMinute,
           false
       )
       timePickerDialog.show()


   }

}