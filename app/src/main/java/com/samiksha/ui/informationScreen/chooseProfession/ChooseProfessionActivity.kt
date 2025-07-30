package com.samiksha.ui.informationScreen.chooseProfession

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivityChooseProfessionBinding
import com.samiksha.ui.informationScreen.chooseGoals.ChooseGoalsActivity
import com.samiksha.ui.informationScreen.chooseSport.ChooseSportActivity
import com.samiksha.ui.informationScreen.pojo.QuestionResponsePOJO
import com.samiksha.utils.GridSpacingItemDecoration
import com.samiksha.utils.ProjectUtils
import java.util.ArrayList

class ChooseProfessionActivity : AppCompatActivity(),
    ChooseProfessionAdapter.IClickListenerChooseProfession {
    private var chooseProfessionList = ArrayList<QuestionResponsePOJO.QuestionsItem>()
    var chooseProfessionAdapter: ChooseProfessionAdapter? = null
    var selectedProfessionID = -1
    var selectedSportId: String? = null
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


/*
    @JvmField
    @BindView(R.id.recycler_profession)
    var recycler_profession: RecyclerView? = null*/

    lateinit var binding: ActivityChooseProfessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_choose_profession)
        //ButterKnife.bind(this)
        binding = ActivityChooseProfessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initView()
        val redString = getResources().getString(R.string.any_one)
        binding.txtChooseOne!!.setText(Html.fromHtml(redString))




    }

    private fun initView() {

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

        setListners()

        selectedSportId = intent.getStringExtra("selectedSportID")
        chooseProfessionList = intent.getSerializableExtra("ChooseProfessionList")
                as ArrayList<QuestionResponsePOJO.QuestionsItem>
        binding.tvHeading.setText(chooseProfessionList!![1].name)


        binding.recyclerProfession.layoutManager = LinearLayoutManager(this@ChooseProfessionActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerProfession.itemAnimator = DefaultItemAnimator()



        chooseProfessionAdapter = ChooseProfessionAdapter(this@ChooseProfessionActivity,chooseProfessionList.get(1).options)
        val spanCount = 2 // 3 columns
        binding.recyclerProfession!!.setLayoutManager(GridLayoutManager(this@ChooseProfessionActivity, spanCount))
        val spacing = 20 // 50px

        val includeEdge = true
        binding.recyclerProfession!!.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerProfession!!.setAdapter(chooseProfessionAdapter)
        
        chooseProfessionAdapter!!.setClickListener(this)



    }

  /*  @OnClick(R.id.btnBack)
    fun btnBack() {

        val intent = Intent(applicationContext, ChooseSportActivity::class.java)
        intent.putExtra("ChooseProfessionList", chooseProfessionList)


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


        intent.putExtra("selectedSportID", selectedSportId)

        startActivity(intent)
        finish()


    }*/
 /*@OnClick(R.id.btnNext)
    fun btnNext() {

     if (selectedProfessionID > 0) {

         val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)
         intent.putExtra("GoalsList", chooseProfessionList)
         intent.putExtra("LoginType", loginType)
         intent.putExtra("userFirstName", userFirstName)
         intent.putExtra("userLastName", userLastName)
         intent.putExtra("userEmail", userEmail)
         intent.putExtra("userMobileNo", userMobileNo)
         intent.putExtra("userState", userState)
         intent.putExtra("userGender", userGender)
         intent.putExtra("userOTP", userOTP)
         intent.putExtra("userCurrentState", userCurrentState)
         intent.putExtra("professionID", selectedProfessionID.toString())
         intent.putExtra("selectedSportId", selectedSportId.toString())
         intent.putExtra("userUserCountryCOde", userUserCountryCOde)

         startActivity(intent)
         finish()
     }else {
         Toast.makeText(
             this@ChooseProfessionActivity,
             "Please select any one professional level",
             Toast.LENGTH_SHORT
         ).show()
     }


    }
*/

    private fun setListners() {
        binding.btnBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ChooseSportActivity::class.java)
            intent.putExtra("ChooseProfessionList", chooseProfessionList)
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
            intent.putExtra("selectedSportID", selectedSportId)
            startActivity(intent)
            finish()
        })
        binding.btnNext.setOnClickListener(View.OnClickListener {
            if (selectedProfessionID > 0) {

                val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)
                intent.putExtra("GoalsList", chooseProfessionList)
                intent.putExtra("LoginType", loginType)
                intent.putExtra("userFirstName", userFirstName)
                intent.putExtra("userLastName", userLastName)
                intent.putExtra("userEmail", userEmail)
                intent.putExtra("userMobileNo", userMobileNo)
                intent.putExtra("userState", userState)
                intent.putExtra("userGender", userGender)
                intent.putExtra("userOTP", userOTP)
                intent.putExtra("userCurrentState", userCurrentState)
                intent.putExtra("professionID", selectedProfessionID.toString())
                intent.putExtra("selectedSportId", selectedSportId.toString())
                intent.putExtra("userUserCountryCOde", userUserCountryCOde)

                startActivity(intent)
                finish()
            }else {
                Toast.makeText(
                    this@ChooseProfessionActivity,
                    "Please select any one professional level",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun selectedSport(position: Int) {
        selectedProfessionID = position
    }
}
