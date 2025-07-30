package com.samiksha.ui.sportPsychology.reports

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.ActivityReportsPdfBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.reports.pojo.AssesmentScoreResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ReportsPdfActivity : AppCompatActivity() {
    var reportspdfAdapter: ReportsPdfAdapter? = null

    /*@JvmField
    @BindView(R.id.recyclerviewpdf)
    var recyclerviewpdf: RecyclerView? = null

    @JvmField
    @BindView(R.id.ll_no_report)
    var ll_no_report: LinearLayout? = null



    @JvmField
    @BindView(R.id.toolbarReport)
    var toolbarReport: Toolbar? = null
*/
    var token: String? = null
    var userID: String? = null
    var assesmentID: String? = null
    var preferencesManager: PreferencesManager? = null
    private var getAssesmentScoreList: List<AssesmentScoreResponsePOJO.DataItem>? = ArrayList()

    lateinit var binding: ActivityReportsPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_reports_pdf)
     //   ButterKnife.bind(this)
        binding = ActivityReportsPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarGradiant(this)

        binding.toolbarReport!!.title = "Reports"
        binding.toolbarReport!!.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbarReport)
        binding.toolbarReport!!.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

        PreferencesManager.initializeInstance(this)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        userID =
            preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)
        assesmentID = intent.getStringExtra("AssesmentID")



            val progressDialogLogout = ProgressDialog(this)
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog

        ApiClient.client.getAssesmentScore("Bearer $token", userID, assesmentID)!!
            .enqueue(object :
                Callback<AssesmentScoreResponsePOJO?> {
                override fun onResponse(
                    call: Call<AssesmentScoreResponsePOJO?>,
                    response: Response<AssesmentScoreResponsePOJO?>
                ) {
                    progressDialogLogout.dismiss()
                    if (response.code() == 200) {
                        if (response.body() != null) {

                            getAssesmentScoreList = response!!.body()!!.scores!!.data

                            if(getAssesmentScoreList!!.isNotEmpty())
                            {
                                reportspdfAdapter = ReportsPdfAdapter(
                                    this@ReportsPdfActivity,
                                    getAssesmentScoreList
                                )
                                binding.recyclerviewpdf!!.layoutManager =
                                    GridLayoutManager(this@ReportsPdfActivity, 3)
                                binding.recyclerviewpdf!!.adapter = reportspdfAdapter

                                binding.recyclerviewpdf!!.visibility = View.VISIBLE
                                binding.llNoReport!!.visibility = View.GONE

                            } else{
                                binding.recyclerviewpdf!!.visibility = View.GONE
                                binding.llNoReport!!.visibility = View.VISIBLE

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
                                this@ReportsPdfActivity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            this@ReportsPdfActivity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(
                    call: Call<AssesmentScoreResponsePOJO?>, t: Throwable
                ) {
                    progressDialogLogout.dismiss()
                    Toast.makeText(
                        this@ReportsPdfActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
            })



    }


    private fun setStatusBarGradiant(activity: ReportsPdfActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }

}
