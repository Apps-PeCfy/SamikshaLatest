package com.samiksha.ui.sportPsychology.session

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentMySessionsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.mysession.MySessionResponsePOJO
import com.samiksha.ui.drawer.mysession.MySessionsAdapter
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MySessionCoach : Fragment(), MySessionSubAdapter.ICommentClickListener {

    var mySessionsAdapter: MySessionsAdapter? = null


    var token: String? = null
    var userId: String? = null
    var userRole: String? = null
    var preferencesManager: PreferencesManager? = null


   /* @JvmField
    @BindView(R.id.recycler_mysessions)
    var recycler_mysessions: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvNoSessionData)
    var tvNoSessionData: TextView? = null

    @JvmField
    @BindView(R.id.txtUserName)
    var txtUserName: TextView? = null

    @JvmField
    @BindView(R.id.ll_no_data)
    var ll_no_data: LinearLayout? = null
*/
    private var getSessionList: List<MySessionResponsePOJO.DataItem>? = ArrayList()
    var playerRole: String? = ""


    lateinit var binding: FragmentMySessionsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_my_sessions, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentMySessionsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
    }


    private fun initView() {


        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        userRole = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)
        userId = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)

        binding.txtUserName!!.visibility = View.VISIBLE
        binding.txtUserName!!.text = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_SELECTED_USER_NAME)

        if (userRole.equals("SuperCounsellor")) {
            playerRole =
                preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_COUNSELLOR_PLAYERS)


        }



        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.getSessionList("Bearer $token", userId)!!.enqueue(object :
            Callback<MySessionResponsePOJO?> {
            override fun onResponse(
                call: Call<MySessionResponsePOJO?>,
                response: Response<MySessionResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {

                        getSessionList = response.body()!!.data

                        if (!getSessionList.isNullOrEmpty()) {

                            if (getSessionList!!.size > 0) {

                                //  tvNoSessionData?.visibility = View.GONE
                                binding.llNoData.visibility = View.GONE
                                binding.recyclerMysessions.visibility = View.VISIBLE

                                mySessionsAdapter =
                                    MySessionsAdapter(
                                        requireActivity(),
                                        getSessionList,
                                        userRole,
                                        playerRole
                                    )
                                binding.recyclerMysessions.layoutManager =
                                    LinearLayoutManager(activity)
                                binding.recyclerMysessions.adapter = mySessionsAdapter
                            } else {
                                // tvNoSessionData?.visibility = View.VISIBLE
                                binding.llNoData.visibility = View.VISIBLE
                                binding.recyclerMysessions.visibility = View.GONE

                            }

                        } else {
                            //    tvNoSessionData?.visibility = View.VISIBLE
                            binding.llNoData.visibility = View.VISIBLE
                            binding.recyclerMysessions.visibility = View.GONE
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

            override fun onFailure(
                call: Call<MySessionResponsePOJO?>, t: Throwable
            ) {
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



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                if(ProjectUtils.checkInternetAvailable(context)!!){
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


    override fun commentClick(comment: String?, id: Int) {


    }


}