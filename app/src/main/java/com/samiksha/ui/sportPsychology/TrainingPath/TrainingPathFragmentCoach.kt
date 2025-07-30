package com.samiksha.ui.sportPsychology.TrainingPath

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samiksha.R
import com.samiksha.commonMethod.UpdateProfile
import com.samiksha.ui.home.ITrainingPathView
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.*
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TrainingPathFragmentCoach : Fragment(), AdapterView.OnItemSelectedListener,
    ITrainingPathView {
    private var mView: View? = null
    private var mContext: Context? = null
    private var list: ArrayList<TrainingPathModel.DataItem> = ArrayList()
    private var calenderList: ArrayList<TrainingCalenderModel> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var recyclerViewDates: RecyclerView? = null
    private var txtLabel: TextView? = null
    private var txtMonth: TextView? = null
    private var txtNoData: TextView? = null
    private var llNoData: LinearLayout? = null
    private var llMain: LinearLayout? = null
    private var txtPrev: TextView? = null
    private var txtUserName: TextView? = null
    private var txtNext: TextView? = null
    private var imgViewInfo: ImageView? = null
    private var mAdapter: TrainingPathAdapter? = null
    private var calenderAdapter: TrainingCalenderAdapter? = null
    private var mTrainingReportAdapter: TrainingPathReportAdapter? = null

    var iTrainingPathPresenter: TrainingPathImplementerCoach? = null
    var token: String? = null
    var userID: String? = null
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


    val calendar = Calendar.getInstance()
    var todayDate: String? = null
    var selectedDayes: String? = ""
    var startDateForAPI: String? = null
    var DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    var DATE_PATTERN = "yyyy-MM-dd"
    var DATE_FULL_PATTERN = "MM dd, YYYY"


    private var spnMonth: Spinner? = null
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
        setHasOptionsMenu(true)
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_training_path, container, false)
            initFragment()
        }

        return mView
    }

    private fun initFragment() {
        mContext = context
        recyclerView = mView?.findViewById(R.id.recycler_view)
        recyclerViewDates = mView?.findViewById(R.id.recycler_view_dates)
        txtNoData = mView?.findViewById(R.id.txt_no_data)
        txtMonth = mView?.findViewById(R.id.txt_month)
        llNoData = mView?.findViewById(R.id.ll_no_data)
        llMain = mView?.findViewById(R.id.ll_main)
        txtPrev = mView?.findViewById(R.id.txt_prev)
        txtNext = mView?.findViewById(R.id.txt_next)
        txtLabel = mView?.findViewById(R.id.txt_label)
        spnMonth = mView?.findViewById(R.id.spnMonth)
        imgViewInfo = mView?.findViewById(R.id.imgViewInfo)
        txtUserName = mView?.findViewById(R.id.txtUserName)

        imgViewInfo!!.visibility = View.GONE
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(requireContext())
        updateProfile = UpdateProfile.getInstance()
        txtUserName!!.visibility = View.VISIBLE
        txtUserName!!.text = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_SELECTED_USER_NAME)
        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        iTrainingPathPresenter = TrainingPathImplementerCoach(this)


        if((sessionManager!!.getUserModel()!!.registerDate).isNullOrEmpty()){
            iTrainingPathPresenter!!.updateRegisterDate(token)
        }else{
            remainingFragCall()
        }


    }

    private fun remainingFragCall() {
        registerDate = inputFormat.parse(preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_PLAYER_REGISTER_DATE))
        // registerDate = inputFormat.parse("2022-02-08T05:56:42.000000Z")


        userID = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_ID_SELECTED_POSITION)




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
        spnMonth?.adapter = spinnerLuggage
        spnMonth?.setSelection(globalPosition)
        spnMonth?.onItemSelectedListener = this




        calendar.time = Date()

        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        selectedDayes = sdf.format(d)


        setRecyclerView()
        setListeners()

        /*  todayDate = ProjectUtils.dateToRequiredFormat(calendar.time, "dd MMM yyyy")
          calenderList = getCurrentWeek(calendar)
          calenderAdapter?.updateAdapter(calenderList)
  */

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
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


    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        txtPrev?.setOnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                calendar.add(Calendar.DATE, -7)
            calenderList = getCurrentWeek(calendar)
            calenderAdapter?.updateAdapter(calenderList)

                previousButtonVisibleFun()
            if (calendar.time.before(Date())) {
                txtNext?.visibility = View.VISIBLE
            } else {
                txtNext?.visibility = View.GONE
            }

            isPrevSelected = true
            spinnerUpdate(mCalendar1)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        }




        txtNext?.setOnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {
                txtPrev?.visibility = View.VISIBLE
                calendar.add(Calendar.DATE, +7)
            calenderList = getCurrentWeek(calendar)
            calenderAdapter?.updateAdapter(calenderList)

            if (calendar.time.before(Date())) {
                txtNext?.visibility = View.VISIBLE
            } else {
                txtNext?.visibility = View.GONE
            }

            isPrevSelected = true
            spinnerUpdate(mCalendar1)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        }





        llMain?.setOnTouchListener(object : OnSwipeTouchListener(mContext) {
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
        var mStrings: ArrayList<Date> = ArrayList<Date>()
        for (i in calenderList.indices) {
            //   val createdDate: String = SimpleDateFormat("yyyy-MM-dd").format(calenderList.get(i).date)
            val createdDate: String =
                SimpleDateFormat("yyyy-MM-dd").format(calenderList.get(i).date)
            mStrings.add(calenderList.get(i).date!!)
        }


        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = outputFormat.format(registerDate)
        if (registerDate!!.after(mStrings.get(0)) || mStrings.get(0).equals(registerDate)) {

            txtPrev?.visibility = View.GONE
        } else {
            txtPrev?.visibility = View.VISIBLE
        }

        if(globalPosition==0){
            txtNext?.visibility = View.GONE
        }else{
            txtNext?.visibility = View.VISIBLE
        }

        if (globalPosition == 11) {
            for (i in calenderList.indices) {
                if (calenderList.get(i).dayDate.equals("01")) {
                    txtPrev?.visibility = View.GONE
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
                spnMonth?.setSelection(globalPosition)

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
            spnMonth?.adapter = spinnerLuggage
            spnMonth?.setSelection(globalPosition)
            spnMonth?.onItemSelectedListener = this


        } else {
            isPrevSelected = false

        }


    }

    private fun setRecyclerView() {

        val newDate = Date(registerDate!!.getTime() - (1*24*60*60*1000))


        calenderAdapter = TrainingCalenderAdapter(
            mContext,
            calenderList,newDate,
            object : TrainingCalenderAdapter.TrainingCalenderAdapterInterface {
                override fun onItemSelected(position: Int, model: TrainingCalenderModel) {

                    if (ProjectUtils.checkInternetAvailable(context)!!) {

                        changeColorLogic(model)
                    val sdf = SimpleDateFormat("EEEE")
                    selectedDayes = sdf.format(model.date)


                    mAdapter!!.updateAdapter(selectedDayes, list)

                    } else {
                        ProjectUtils.showToast(
                            context,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }
            })



        recyclerViewDates?.layoutManager = GridLayoutManager(mContext, 7)
        recyclerViewDates?.adapter = calenderAdapter

        mAdapter = TrainingPathAdapter(
            selectedDayes,
            mContext,
            list,
            object : TrainingPathAdapter.TrainingPathAdapterInterface {
                override fun onItemSelected(position: Int, model: TrainingPathModel.DataItem) {
                    // iTrainingPathPresenter?.skillDetails(token, model.id.toString())
                }

                override fun onRepeatClick(position: Int, model: TrainingPathModel.DataItem) {
                    //  showAlertDialog(context?.resources?.getString(R.string.msg_repeat_schedule)!!, model)
                }

                override fun onDeleteClick(position: Int, model: TrainingPathModel.DataItem) {
                    //  showAlertDialog(context?.resources?.getString(R.string.msg_delete_schedule)!!, model)
                }

                override fun onReportClick(position: Int, model: TrainingPathModel.DataItem) {
                    if (ProjectUtils.checkInternetAvailable(context)!!) {
                        showReport(model, position)
                    } else {
                        ProjectUtils.showToast(
                            context,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }

            })

        var layoutManager = LinearLayoutManager(mContext)

        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = mAdapter

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@TrainingPathFragmentCoach.isLoading = true
                currentPage++
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return 99
            }

            override fun isLastPage(): Boolean {
                return this@TrainingPathFragmentCoach.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@TrainingPathFragmentCoach.isLoading
            }
        })

        //  loadNextPage()
    }


    private fun showReport(model: TrainingPathModel.DataItem, position: Int) {


        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        val customLayout: View? =
            getLayoutInflater().inflate(R.layout.custom_report_view, null)
        alertDialog.setView(customLayout)

        val txtInfo: TextView = customLayout!!.findViewById(R.id.txtInfo)
        val tvDescription: TextView = customLayout!!.findViewById(R.id.tvDescription)
        val imgCloseViewInfo: ImageView = customLayout!!.findViewById(R.id.imgCloseViewInfo)
        val recycler_view: RecyclerView = customLayout!!.findViewById(R.id.recycler_view)

        calR(recycler_view, model, position)


        val reward = (model.days).size * 10

        txtInfo.text = reward.toString()

        //   txtInfo.text = " "+(reward)+" "

        tvDescription.text =
            "If you train yourself for 7 consecutive days, you will earn 200 reward points"
        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(true)


        llMain!!.alpha = 0.1f


        alert.show()
        imgCloseViewInfo.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()
        })

        alert.setOnCancelListener {
            llMain!!.alpha = 1.0f
        }

        alert.setOnDismissListener {
            llMain!!.alpha = 1.0f

        }

    }

    private fun calR(
        recycler_view: RecyclerView,
        model: TrainingPathModel.DataItem,
        position: Int
    ) {


        mTrainingReportAdapter = TrainingPathReportAdapter(
            selectedDayes,
            mContext,
            list, position
        )

        var layoutManager = LinearLayoutManager(mContext)

        recycler_view?.layoutManager = layoutManager
        recycler_view?.adapter = mTrainingReportAdapter


    }

    private fun changeColorLogic(model: TrainingCalenderModel) {
        for (answerModel in calenderList) {
            if (!model.date?.after(Date())!!) {
                val newDate = Date(registerDate!!.getTime() - (1*24*60*60*1000))

                if(!model.date?.before(newDate)!!){
                    answerModel.selected = model.date == answerModel.date
                }
            }
        }
        calenderAdapter?.updateAdapter(calenderList)
    }


    //MONDAY - SUNDAY
    private fun getCurrentWeek(mCalendar: Calendar): ArrayList<TrainingCalenderModel> {

        mCalendar1 = mCalendar

        txtMonth?.text = ProjectUtils.dateToRequiredFormat(mCalendar.time, "MMMM yyyy")
        // 1 = Sunday, 2 = Monday, etc.
        val day_of_week = mCalendar[Calendar.DAY_OF_WEEK]
        val monday_offset: Int
        monday_offset = if (day_of_week == 1) {
            -6
        } else 2 - day_of_week // need to minus back
        mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset)
        val mDateMonday = mCalendar.time
        startDateForAPI = ProjectUtils.dateToRequiredFormat(mDateMonday, "yyyy-MM-dd")
        resetAPI()

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6)
        val mDateSunday = mCalendar.time

        //Get format date
        val sdf =
            SimpleDateFormat(DATE_FORMAT_PATTERN)
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
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
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


    /**
     * Pagination
     **/

    private fun resetAPI(): Unit {

        // To clear already set adapter and new list on swipe refresh
        mAdapter!!.clear()
        currentPage = PAGE_START
        isLastPage = false
        iTrainingPathPresenter?.trainingSkillList(token, userID, startDateForAPI)

    }

    private fun loadNextPage() {
        iTrainingPathPresenter?.trainingSkillList(token, userID, startDateForAPI)

    }

    private fun showAlertDialog(msg: String, model: TrainingPathModel.DataItem) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()

            if (msg.equals(
                    context?.resources?.getString(R.string.msg_delete_schedule)!!,
                    ignoreCase = true
                )
            ) {
                iTrainingPathPresenter?.deleteSchedule(token, model.schedule_id.toString())
            } else {
                iTrainingPathPresenter?.repeatSchedule(token, model.schedule_id.toString())
            }

        }
        alertDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun saveSkillDetailInSession(skillDetailsResponsePOJO: SkillDetailsResponsePOJO) {
        if (skillDetailsResponsePOJO.data?.subSkiils != null && skillDetailsResponsePOJO?.data?.subSkiils?.size!! > 0) {
            var model: SkillDetailModel = SkillDetailModel()
            model.skillDetail = skillDetailsResponsePOJO

            if (skillDetailsResponsePOJO.data?.subSkiils!![0].data!!.isNotEmpty()) {
                model.learningFirstQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(0)
                model.learningVideoModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(1)
                model.learningSecondQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(2)
                model.learningThirdQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(3)
            }

            if (skillDetailsResponsePOJO.data?.subSkiils?.size!! > 1 && skillDetailsResponsePOJO.data?.subSkiils!![1].data!!.isNotEmpty() && skillDetailsResponsePOJO.data?.subSkiils!![1].data?.size!! > 2) {
                model.trainingAudioModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(0)
                model.trainingQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(1)
                model.trainingSecondQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(2)
            }

            model.isFromTrainingPath = true

            SessionManager.getInstance(requireContext()).setSkillDetailModel(model)

            val intent = Intent(activity, TrainingStartActivity::class.java)
            startActivity(intent)

        }

    }

    override fun trainingMentalSkillListSuccess(model: TrainingPathModel?) {
        ProjectUtils.dismissProgressDialog()

        if (currentPage == PAGE_START) {
            mAdapter!!.clear()
        }
        mAdapter!!.removeLoadingFooter()
        isLoading = false

        list = model?.mentalSkills?.data!!


        // if list<10 then last page true
        if (list.size < 50) {
            isLastPage = true
        }
        if (list.size > 0) {
            mAdapter!!.addAll(list)
        }
        if (list.size > 0 || currentPage != PAGE_START) {
            recyclerView?.visibility = View.VISIBLE
            txtLabel?.visibility = View.VISIBLE
            llNoData?.visibility = View.GONE
        } else {
            recyclerView?.visibility = View.GONE
            txtLabel?.visibility = View.GONE
            llNoData?.visibility = View.VISIBLE
        }


        // Add loading footer if last page is false
        if (!isLastPage) {
            mAdapter!!.addLoadingFooter()
        }


    }

    override fun deleteScheduleSuccess(model: TrainingPathModel?) {
        ProjectUtils.dismissProgressDialog()
        iTrainingPathPresenter?.trainingSkillList(token, userID, startDateForAPI)

    }

    override fun repeatScheduleSuccess(model: TrainingPathModel?) {
        ProjectUtils.dismissProgressDialog()
        iTrainingPathPresenter?.trainingSkillList(token, userID, startDateForAPI)

    }

    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        saveSkillDetailInSession(skillDetailsResponsePOJO!!)
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
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(activity, appErrorMessage, Toast.LENGTH_LONG).show()
        recyclerView?.visibility = View.GONE
        txtLabel?.visibility = View.GONE
        llNoData?.visibility = View.VISIBLE
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