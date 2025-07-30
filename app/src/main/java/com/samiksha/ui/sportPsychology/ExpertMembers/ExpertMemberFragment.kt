package com.samiksha.ui.sportPsychology.ExpertMembers

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentAcademyMembersBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersAdapter
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ExpertMemberFragment : Fragment(), ExpertMemberAdapter.IClickListener,
    AcademyMembersAdapter.IClickListener {
   /* @JvmField
    @BindView(R.id.recycler_view)
    var recycler_view: RecyclerView? = null

    @JvmField
    @BindView(R.id.recyclerViewPlayers)
    var recyclerViewPlayers: RecyclerView? = null

    @JvmField
    @BindView(R.id.ll_main)
    var mainll: LinearLayout? = null

    @JvmField
    @BindView(R.id.swipe_refresh)
    var swipeRefresh: SwipeRefreshLayout? = null

    @JvmField
    @BindView(R.id.tvPlayers)
    var tvPlayers: TextView? = null

    @JvmField
    @BindView(R.id.tvPsychologist)
    var tvPsychologist: TextView? = null

    @JvmField
    @BindView(R.id.txt_no_data)
    var txtNoData: TextView? = null

    @JvmField
    @BindView(R.id.txtNoPlayersData)
    var txtNoPlayersData: TextView? = null

    @JvmField
    @BindView(R.id.edt_search)
    var edtSearch: EditText? = null
*/
    var token: String? = null
    var preferencesManager: PreferencesManager? = null


    var expertMemberAdapter: ExpertMemberAdapter? = null
    var academyMembersAdapter: AcademyMembersAdapter? = null


    private var getUserList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var getPlayersList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var initialList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var initialPlayerList: ArrayList<UserResponsePOJO.DataItem>? = ArrayList()
    private var userData: CheckOtpResponsePOJO.User? = null
    private var expertUser: UserResponsePOJO? = null
    private var sessionManager: SessionManager? = null
    lateinit var binding: FragmentAcademyMembersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val rootView = inflater.inflate(R.layout.fragment_academy_members, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentAcademyMembersBinding.inflate(inflater, container, false);

        initView()

        return binding.root
    }

    private fun initView() {
        binding.recyclerViewPlayers.isNestedScrollingEnabled = false
        binding.recyclerView.isNestedScrollingEnabled = false

        sessionManager = SessionManager.getInstance(requireContext())

        userData = sessionManager?.getUserModel()

        callUserAPI()


        setListeners()


    }

    private fun setListeners() {
        /* swipeRefresh?.setOnRefreshListener {
             // callUserAPI()
             swipeRefresh?.isRefreshing = false

         }
 */
        binding.edtSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length!! > 3) {
                        callSearchAPI(s)
                        /*for (model in initialList!!) {
                            if (model.name?.toUpperCase()
                                    ?.contains(s.toString().toUpperCase()) == true
                            ) {
                                sortedList.add(model)
                            }
                        }*/

                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.recyclerViewPlayers.visibility = View.VISIBLE
                        getUserList?.clear()
                        getPlayersList?.clear()
                        initialList?.let { getUserList?.addAll(it) }
                        initialPlayerList?.let { getPlayersList?.addAll(it) }
                        expertMemberAdapter?.updateAdapter(getUserList)
                        academyMembersAdapter?.updateAdapter(getPlayersList)
                    }

                    if (getUserList.isNullOrEmpty()) {
                        binding.txtNoData.visibility = View.VISIBLE
                    } else {
                        binding.txtNoData.visibility = View.GONE
                    }

                    if (getPlayersList.isNullOrEmpty()) {
                        binding.txtNoPlayersData.visibility = View.VISIBLE
                    } else {
                        binding.txtNoPlayersData.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            }
        )
    }

    private fun callSearchAPI(searchKey: CharSequence) {
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        ApiClient.client.getSearchUsers("Bearer $token", searchKey)!!.enqueue(object :
            Callback<UserResponsePOJO?> {
            override fun onResponse(
                call: Call<UserResponsePOJO?>,
                response: Response<UserResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {


                        /*   var sortedList: ArrayList<UserResponsePOJO.DataItem> = ArrayList()
                           sortedList = response!!.body()!!.data!!
                           getUserList?.addAll(sortedList)

   */
                        getUserList?.clear()
                        getPlayersList?.clear()
                        for (i in response.body()!!.data!!.indices) {
                            if (response!!.body()!!.data!![i].role.equals("Counsellor")) {
                                getUserList!!.add(response!!.body()!!.data!![i]!!)
                            } else {
                                getPlayersList!!.add(response!!.body()!!.data!![i]!!)
                            }

                        }


                        preferencesManager!!.createCoachOrCounsellorSession(response.body())
                        sessionManager?.setCoachOrCounsellorModel(response.body())

                        if (getUserList.isNullOrEmpty()) {
                            binding.recyclerView.visibility = View.GONE
                            binding.txtNoData.visibility = View.VISIBLE
                        } else {
                            binding.txtNoData.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                            expertMemberAdapter?.updateAdapter(getUserList)

                        }



                        if (getPlayersList.isNullOrEmpty()) {
                            binding.recyclerViewPlayers.visibility = View.GONE
                            binding.txtNoPlayersData.visibility = View.VISIBLE
                        } else {

                            binding.txtNoPlayersData.visibility = View.GONE
                            binding.recyclerViewPlayers.visibility = View.VISIBLE
                            academyMembersAdapter?.updateAdapter(getPlayersList)
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
                            pojo.errors!![0].message,
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

            override fun onFailure(
                call: Call<UserResponsePOJO?>, t: Throwable
            ) {
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

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


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
                // binding.swipeRefresh?.isRefreshing = false
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {

                        binding.llMain.visibility = View.VISIBLE
                        binding.tvPsychologist.visibility = View.VISIBLE
                        binding.tvPlayers.visibility = View.VISIBLE

                        getUserList?.clear()
                        getPlayersList?.clear()
                        for (i in response.body()!!.data!!.indices) {
                            if (response!!.body()!!.data!![i].role.equals("Counsellor")) {
                                getUserList!!.add(response!!.body()!!.data!![i]!!)
                            } else {
                                getPlayersList!!.add(response!!.body()!!.data!![i]!!)
                            }

                        }


                        //  getUserList = response.body()!!.data
                        initialList?.clear()
                        initialPlayerList?.clear()
                        getUserList?.let { initialList?.addAll(it) }
                        getPlayersList?.let { initialPlayerList?.addAll(it) }

                        if (userData!!.name != null) {
                            binding.tvCoachName.text = userData!!.name
                        }
                        binding.tvNumberPlayers.text = getUserList?.size.toString()
                        if (userData?.role.equals("Coach")) {
                            binding.tvCoachSport.visibility = View.VISIBLE
                            binding.tvCoachSport.text = userData?.sport?.name
                            binding.tvAcademyName.text = userData?.academyInstitute
                        } else {
                            binding.tvAcademyName.text = userData?.designation

                        }
                        binding.tvCityState.text = userData?.city?.name + ", " + userData?.state?.name

                        expertUser = response.body()

                        preferencesManager?.createCoachOrCounsellorSession(response.body())
                        sessionManager?.setCoachOrCounsellorModel(response.body())




                        if (getUserList?.size!! > 0) {
                            binding.txtNoData.visibility = View.GONE

                            binding.recyclerView.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            binding.recyclerView.itemAnimator = DefaultItemAnimator()

                            if (activity != null) {
                                expertMemberAdapter =
                                    ExpertMemberAdapter(
                                        requireActivity(),
                                        getUserList,
                                        userData?.role
                                    )
                                binding.recyclerView.adapter = expertMemberAdapter
                                expertMemberAdapter?.setClickListener(this@ExpertMemberFragment)
                            }
                            expertMemberAdapter?.notifyDataSetChanged()

                        } else {
                            binding.txtNoData.visibility = View.VISIBLE
                        }


                        //Players RecyclerView//

                        if (getPlayersList.isNullOrEmpty()) {

                            binding.txtNoPlayersData.visibility = View.VISIBLE

                        } else {

                            binding.txtNoPlayersData.visibility = View.GONE

                            binding.recyclerViewPlayers.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            binding.recyclerViewPlayers.itemAnimator = DefaultItemAnimator()

                            if (activity != null) {
                                academyMembersAdapter = AcademyMembersAdapter(
                                    requireActivity(),
                                    getPlayersList,
                                    userData?.role
                                )
                                binding.recyclerViewPlayers.adapter = academyMembersAdapter
                                academyMembersAdapter?.setClickListener1(this@ExpertMemberFragment)

                            }
                            academyMembersAdapter?.updateAdapter(getPlayersList)
                            academyMembersAdapter?.notifyDataSetChanged()


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
                            pojo.errors!![0].message,
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

            override fun onFailure(
                call: Call<UserResponsePOJO?>, t: Throwable
            ) {
                // swipeRefresh?.isRefreshing = false
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

    override fun expertMemberClick(position: Int, id: String?) {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION_EXPERT_MEMBERS,
                id
            )


            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_COUNSELLOR_PLAYERS,
                ""
            )

            preferencesManager!!.createExpertSession(expertUser!!.DataItem())
            sessionManager?.setExpertModel(expertUser!!.data!![position])


            val intent = Intent(activity, AcademyMembersActivity::class.java)
            intent.action = "ExpertMember"
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    override fun memberClick(position: Int, model: UserResponsePOJO.DataItem?) {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setIntegerValue(
                Constants.SHARED_PREFERENCE_LOGIN_USER_SELECTED_POSITION,
                position + getUserList!!.size
            )
            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION,
                model?.id
            )

            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_PLAYER_REGISTER_DATE,
                model?.registerDate
            )


            preferencesManager!!.setStringValue(
                Constants.SHARED_PREFERENCE_COUNSELLOR_PLAYERS,
                "MasterUser"
            )

            val intent = Intent(requireActivity(), AcademyMembersActivity::class.java)
            intent.action = "AcademyMembers"
            startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

}


