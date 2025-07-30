package com.samiksha.ui.progressReport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.samiksha.R
import com.samiksha.databinding.ActivityProgressReportBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils

class ProgressReportActivity : AppCompatActivity(), IProgressReportView {
    lateinit var binding: ActivityProgressReportBinding
    private var context: Context = this
    private var skillID: String? = null
    private var selectedDate: String? = null

    private var list: ArrayList<ProgressReportModel.DataItem> = ArrayList()
    private var mAdapter: ProgressReportAdapter? = null

    var iProgressReportPresenter: IProgressReportPresenter? = null
    var token: String? = null
    var selectedUserID: String? = null
    var userRole: String? = null
    var preferencesManager: PreferencesManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        skillID = intent.getStringExtra("skill_id")
        selectedDate = intent.getStringExtra("selected_date")

        PreferencesManager.initializeInstance(context)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        selectedUserID =
            preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)

        userRole = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE)


        setRecyclerView()
        setListeners()
        iProgressReportPresenter = ProgressReportImplementer(this)



            if (userRole.equals("MasterUser")) {

                iProgressReportPresenter?.getReportSummary(token, skillID, selectedDate)

            } else {

                iProgressReportPresenter?.getReportSummaryWithID(
                    token,
                    selectedUserID,
                    skillID,
                    selectedDate
                )

            }

    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setRecyclerView() {
        mAdapter = ProgressReportAdapter(
            context,
            list,
            object : ProgressReportAdapter.ProgressReportAdapterInterface {
                override fun onItemSelected(position: Int, model: ProgressReportModel.DataItem) {

                }

            })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = mAdapter

    }


    override fun reportSummarySuccess(model: ProgressReportModel?) {
        binding.swipeRefresh.isRefreshing = false
        if (model?.data?.learning.isNullOrEmpty()) {
        } else {
            list.addAll(model?.data?.learning!!)

        }
        list.addAll(model?.data?.training!!)
        mAdapter?.updateAdapter(list)

        if (list.size > 0) {
            binding.txtName.text = list[0].name
        } else {
            binding.llNoData.visibility = View.VISIBLE

        }
    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(context)
    }

    override fun removeWait() {
        binding.swipeRefresh.isRefreshing = false

        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        binding.swipeRefresh.isRefreshing = false
        Toast.makeText(context, appErrorMessage, Toast.LENGTH_LONG).show()
        binding.recyclerView.visibility = View.GONE
        binding.llNoData.visibility = View.VISIBLE
    }
}