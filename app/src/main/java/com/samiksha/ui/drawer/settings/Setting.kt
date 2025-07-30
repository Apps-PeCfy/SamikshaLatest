package com.samiksha.ui.drawer.settings

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentSettingBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.settings.pojo.SettingResponsePojo
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Setting : Fragment(), AdapterView.OnItemSelectedListener {


    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    var langId = 0
    var langSpinnerposition = 0


  /*  @JvmField
    @BindView(R.id.spinner_lang)
    var spinner_lang: Spinner? = null

    @JvmField
    @BindView(R.id.spinner_notification)
    var spinner_notification: Spinner? = null

    @JvmField
    @BindView(R.id.spinner_reminder)
    var spinner_reminder: Spinner? = null

    @JvmField
    @BindView(R.id.spinner_tips)
    var spinner_tips: Spinner? = null*/

    var languageList: List<String?>? = null
    var settingpojo: SettingResponsePojo? = null


    //var languageList = arrayOf<String?>("Hindi", "English")
    var reminderList = arrayOf<String?>("On", "Off")
    var notifyList = arrayOf<String?>("On", "Off")
    var dailyTips = arrayOf<String?>("Yes", "No")
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentSettingBinding.inflate(inflater, container, false);


        setHasOptionsMenu(true)
        init()
        getUserSettings()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                if(ProjectUtils.checkInternetAvailable(context)!!){
                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                } else {
                    ProjectUtils.showToast(
                        context,
                        getString(R.string.no_internet_connection)
                    )
                }
            }
        }
        return true
    }


    private fun getUserSettings() {

        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        ApiClient.client.getUserSetting("Bearer $token")!!.enqueue(object :
            Callback<SettingResponsePojo?> {
            override fun onResponse(
                call: Call<SettingResponsePojo?>,
                response: Response<SettingResponsePojo?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {

                    settingpojo = response.body()

                    initSpinner(response.body())


                } else if (response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(activity, pojo.message, Toast.LENGTH_LONG).show()


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
                call: Call<SettingResponsePojo?>,
                t: Throwable
            ) {
                progressDialog.dismiss()

            }
        })


    }


    private fun initSpinner(body: SettingResponsePojo?) {

        languageList = ArrayList()

        for (i in body!!.languageList!!.data!!.indices) {
            (languageList as ArrayList<String?>).add(body!!.languageList!!.data?.get(i)!!.name)

        }

        for (i in body!!.languageList!!.data!!.indices) {

            if (langId.equals(body!!.languageList!!.data?.get(i)!!.id)) {
                langSpinnerposition = i

            }


        }


        val spinnerLuggage: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity().applicationContext,
            R.layout.simple_setting_spinner,
            languageList!!
        )
        spinnerLuggage.setDropDownViewResource(R.layout.simple_setting_spinner)
        binding.spinnerLang.adapter = spinnerLuggage
        binding.spinnerLang.setSelection(langSpinnerposition)
        binding.spinnerLang.onItemSelectedListener = this


        val spinnerReminnder: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity().applicationContext,
            R.layout.simple_setting_spinner,
            reminderList!!
        )
        spinnerReminnder.setDropDownViewResource(R.layout.simple_setting_spinner)
        binding.spinnerReminder.adapter = spinnerReminnder
        binding.spinnerReminder.setSelection(0)


        val spinnerNotification: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity().applicationContext,
            R.layout.simple_setting_spinner, notifyList!!
        )
        spinnerNotification.setDropDownViewResource(R.layout.simple_setting_spinner)
        binding.spinnerNotification.adapter = spinnerNotification
        binding.spinnerNotification.setSelection(0)

        val spinnerDailyTips: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity().applicationContext,
            R.layout.simple_setting_spinner, dailyTips!!
        )
        spinnerDailyTips.setDropDownViewResource(R.layout.simple_setting_spinner)
        binding.spinnerTips.adapter = spinnerDailyTips
        binding.spinnerTips.setSelection(0)


    }
    private fun init() {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Settings"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })


        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        langId = preferencesManager!!.getIntegerValue(Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID)
        setListners()
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        if (parent!!.id == R.id.spinner_lang) {

            langId = settingpojo?.languageList?.data?.get(position)!!.id
            Log.d("deswssasqaq", langId.toString())
        }
    }

    private fun setListners() {
        binding.btnSaveSettings.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val progressDialog = ProgressDialog(activity)
                progressDialog.setCancelable(false) // set cancelable to false
                progressDialog.setMessage("Please Wait") // set message
                progressDialog.show() // show progress dialog

                ApiClient.client.updateLanguage("Bearer $token", langId.toString())!!.enqueue(object :
                    Callback<SettingResponsePojo?> {
                    override fun onResponse(
                        call: Call<SettingResponsePojo?>,
                        response: Response<SettingResponsePojo?>
                    ) {
                        progressDialog.dismiss()
                        if (response.code() == 200) {

                            //  Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()

                            preferencesManager!!.setIntegerValue(Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID,
                                langId)




                        } else if (response.code() == 422) {
                            val gson = GsonBuilder().create()
                            var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                            try {
                                pojo = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ValidationResponsePOJO::class.java
                                )
                                Toast.makeText(activity, pojo.message, Toast.LENGTH_LONG).show()


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
                        call: Call<SettingResponsePojo?>,
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
    }


    /*  @OnClick(R.id.btnSaveSettings)
      fun btnSave() {

          if (ProjectUtils.checkInternetAvailable(context)!!) {

          val progressDialog = ProgressDialog(activity)
          progressDialog.setCancelable(false) // set cancelable to false
          progressDialog.setMessage("Please Wait") // set message
          progressDialog.show() // show progress dialog

          ApiClient.client.updateLanguage("Bearer $token", langId.toString())!!.enqueue(object :
              Callback<SettingResponsePojo?> {
              override fun onResponse(
                  call: Call<SettingResponsePojo?>,
                  response: Response<SettingResponsePojo?>
              ) {
                  progressDialog.dismiss()
                  if (response.code() == 200) {

                    //  Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()

                      preferencesManager!!.setIntegerValue(Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID,
                          langId)




                  } else if (response.code() == 422) {
                      val gson = GsonBuilder().create()
                      var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                      try {
                          pojo = gson.fromJson(
                              response.errorBody()!!.string(),
                              ValidationResponsePOJO::class.java
                          )
                          Toast.makeText(activity, pojo.message, Toast.LENGTH_LONG).show()


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
                  call: Call<SettingResponsePojo?>,
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
  */

}


