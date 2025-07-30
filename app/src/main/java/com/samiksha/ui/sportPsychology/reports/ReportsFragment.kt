package com.samiksha.ui.sportPsychology.reports

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.samiksha.databinding.FragmentReportsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentReportResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ReportsFragment : Fragment(), ReportsAdapter.IClickListener {

    var reportsAdapter: ReportsAdapter? = null

   /* @JvmField
    @BindView(R.id.recyclerViewReports)
    var recyclerViewReports: RecyclerView? = null

    @JvmField
    @BindView(R.id.txtUserName)
    var txtUserName: TextView? = null

    @JvmField
    @BindView(R.id.ll_noreport)
    var ll_noreport: LinearLayout? = null
*/
    var token: String? = null
    var userID: String? = null
    var preferencesManager: PreferencesManager? = null
    private var getAssesmentList: List<AssesmentReportResponsePOJO.DataItem>? = ArrayList()

    lateinit var binding: FragmentReportsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_reports, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentReportsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
    }


    private fun initView() {

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance

        binding.txtUserName!!.visibility = View.VISIBLE
        binding.txtUserName!!.text = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_SELECTED_USER_NAME)
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        userID =
            preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)


        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.getAssesmentList("Bearer $token", userID)!!.enqueue(object :
            Callback<AssesmentReportResponsePOJO?> {
            override fun onResponse(
                call: Call<AssesmentReportResponsePOJO?>,
                response: Response<AssesmentReportResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {

                        getAssesmentList = response!!.body()!!.assessments!!.data

                        if (getAssesmentList!!.isNotEmpty()) {
                            binding.recyclerViewReports!!.visibility = View.VISIBLE
                            binding.llNoreport!!.visibility = View.GONE

                            reportsAdapter = ReportsAdapter(requireActivity(), getAssesmentList)
                            binding.recyclerViewReports!!.layoutManager = LinearLayoutManager(activity)
                            binding.recyclerViewReports!!.adapter = reportsAdapter
                            reportsAdapter!!.setClickListener(this@ReportsFragment)

                            reportsAdapter!!.notifyDataSetChanged()

                        }else{
                            binding.recyclerViewReports!!.visibility = View.GONE
                            binding.llNoreport!!.visibility = View.VISIBLE

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
                call: Call<AssesmentReportResponsePOJO?>, t: Throwable
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

    override fun memberClick(position: Int?) {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, ReportsPdfActivity::class.java)
            intent.putExtra("AssesmentID", position.toString())

            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

}