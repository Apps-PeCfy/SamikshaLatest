package com.samiksha.ui.sportPsychology.academyMembers

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentAcademyMembersBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AcademyMembersFragment : Fragment(), AcademyMembersAdapter.IClickListener {

    lateinit var binding: FragmentAcademyMembersBinding
    var token: String? = null
    var userRole: String? = null
    var preferencesManager: PreferencesManager? = null


    var academyMembersAdapter: AcademyMembersAdapter? = null


    private var getUserList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var initialList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var userData: CheckOtpResponsePOJO.User? = null
    private var ExpertCounsellor: UserResponsePOJO.DataItem? = null
    private var sessionManager: SessionManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcademyMembersBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {

        sessionManager = SessionManager.getInstance(requireContext())

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        userRole = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)

        binding.tvPlayers.visibility = View.GONE
        binding.tvPsychologist.visibility = View.GONE
        callAPI()
        setListeners()


    }

    private fun setListeners() {
        binding.apply {
            /*    swipeRefresh.setOnRefreshListener {
                    callAPI()
                }
    */
            edtSearch.addTextChangedListener(
                object : TextWatcher {

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s?.length!! > 1) {
                            val sortedList: ArrayList<UserResponsePOJO.DataItem> = ArrayList()
                            for (model in initialList!!) {
                                if (model.name?.toUpperCase()
                                        ?.contains(s.toString().toUpperCase()) == true
                                ) {
                                    sortedList.add(model)
                                }
                            }
                            getUserList?.clear()
                            getUserList?.addAll(sortedList)
                            academyMembersAdapter?.updateAdapter(getUserList)
                        } else {
                            getUserList?.clear()
                            initialList?.let { getUserList?.addAll(it) }
                            academyMembersAdapter?.updateAdapter(getUserList)
                        }

                        if (getUserList.isNullOrEmpty()) {
                            txtNoData.visibility = View.VISIBLE
                        } else {
                            txtNoData.visibility = View.GONE
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                }
            )
        }
    }

    private fun callAPI() {
        if (userRole.equals("SuperCounsellor")) {
            setHasOptionsMenu(true)

            ExpertCounsellor = sessionManager!!.getExpertModel()

            callUserAPIWithID()

        } else {
            userData = sessionManager!!.getUserModel()

            callUserAPI()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                if (ProjectUtils.checkInternetAvailable(context)!!) {
                    val intent = Intent(activity, AcademyMembersActivity::class.java)
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


    /**
     * API CALLING
     */


    private fun callUserAPIWithID() {

        val UserId =
            preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION_EXPERT_MEMBERS)


        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.getuserWithID("Bearer $token", UserId)!!.enqueue(object :
            Callback<UserResponsePOJO?> {
            override fun onResponse(
                call: Call<UserResponsePOJO?>,
                response: Response<UserResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                binding.apply {
                    //  swipeRefresh.isRefreshing = false
                    if (response.code() == 200) {
                        if (response.body() != null) {

                            llMain.visibility = View.VISIBLE
                            getUserList = response.body()!!.data
                            initialList?.clear()
                            getUserList?.let { initialList?.addAll(it) }


                            tvCoachName.text = ExpertCounsellor!!.name
                            if (ExpertCounsellor?.role.equals("Coach")) {
                                tvCoachSport.visibility = View.VISIBLE
                                tvCoachSport.text = ExpertCounsellor?.sport!!.name
                                tvAcademyName.text =
                                    ExpertCounsellor!!.academyInstitute + ", " + ExpertCounsellor?.city?.name
                            } else {
                                tvAcademyName.text = ExpertCounsellor!!.designation

                            }
                            tvCityState.text =
                                ExpertCounsellor?.city?.name + ", " + ExpertCounsellor?.state!!.name

                            preferencesManager!!.createCoachOrCounsellorSession(response.body())
                            SessionManager.getInstance(requireContext())
                                .setCoachOrCounsellorModel(response.body())




                            if (getUserList!!.size > 0) {

                                binding.recyclerView.visibility = View.VISIBLE
                                binding.llNoPlayer.visibility = View.GONE

                                recyclerView.layoutManager = LinearLayoutManager(
                                    activity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )


                                recyclerView.itemAnimator = DefaultItemAnimator()

                                if (activity != null) {
                                    academyMembersAdapter =
                                        AcademyMembersAdapter(
                                            requireActivity(),
                                            getUserList,
                                            ExpertCounsellor?.role
                                        )
                                    recyclerView.adapter = academyMembersAdapter
                                    academyMembersAdapter!!.setClickListener(this@AcademyMembersFragment)
                                }
                            } else {
                                binding.recyclerView.visibility = View.GONE
                                binding.llNoPlayer.visibility = View.VISIBLE
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
                                activity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

            }

            override fun onFailure(
                call: Call<UserResponsePOJO?>, t: Throwable
            ) {
                //  binding.swipeRefresh.isRefreshing = false
                progressDialogLogout.dismiss()
                Toast.makeText(
                    activity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    private fun callUserAPI() {


        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.getuser("Bearer $token")!!.enqueue(object :
            Callback<UserResponsePOJO?> {
            override fun onResponse(
                call: Call<UserResponsePOJO?>,
                response: Response<UserResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()

                binding.apply {
                    //  swipeRefresh.isRefreshing = false

                    if (response.code() == 200) {
                        if (response.body() != null) {

                            binding.llMain.visibility = View.VISIBLE

                            getUserList = response.body()!!.data
                            initialList?.clear()
                            getUserList?.let { initialList?.addAll(it) }

                            if (userData!!.name != null) {
                                tvCoachName.text = userData!!.name

                            }
                            if (userData?.role.equals("Coach")) {
                                tvCoachSport.visibility = View.VISIBLE
                                tvCoachSport.text = userData?.sport!!.name
                                tvAcademyName.text =
                                    userData!!.academyInstitute + ", " + userData?.city?.name
                            } else {
                                tvAcademyName.text = userData!!.designation

                            }
                            tvCityState.text = userData?.city?.name + ", " + userData?.state!!.name

                            if (userRole.equals("SuperCounsellor")) {

                            } else {

                            }
                            preferencesManager!!.createCoachOrCounsellorSession(response.body())
                            SessionManager.getInstance(requireContext())
                                .setCoachOrCounsellorModel(response.body())


                            if (getUserList!!.size > 0) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.llNoPlayer.visibility = View.GONE



                                recyclerView.layoutManager = LinearLayoutManager(
                                    activity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )


                                recyclerView.itemAnimator = DefaultItemAnimator()

                                if (activity != null) {
                                    academyMembersAdapter =
                                        AcademyMembersAdapter(
                                            requireActivity(),
                                            getUserList,
                                            userData?.role
                                        )
                                    recyclerView.adapter = academyMembersAdapter
                                    academyMembersAdapter!!.setClickListener(this@AcademyMembersFragment)
                                }
                            } else {
                                binding.recyclerView.visibility = View.GONE
                                binding.llNoPlayer.visibility = View.VISIBLE

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
                                activity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

            }

            override fun onFailure(
                call: Call<UserResponsePOJO?>, t: Throwable
            ) {
                //  binding.swipeRefresh.isRefreshing = false
                progressDialogLogout.dismiss()
                Toast.makeText(
                    activity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    override fun memberClick(position: Int, model: UserResponsePOJO.DataItem?) {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setIntegerValue(
                Constants.SHARED_PREFERENCE_LOGIN_USER_SELECTED_POSITION,
                position
            )
            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION,
                model?.id
            )
            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_PLAYER_REGISTER_DATE,
                model?.registerDate
            )

            val intent = Intent(requireActivity(), AcademyMembersActivity::class.java)
            intent!!.action = "AcademyMembers"
            startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

}