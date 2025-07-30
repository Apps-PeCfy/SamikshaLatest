package com.samiksha.ui.drawer.mytraining

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.samiksha.R
import com.samiksha.databinding.ActivityMyTrainingBinding
import com.samiksha.databinding.FragmentProgressBinding
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.utils.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyTraining : Fragment(),IMyTrainingView {

    lateinit var binding: ActivityMyTrainingBinding

    private var list: ArrayList<MyTrainingModel.DataItem> = ArrayList()
    var mAdapter: MyTrainingAdapter? = null

    var iMyTrainingPresenter: IMyTrainingPresenter? = null

    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    var startDate :String = ""
    var endDate :String = ""

    // PAGINATION VARS
    private val PAGE_START = 1

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private var isLoading = false

    // If current page is the last page (Pagination will stop after this page load)
    private var isLastPage = false

    // indicates the current page which Pagination is fetching.
    private var currentPage = PAGE_START


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val rootView = inflater.inflate(R.layout.activity_my_training, container, false)
        ButterKnife.bind(this, rootView)
        setHasOptionsMenu(true)
        binding = ActivityMyTrainingBinding.inflate(layoutInflater)

        init()
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



    private fun init() {

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "My Training"

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        iMyTrainingPresenter = MyTrainingImplementer(this)
        iMyTrainingPresenter?.getMyActivities(token, startDate, endDate, currentPage)

        setListeners()
        setRecyclerView()
    }


    private fun setListeners() {

        binding.rlStartDate.setOnClickListener {
            showDatePickerDialog(binding.edtStartDate)
        }

        binding.rlEndDate.setOnClickListener {
            showDatePickerDialog(binding.edtEndDate)
        }
    }



    private fun showDatePickerDialog(txtDate: TextView) {
        var mYear = 0
        var mMonth = 0
        var mDay = 0

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->

                var strMonth = ""
                val iMonth = monthOfYear + 1
                strMonth = if (iMonth < 10) {
                    "0$iMonth"
                } else {
                    "" + iMonth
                }

                var strDay = ""
                strDay = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    "" + dayOfMonth
                }
                var dateString = "$year-$strMonth-$strDay"
                if (txtDate.equals(binding.edtStartDate)){
                    startDate = dateString
                }else{
                    endDate = dateString
                }
                txtDate.text = ProjectUtils.changeDateFormat(dateString, "yyyy-MM-dd", "dd MMM yyyy")
                if (validateDate(startDate, endDate)){
                    resetAPI()
                }else if (endDate != ""){
                    Toast.makeText(context, "Please select valid date range", Toast.LENGTH_SHORT).show()
                }
            }, mYear, mMonth, mDay
        )
        //   datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateDate(startDate: String, endDate: String): Boolean {
        var returnVal = false
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val fromDate = dateFormat.parse(startDate)
            val toDate = dateFormat.parse(endDate)

            returnVal = !toDate.before(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return returnVal
    }

    private fun saveSkillDetailInSession(skillDetailsResponsePOJO: SkillDetailsResponsePOJO) {
        if (skillDetailsResponsePOJO.data?.subSkiils != null && skillDetailsResponsePOJO?.data?.subSkiils?.size!! > 0){
            var model : SkillDetailModel = SkillDetailModel()
            model.skillDetail = skillDetailsResponsePOJO
            if (skillDetailsResponsePOJO.data?.subSkiils!![0].data!!.isNotEmpty()){
                model.learningFirstQuestionModel = skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(0)
                model.learningVideoModel = skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(1)
                model.learningSecondQuestionModel = skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(2)
                model.learningThirdQuestionModel = skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(3)
            }

            if (skillDetailsResponsePOJO.data?.subSkiils?.size!! > 1  && skillDetailsResponsePOJO.data?.subSkiils!![1].data!!.isNotEmpty() && skillDetailsResponsePOJO.data?.subSkiils!![1].data?.size!! >2){
                model.trainingAudioModel = skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(0)
                model.trainingQuestionModel = skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(1)
                model.trainingSecondQuestionModel = skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(2)
            }

            SessionManager.getInstance(requireActivity()).setSkillDetailModel(model)

            val intent = Intent(context, TrainingStartActivity::class.java)
            startActivity(intent)

        }

    }

    private fun setRecyclerView() {
        mAdapter = MyTrainingAdapter(context, list, object :MyTrainingAdapter.MyTrainingAdapterInterface{
            override fun onItemSelected(position: Int, model: MyTrainingModel.DataItem) {
                if(model.moduleType.equals("Yoga", ignoreCase = true)){
                    iMyTrainingPresenter?.skillDetails(token, model.id.toString())
                }else{

                    val intent = Intent(context, DealingWithDistractionsActivity::class.java)
                    intent.putExtra("skill_id", model.id.toString())

                    startActivity(intent)
                }
            }

            override fun onFavoriteClick(position: Int, model: MyTrainingModel.DataItem) {

            }

        })
        binding.recyclerView.adapter = mAdapter
        var layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.swipeRefresh.setOnRefreshListener { resetAPI() }

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@MyTraining.isLoading = true
                currentPage++
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return 99
            }

            override fun isLastPage(): Boolean {
                return this@MyTraining.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@MyTraining.isLoading
            }
        })

        loadNextPage()
    }

    /**
     * Pagination
     **/


    private  fun resetAPI(): Unit {
        // To clear already set adapter and new list on swipe refresh
        binding.swipeRefresh.isRefreshing = true
        mAdapter!!.clear()
        currentPage = PAGE_START
        isLastPage = false
        iMyTrainingPresenter?.getMyActivities(token, startDate, endDate, currentPage)

    }

    private fun loadNextPage() {
        iMyTrainingPresenter?.getMyActivities(token, startDate, endDate, currentPage)

    }


    override fun getMyActivitiesSuccess(model: MyTrainingModel?) {
        if (currentPage == PAGE_START) {
            mAdapter!!.clear()
        }
        mAdapter!!.removeLoadingFooter()
        isLoading = false

        list = model?.my_activity_list?.data!!


        // if list<10 then last page true
        if (list.size < 50) {
            isLastPage = true
        }
        if (list.size > 0) {
            mAdapter!!.addAll(list)
        }
        if (list.size > 0 || currentPage != PAGE_START) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.llNoData.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }


        // Add loading footer if last page is false
        if (!isLastPage) {
            mAdapter!!.addLoadingFooter()
        }
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        saveSkillDetailInSession(skillDetailsResponsePOJO!!)
    }

    override fun showWait() {
    }

    override fun removeWait() {
        binding.swipeRefresh.isRefreshing = false

    }

    override fun onFailure(appErrorMessage: String?) {
        binding.swipeRefresh.isRefreshing = false
        binding.recyclerView.visibility = View.GONE
        binding.llNoData.visibility = View.VISIBLE


    }


}