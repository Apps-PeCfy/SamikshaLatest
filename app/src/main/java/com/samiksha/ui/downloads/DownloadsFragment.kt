package com.samiksha.ui.downloads

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.database.LocalCrudRepository
import com.samiksha.databinding.FragmentDownloadsBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.utils.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class DownloadsFragment : Fragment() {
    lateinit var binding: FragmentDownloadsBinding
    private var mContext: Context? = null
    private var mAdapter: DownloadsAdapter? = null
    var list: ArrayList<SkillModel> = ArrayList()
    private var localCrudRepository: LocalCrudRepository? = null

   
    var sessionManager: SessionManager? = null

    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadsBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        mContext = context
        sessionManager = SessionManager.getInstance(mContext!!)
        localCrudRepository = LocalCrudRepository.getInstance(mContext!!)

        PreferencesManager.initializeInstance(mContext!!)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.setNavigationIcon(R.drawable.left_arrow)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               activity?.onBackPressed()
            }
        })
        
        list = localCrudRepository?.getAllSkills(sessionManager?.getUserModel()?.id) as ArrayList<SkillModel>

        setData()
        setRecyclerView()
        setListeners()

    }

    private fun setData() {
        if(ProjectUtils.checkInternetAvailable(context) == true && localCrudRepository?.getAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id)?.isNotEmpty() == true){
            binding.btnSync.visibility = View.VISIBLE
        }else{
            binding.btnSync.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.apply {
            btnSync.setOnClickListener {
                callUploadDataAPI()
            }

            swipeRefresh.setOnRefreshListener {
                refreshPage()
            }
        }

    }



    private fun setRecyclerView() {
        binding.apply {
            mAdapter = DownloadsAdapter(
                mContext,
                list,
                object : DownloadsAdapter.DownloadsAdapterInterface {
                    override fun onItemSelected(position: Int, skillModel: SkillModel, skillDetail : SkillDetailsResponsePOJO.Data) {
                        startActivity(Intent(context, DownloadedTrainingActivity::class.java).putExtra("skill_model", Gson().toJson(skillModel)))
                    }

                    override fun onDeleteClick(position: Int, skillModel: SkillModel, skillDetail : SkillDetailsResponsePOJO.Data) {
                        showAlertDialog(
                            context?.resources?.getString(R.string.msg_delete_downloaded_skill)!!,
                            skillModel, skillDetail
                        )
                    }

                })

            var layoutManager = LinearLayoutManager(mContext)

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mAdapter

            refreshPage()
        }

    }

    private fun showAlertDialog(
        msg: String,
        model: SkillModel,
        skillDetail: SkillDetailsResponsePOJO.Data
    ) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("Yes") { dialog, which ->
            deleteSkillFromDownload(model, skillDetail)
            dialog.dismiss()

        }
        alertDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun deleteSkillFromDownload(model: SkillModel, skillDetail: SkillDetailsResponsePOJO.Data) {
        binding.swipeRefresh.isRefreshing = true
        var filePath: String

        filePath = DownloadUtils.fetchStoragePath(requireContext()) + "${Constants.FOLDER_NAME}/${model.user_id}/" + skillDetail.name + "/" + model.fileName

        val topicFile = File(filePath)
        if (topicFile.exists()){
            topicFile.delete()
        }

        localCrudRepository?.deleteSkillModel(model)
        refreshPage()
    }

    private fun refreshPage() {
        list = localCrudRepository?.getAllSkills(sessionManager?.getUserModel()?.id) as ArrayList<SkillModel>
        mAdapter?.updateAdapter(list)
        if (list.isNullOrEmpty()){
            binding.llNoData.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }else{
            binding.llNoData.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
        setData()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        if(Constants.SHOULD_RELOAD){
            refreshPage()
        }
    }


    /**
     * API CALLING
     */

    private fun callUploadDataAPI() {
        val gson = Gson()
        val jsonString = gson.toJson(localCrudRepository?.getAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id))
        var params : JSONObject = JSONObject()
        params.put("data", JSONArray(jsonString))
        params.put("token", token)



        val progressDialogLogout = ProgressDialog(mContext)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog


        APIManager.getInstance(mContext!!).postAPI(
            BuildConfig.API_URL + "submitAllAnswer",
            params,
            UserResponsePOJO::class.java,
            mContext,
            object : APIManager.APIManagerInterface {
                override fun onSuccess(resultObj: Any?, jsonObject: JSONObject) {
                    progressDialogLogout.dismiss()
                    var model: UserResponsePOJO = resultObj as UserResponsePOJO
                    Toast.makeText(mContext!!, model.message, Toast.LENGTH_SHORT).show()
                    localCrudRepository?.deleteAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id)
                    refreshPage()
                    setData()
                }

                override fun onError(error: String?) {
                    progressDialogLogout.dismiss()
                    if (error != null){
                        Toast.makeText(mContext!!, error, Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }


}