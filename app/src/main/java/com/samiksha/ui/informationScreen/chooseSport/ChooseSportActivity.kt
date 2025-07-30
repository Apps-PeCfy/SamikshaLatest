package com.samiksha.ui.informationScreen.chooseSport

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivityChooseSportBinding
import com.samiksha.ui.informationScreen.IQuestionPresenter
import com.samiksha.ui.informationScreen.IQuetionView
import com.samiksha.ui.informationScreen.QuestionImplementer
import com.samiksha.ui.informationScreen.chooseProfession.ChooseProfessionActivity
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.ui.informationScreen.welcomeScreen.WelComeActivity
import com.samiksha.utils.GridSpacingItemDecoration
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import java.util.*

class ChooseSportActivity : AppCompatActivity(), IQuetionView,
    ChoseSportAdapter.IClickListenerChooseSport {

  /*  @JvmField
    @BindView(R.id.recycler_sport)
    var recycler_sport: RecyclerView? = null*/

    var choseSportAdapter: ChoseSportAdapter? = null
    var iQuestionPresenter: IQuestionPresenter? = null
    var selectedSportID = -1

    var userMobileNo: String? = null
    var loginType: String? = null
    var userFirstName: String? = null
    var userLastName: String? = null
    var userEmail: String? = null
    var userState: String? = null
    var userGender: String? = null
    var userOTP: String? = null
    var userCurrentState: String? = null
    var userUserCountryCOde: String? = null
    var isBackButton: String? = ""
    var sessionManager: SessionManager? = null


    private var mPreferencesManager: PreferencesManager? = null


    private val questionList: ArrayList<QuestionResponsePOJO.OptionsItem> =
        ArrayList<QuestionResponsePOJO.OptionsItem>()

    private val questionList1: ArrayList<QuestionResponsePOJO.QuestionsItem> =
        ArrayList<QuestionResponsePOJO.QuestionsItem>()
    lateinit var binding: ActivityChooseSportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_choose_sport)
       // ButterKnife.bind(this)
        binding = ActivityChooseSportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initView()
        val redString = getResources().getString(R.string.any_one)
        binding.txtChooseOne.setText(Html.fromHtml(redString))

    }

    private fun initView() {
        PreferencesManager.initializeInstance(this@ChooseSportActivity)
        mPreferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(this@ChooseSportActivity)

        setListners()
        loginType = intent.getStringExtra("LoginType")
        userMobileNo = intent.getStringExtra("userMobileNo")
        userFirstName = intent.getStringExtra("userFirstName")
        userLastName = intent.getStringExtra("userLastName")
        userEmail = intent.getStringExtra("userEmail")
        userState = intent.getStringExtra("userState")
        userGender = intent.getStringExtra("userGender")
        userOTP = intent.getStringExtra("userOTP")
        userCurrentState = intent.getStringExtra("userCurrentState")
        userUserCountryCOde = intent.getStringExtra("userUserCountryCOde")
        isBackButton = intent.getStringExtra("isBackButton")


        if(isBackButton.equals("NotVisible")){
            binding.btnBack.visibility = View.GONE
        }else{
            binding.btnBack.visibility = View.VISIBLE

        }
        val spacing = 25 // 50px

        val spanCount = 7// 3 columns
        val layoutManager = GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false)
        binding.recyclerSport!!.setLayoutManager(layoutManager)
        choseSportAdapter = ChoseSportAdapter(this@ChooseSportActivity, questionList)

        binding.recyclerSport!!.setAdapter(choseSportAdapter)
        choseSportAdapter!!.setClickListener(this)



        iQuestionPresenter = QuestionImplementer(this)
            iQuestionPresenter!!.question()




    }

   /* @OnClick(R.id.btnNext)
    fun btnNext() {

        if (selectedSportID > 0) {
            val intent = Intent(applicationContext, ChooseProfessionActivity::class.java)
            intent.putExtra("ChooseProfessionList", questionList1)


            intent.putExtra("LoginType", loginType)
            intent.putExtra("userFirstName", userFirstName)
            intent.putExtra("userLastName", userLastName)
            intent.putExtra("userEmail", userEmail)
            intent.putExtra("userMobileNo", userMobileNo)
            intent.putExtra("userState", userState)
            intent.putExtra("userGender", userGender)
            intent.putExtra("userOTP", userOTP)
            intent.putExtra("userCurrentState", userCurrentState)
            intent.putExtra("userUserCountryCOde", userUserCountryCOde)


            intent.putExtra("selectedSportID", selectedSportID.toString())

            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                this@ChooseSportActivity,
                "Please select any one sport",
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    @OnClick(R.id.btnBack)
    fun btnBack() {

        val intent = Intent(applicationContext, WelComeActivity::class.java)
        intent.putExtra("LoginType", loginType)
        intent.putExtra("userFirstName", userFirstName)
        intent.putExtra("userLastName", userLastName)
        intent.putExtra("userEmail", userEmail)
        intent.putExtra("userMobileNo", userMobileNo)
        intent.putExtra("userState", userState)
        intent.putExtra("userGender", userGender)
        intent.putExtra("userOTP", userOTP)
        intent.putExtra("userCurrentState", userCurrentState)
        intent.putExtra("userUserCountryCOde", userUserCountryCOde)

        startActivity(intent)
        finish()


    }
*/

    private fun setListners() {
        binding.btnNext.setOnClickListener(View.OnClickListener {
            if (selectedSportID > 0) {
                val intent = Intent(applicationContext, ChooseProfessionActivity::class.java)
                intent.putExtra("ChooseProfessionList", questionList1)


                intent.putExtra("LoginType", loginType)
                intent.putExtra("userFirstName", userFirstName)
                intent.putExtra("userLastName", userLastName)
                intent.putExtra("userEmail", userEmail)
                intent.putExtra("userMobileNo", userMobileNo)
                intent.putExtra("userState", userState)
                intent.putExtra("userGender", userGender)
                intent.putExtra("userOTP", userOTP)
                intent.putExtra("userCurrentState", userCurrentState)
                intent.putExtra("userUserCountryCOde", userUserCountryCOde)


                intent.putExtra("selectedSportID", selectedSportID.toString())

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@ChooseSportActivity,
                    "Please select any one sport",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        binding.btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, WelComeActivity::class.java)
            intent.putExtra("LoginType", loginType)
            intent.putExtra("userFirstName", userFirstName)
            intent.putExtra("userLastName", userLastName)
            intent.putExtra("userEmail", userEmail)
            intent.putExtra("userMobileNo", userMobileNo)
            intent.putExtra("userState", userState)
            intent.putExtra("userGender", userGender)
            intent.putExtra("userOTP", userOTP)
            intent.putExtra("userCurrentState", userCurrentState)
            intent.putExtra("userUserCountryCOde", userUserCountryCOde)

            startActivity(intent)
            finish()

        })
    }

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }


    override fun onQuestionSuccess(questionResponsePOJO: QuestionResponsePOJO?) {


        for (i in questionResponsePOJO!!.questions!!.indices) {
            questionList1.add(questionResponsePOJO.questions?.get(i)!!)
        }




        binding.tvMAinHeading.setText(questionResponsePOJO!!.questions?.get(0)!!.name)

        for (i in questionResponsePOJO!!.questions?.get(0)!!.options!!.indices) {
            questionList.add(questionResponsePOJO.questions?.get(0)!!.options?.get(i)!!)
        }

        choseSportAdapter!!.notifyDataSetChanged()


    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(this@ChooseSportActivity)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(this@ChooseSportActivity, appErrorMessage, Toast.LENGTH_LONG).show()
    }

    override fun selectedSport(position: Int) {
        selectedSportID = position
    }
}

