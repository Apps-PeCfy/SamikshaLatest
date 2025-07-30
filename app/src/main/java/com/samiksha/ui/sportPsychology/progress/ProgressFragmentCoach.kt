package com.samiksha.ui.sportPsychology.progress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.samiksha.R
import com.samiksha.commonMethod.UpdateProfile
import com.samiksha.databinding.FragmentProgressBinding
import com.samiksha.ui.home.IProgressView
import com.samiksha.ui.home.main.ProgressAdapter
import com.samiksha.ui.home.main.TrainingCalenderAdapter
import com.samiksha.ui.home.main.TrainingCalenderModel
import com.samiksha.ui.home.main.TrainingPathModel
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.progressReport.ProgressReportActivity
import com.samiksha.ui.progressReport.progressreportweekly.ProgressReportWeekly
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.*
import com.samiksha.utils.OnSwipeTouchListener
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgressFragmentCoach : Fragment(), AdapterView.OnItemSelectedListener, IProgressView {
    lateinit var binding: FragmentProgressBinding
    private var mContext: Context? = null
    private var list: ArrayList<TrainingPathModel.DataItem> = ArrayList()

    val calendar = Calendar.getInstance()
    var todayDate: String? = null
    var startDateForAPI: String? = null
    var DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    var DATE_PATTERN = "yyyy-MM-dd"
    var DATE_FULL_PATTERN = "MM dd, YYYY"
    var selectedDate: String? = ""


    private var calenderList: ArrayList<TrainingCalenderModel> = ArrayList()
    private var calenderAdapter: TrainingCalenderAdapter? = null


    private var mAdapter: ProgressAdapter? = null


    var iProgressPresenter: IProgressPresenterCoach? = null
    var token: String? = null
    var userID: String? = null
    var userName: String? = null
    var preferencesManager: PreferencesManager? = null
    private var sessionManager: SessionManager? = null
    private var updateProfile: UpdateProfile? = null

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    var registerDate: Date? = null


    // PAGINATION VARS
    private val PAGE_START = 1

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private var isLoading = false

    // If current page is the last page (Pagination will stop after this page load)
    private var isLastPage = false

    // indicates the current page which Pagination is fetching.
    private var currentPage = PAGE_START


    var monthList = arrayListOf<String>()
    var globalPosition = 0
    var isPrevSelected = false
    var selectedFirstDate: String? = null
    var mCalendar1 = Calendar.getInstance()


     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentProgressBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        init()
        return binding.root
    }

    private fun init() {
        mContext = context
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(requireContext())

        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        userID = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)
        userName = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_SELECTED_USER_NAME)
        binding.txtUserName.visibility = View.VISIBLE
        binding.txtUserName!!.text = userName
        Log.d("StrUserNAme",userName!!)

        iProgressPresenter = ProgressImplementerCoach(this)


        if ((sessionManager!!.getUserModel()!!.registerDate).isNullOrEmpty()) {
            iProgressPresenter!!.updateRegisterDate(token)
        } else {
            remainingFragCall()
        }
    }


    private fun remainingFragCall() {
        registerDate =
            inputFormat.parse(preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_PLAYER_REGISTER_DATE))
        monthList.clear()
        collectMonthList()
        Collections.reverse(monthList)

        val spinnerLuggage: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireActivity(), R.layout.spinner_month, monthList as List<Any?>)
        spinnerLuggage.setDropDownViewResource(R.layout.spinner_month)
        binding.spnMonth?.adapter = spinnerLuggage
        binding.spnMonth?.setSelection(globalPosition)
        binding.spnMonth?.onItemSelectedListener = this







        setRecyclerView()
        setListeners()


        calendar.time = Date()
        todayDate = ProjectUtils.dateToRequiredFormat(calendar.time, "dd MMM yyyy")
        calenderList = getCurrentWeek(calendar)
        calenderAdapter?.updateAdapter(calenderList)


    }

    private fun collectMonthList() {
        val formatter = SimpleDateFormat("MMMM yyyy")
        val startCalendar: Calendar = GregorianCalendar()
        startCalendar.time = registerDate
        val endCalendar: Calendar = GregorianCalendar()
        endCalendar.time = endCalendar.getTime()

        val diffMonth: Int = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        val diffYear = endCalendar[Calendar.YEAR] - startCalendar[Calendar.YEAR]
        if (diffYear < 1) {
            Calendar.getInstance().let { calendar ->
                calendar.add(Calendar.MONTH, -(diffMonth))
                for (i in 0 until (diffMonth + 1)) {
                    monthList.add(formatter.format(calendar.timeInMillis))
                    calendar.add(Calendar.MONTH, 1)
                }
            }
        } else {
            Calendar.getInstance().let { calendar ->
                calendar.add(Calendar.MONTH, -11)
                for (i in 0 until 12) {
                    monthList.add(formatter.format(calendar.timeInMillis))
                    calendar.add(Calendar.MONTH, 1)
                }
            }

        }
    }


    private fun setListeners() {


        binding.txtPrev?.setOnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                calendar.add(Calendar.DATE, -7)
                calenderList = getCurrentWeek(calendar)
                calenderAdapter?.updateAdapter(calenderList)

                binding.recyclerView.visibility = View.GONE
                binding.txtLabel.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE

                selectedDate = ""
                previousButtonVisibleFun()

                if (calendar.time.before(Date())) {
                    binding.txtNext?.visibility = View.VISIBLE
                } else {
                    binding.txtNext?.visibility = View.GONE
                }

                spinnerUpdate(mCalendar1)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        }

        binding.txtNext?.setOnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {
                binding.txtPrev?.visibility = View.VISIBLE
                calendar.add(Calendar.DATE, +7)
                calenderList = getCurrentWeek(calendar)
                calenderAdapter?.updateAdapter(calenderList)

                selectedDate = ""


                if (calendar.time.before(Date())) {
                    binding.txtNext?.visibility = View.VISIBLE
                } else {
                    resetAPI()
                    binding.txtNext?.visibility = View.GONE
                }

                spinnerUpdate(mCalendar1)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        }

        binding.llTrainingPath?.setOnTouchListener(object : OnSwipeTouchListener(mContext) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                if (calendar.time.before(Date())) {
                    calendar.add(Calendar.DATE, +7)
                    calenderList = getCurrentWeek(calendar)
                    calenderAdapter?.updateAdapter(calenderList)
                } else {
                    Toast.makeText(
                        mContext,
                        "No Schedule available for future dates",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                calendar.add(Calendar.DATE, -7)
                calenderList = getCurrentWeek(calendar)
                calenderAdapter?.updateAdapter(calenderList)
            }

            override fun onSwipeUp() {
                super.onSwipeUp()

            }

            override fun onSwipeDown() {
                super.onSwipeDown()
            }
        })
    }

    private fun previousButtonVisibleFun() {
        //  var mStrings: ArrayList<String> = ArrayList<String>()
        var mStrings: java.util.ArrayList<Date> = java.util.ArrayList<Date>()
        for (i in calenderList.indices) {
            //   val createdDate: String = SimpleDateFormat("yyyy-MM-dd").format(calenderList.get(i).date)
            //    val createdDate: String = SimpleDateFormat("yyyy-MM-dd").format(calenderList.get(i).date)
            mStrings.add(calenderList.get(i).date!!)
        }


        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = outputFormat.format(registerDate)
        if (registerDate!!.after(mStrings.get(0)) || mStrings.get(0).equals(registerDate)) {

            binding.txtPrev?.visibility = View.GONE
        } else {
            binding.txtPrev?.visibility = View.VISIBLE
        }

        if (globalPosition == 0) {
            binding.txtNext?.visibility = View.GONE
        } else {
            binding.txtNext?.visibility = View.VISIBLE
        }

        if (globalPosition == 11) {
            for (i in calenderList.indices) {
                if (calenderList.get(i).dayDate.equals("01")) {
                    binding.txtPrev?.visibility = View.GONE
                    break
                }
            }

        }

    }

    private fun spinnerUpdate(mCalendar1: Calendar) {
        for (k in monthList!!.indices) {

            if ((ProjectUtils.dateToRequiredFormat(
                    mCalendar1.time,
                    "MMMM yyyy"
                )).equals(monthList.get(k))
            ) {
                globalPosition = k
                binding.spnMonth?.setSelection(globalPosition)

            }
        }


        if (monthList.contains(ProjectUtils.dateToRequiredFormat(mCalendar1.time, "MMMM yyyy"))) {
            isPrevSelected = true

            monthList.clear()
            collectMonthList()
            Collections.reverse(monthList)

            val spinnerLuggage: ArrayAdapter<*> =
                ArrayAdapter<Any?>(
                    requireActivity(),
                    R.layout.spinner_month,
                    monthList as List<Any?>
                )
            spinnerLuggage.setDropDownViewResource(R.layout.spinner_month)
            binding.spnMonth?.adapter = spinnerLuggage
            binding.spnMonth?.setSelection(globalPosition)
            binding.spnMonth?.onItemSelectedListener = this

        } else {
            isPrevSelected = false

        }

    }


    //MONDAY - SUNDAY
    private fun getCurrentWeek(mCalendar: Calendar): ArrayList<TrainingCalenderModel> {
        mCalendar1 = mCalendar
        binding.txtMonth?.text = ProjectUtils.dateToRequiredFormat(mCalendar.time, "MMMM yyyy")


        // 1 = Sunday, 2 = Monday, etc.
        val day_of_week = mCalendar[Calendar.DAY_OF_WEEK]
        val monday_offset: Int
        monday_offset = if (day_of_week == 1) {
            -6
        } else 2 - day_of_week // need to minus back
        mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset)
        val mDateMonday = mCalendar.time
        startDateForAPI = ProjectUtils.dateToRequiredFormat(mDateMonday, "yyyy-MM-dd")
        //  resetAPI()

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday = mCalendar.time

        //Get format date
        val sdf = SimpleDateFormat(DATE_FORMAT_PATTERN)
        val MONDAY = sdf.format(mDateMonday)
        val SUNDAY = sdf.format(mDateSunday)
        return getDates(MONDAY, SUNDAY)
    }

    private fun getDates(
        dateString1: String,
        dateString2: String
    ): ArrayList<TrainingCalenderModel> {
        var dates: ArrayList<TrainingCalenderModel> = ArrayList()
        var df1: DateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)

        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var cal1 = Calendar.getInstance()
        cal1.time = date1

        var cal2 = Calendar.getInstance()
        cal2.time = date2


        if (globalPosition == 0) {
            while (!cal1.after(cal2)) {
                var model = TrainingCalenderModel()
                model.date = cal1.time
                model.day = ProjectUtils.dateToRequiredFormat(cal1.time, "EEE")
                model.dayDate = ProjectUtils.dateToRequiredFormat(cal1.time, "dd")
                model.month = ProjectUtils.dateToRequiredFormat(cal1.time, "MMM")
                model.monthYear = ProjectUtils.dateToRequiredFormat(cal1.time, "MMMM yyyy")
                model.selected =
                    todayDate == ProjectUtils.dateToRequiredFormat(cal1.time, "dd MMM yyyy")
                cal1.add(Calendar.DATE, 1)
                dates.add(model)
            }
        } else {

            while (!cal1.after(cal2)) {
                var model = TrainingCalenderModel()
                model.date = cal1.time
                model.day = ProjectUtils.dateToRequiredFormat(cal1.time, "EEE")
                model.dayDate = ProjectUtils.dateToRequiredFormat(cal1.time, "dd")
                model.month = ProjectUtils.dateToRequiredFormat(cal1.time, "MMM")
                model.monthYear = ProjectUtils.dateToRequiredFormat(cal1.time, "MMMM yyyy")
                model.selected =
                    selectedFirstDate == ProjectUtils.dateToRequiredFormat(cal1.time, "dd MMM yyyy")
                cal1.add(Calendar.DATE, 1)
                dates.add(model)
            }
        }
        return dates
    }


    private fun changeColorLogic(model: TrainingCalenderModel) {
        var nDate: Date? = null
        for (answerModel in calenderList) {
            if (!model.date?.after(Date())!!) {
                val newDate = Date(registerDate!!.getTime() - (1 * 24 * 60 * 60 * 1000))
                nDate = Date(registerDate!!.getTime() - (1 * 24 * 60 * 60 * 1000))
                if (!model.date?.before(newDate)!!) {
                    answerModel.selected = model.date == answerModel.date
                }

            }
        }

        if (nDate != null) {
            if (!model.date?.before(nDate)!!) {
                val format = SimpleDateFormat("yyyy-MM-dd")
                selectedDate = format.format(Date.parse(model.dayDate + " " + model.monthYear))
                iProgressPresenter?.progressSkillList(token, userID, selectedDate)
            }
        }

        calenderAdapter?.updateAdapter(calenderList)
    }


    private fun setRecyclerView() {
        val newDate = Date(registerDate!!.getTime() - (1 * 24 * 60 * 60 * 1000))


        calenderAdapter = TrainingCalenderAdapter(
            mContext,
            calenderList, newDate,
            object : TrainingCalenderAdapter.TrainingCalenderAdapterInterface {
                override fun onItemSelected(position: Int, model: TrainingCalenderModel) {
                    if (ProjectUtils.checkInternetAvailable(context)!!) {
                        changeColorLogic(model)
                    } else {
                        ProjectUtils.showToast(
                            context,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }
            })



        binding.recyclerViewDates?.layoutManager = GridLayoutManager(mContext, 7)
        binding.recyclerViewDates?.adapter = calenderAdapter





        mAdapter =
            ProgressAdapter(mContext, list, object : ProgressAdapter.ProgressAdapterInterface {
                override fun onItemSelected(
                    position: Int,
                    model: TrainingPathModel.DataItem,
                    s: String
                ) {
                    if (ProjectUtils.checkInternetAvailable(context)!!) {

                        if (s.equals("img")) {


                            val intent = Intent(context, ProgressReportWeekly::class.java)
                            intent.putExtra("calenderList", calenderList)
                            intent.putExtra("token", token)
                            intent.putExtra("mentalSkillID", model.id.toString())
                            intent.putExtra(
                                "FromDate",
                                ProjectUtils.dateToRequiredFormat(
                                    calenderList[0].date,
                                    "yyyy-MM-dd"
                                )
                            )
                            intent.putExtra(
                                "toDate",
                                ProjectUtils.dateToRequiredFormat(
                                    calenderList[6].date,
                                    "yyyy-MM-dd"
                                )
                            )
                            intent.putExtra("UserID", userID)

                            startActivity(intent)


                            Log.d(
                                "Datessdsdsd",
                                "F" + ProjectUtils.dateToRequiredFormat(
                                    calenderList[0].date,
                                    "yyyy-MM-dd"
                                )
                                        + "  " + "L" + ProjectUtils.dateToRequiredFormat(
                                    calenderList[6].date,
                                    "yyyy-MM-dd"
                                )
                                        + "  " + model.id.toString()
                            )
                        } else {
                            val intent = Intent(context, ProgressReportActivity::class.java)
                            intent.putExtra("skill_id", model.id.toString())
                            intent.putExtra("selected_date", selectedDate)
                            startActivity(intent)

                        }
                    } else {
                        ProjectUtils.showToast(
                            context,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }
            })

        var layoutManager = LinearLayoutManager(mContext)

        binding.swipeRefresh.setOnRefreshListener {
            resetAPI()
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = mAdapter

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@ProgressFragmentCoach.isLoading = true
                currentPage++
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return 99
            }

            override fun isLastPage(): Boolean {
                return this@ProgressFragmentCoach.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@ProgressFragmentCoach.isLoading
            }
        })

        loadNextPage()
    }

    /**
     * Pagination
     **/

    private fun resetAPI(): Unit {
        // To clear already set adapter and new list on swipe refresh

        mAdapter!!.clear()
        currentPage = PAGE_START
        isLastPage = false
        iProgressPresenter?.progressSkillList(token, userID, "")

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


    private fun loadNextPage() {

        binding.swipeRefresh.isRefreshing = true
        iProgressPresenter?.progressSkillList(token, userID, "")

    }

    override fun progressMentalSkillListSuccess(model: TrainingPathModel?) {
        ProjectUtils.dismissProgressDialog()
        binding.swipeRefresh.isRefreshing = false

        if (currentPage == PAGE_START) {
            mAdapter!!.clear()
        }
        mAdapter!!.removeLoadingFooter()
        isLoading = false

        list = model?.progress_report_list?.data!!


        // if list<10 then last page true
        if (list.size < 50) {
            isLastPage = true
        }
        if (list.size > 0) {
            mAdapter!!.addAll(list)
        }
        if (list.size > 0 || currentPage != PAGE_START) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtLabel.visibility = View.VISIBLE
            binding.llNoData.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.txtLabel.visibility = View.GONE
            binding.llNoData.visibility = View.VISIBLE
        }


        // Add loading footer if last page is false
        if (!isLastPage) {
            mAdapter!!.addLoadingFooter()
        }
    }

    override fun getProfileSuccess(body: CheckOtpResponsePOJO?) {
        preferencesManager!!.createLoginSession(body!!.user)
        SessionManager.getInstance(requireContext()).setUserModel(body!!.user!!)
        remainingFragCall()
    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(activity)
    }

    override fun removeWait() {
        binding.swipeRefresh.isRefreshing = false

        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        binding.swipeRefresh.isRefreshing = false
        Toast.makeText(activity, appErrorMessage, Toast.LENGTH_LONG).show()
        binding.recyclerView.visibility = View.GONE
        binding.txtLabel.visibility = View.GONE
        binding.llNoData.visibility = View.VISIBLE
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        globalPosition = position

        if (isPrevSelected == true) {
            isPrevSelected = false
        } else {

            if (globalPosition > 0) {
                calendar.time = Date()
                calendar.add(Calendar.MONTH, -position)
                calendar.set(Calendar.DATE, 1)

                val firstDateOfPreviousMonth: Date = calendar.getTime()
                Log.d("wesdsdwdssdwasww", firstDateOfPreviousMonth.toString())


                val outputFormat = SimpleDateFormat("yyyy-MM-dd")
                val outputFormat1 = SimpleDateFormat("dd")
                val outputFormat2 = SimpleDateFormat("MMMM yyyy")
                val formattedDate = outputFormat.format(registerDate)
                val fSingleDate = outputFormat1.format(registerDate)
                val registerMMYY = outputFormat2.format(registerDate)
                val selectedMMYY = outputFormat2.format(firstDateOfPreviousMonth)


                if (firstDateOfPreviousMonth.after(registerDate) || firstDateOfPreviousMonth.equals(
                        registerDate
                    )
                ) {

                    calendar.set(Calendar.DATE, 1)
                    callCalender()
                    previousButtonVisibleFun()

                } else {

                    if (registerMMYY.equals(selectedMMYY)) {
                        calendar.set(Calendar.DATE, fSingleDate.toInt())
                        callCalender()
                        previousButtonVisibleFun()
                    }
                }

            } else {

                calendar.time = Date()
                todayDate = ProjectUtils.dateToRequiredFormat(calendar.time, "dd MMM yyyy")
                calenderList = getCurrentWeek(calendar)
                calenderAdapter?.updateAdapter(calenderList)
                previousButtonVisibleFun()
            }
        }


    }

   private fun callCalender() {
        selectedFirstDate = ProjectUtils.dateToRequiredFormat(calendar.time, "dd MMM yyyy")

        val day_of_week = calendar[Calendar.DAY_OF_WEEK]
        val monday_offset: Int
        monday_offset = if (day_of_week == 1) {
            -6
        } else 2 - day_of_week // need to minus back
        calendar.add(Calendar.DAY_OF_YEAR, monday_offset)
        val mDateMonday = calendar.time
        startDateForAPI = ProjectUtils.dateToRequiredFormat(mDateMonday, "yyyy-MM-dd")
        //  resetAPI()

        // return 6 the next days of current day (object cal save current day)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday = calendar.time

        //Get format date
        val sdf = SimpleDateFormat(DATE_FORMAT_PATTERN)
        val MONDAY = sdf.format(mDateMonday)
        val SUNDAY = sdf.format(mDateSunday)

        getDates(MONDAY, SUNDAY)
        calenderList = getCurrentWeek(calendar)
        calenderAdapter?.updateAdapter(calenderList)


    }

}