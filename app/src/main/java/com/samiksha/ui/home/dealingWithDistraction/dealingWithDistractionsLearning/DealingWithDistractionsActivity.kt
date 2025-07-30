package com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.ActivityDealingWithDistractionsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.subscription.SubscriptionProcessACtivity
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionPresenter
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionView
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionImplementer
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.infoAfterLearning.Learning.BenefitsAdapter
import com.samiksha.ui.infoAfterLearning.SportInformation.SportInformation
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.ui.register.pojo.StateResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DealingWithDistractionsActivity : AppCompatActivity(), IDealingDistractionView,
    PaymentResultWithDataListener {
    lateinit var binding: ActivityDealingWithDistractionsBinding
    private var context: Context = this
    private var list: ArrayList<SubscriptionModel.Subscriptions> = ArrayList()
    private var subscriptionsAdapter: SubscriptionsAdapter? = null

    var iDealingDistractionPresenter: IDealingDistractionPresenter? = null
    var skill_id: String? = null
    var subscriptionID: String? = null

    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var skillDetailModel: SkillDetailsResponsePOJO? = null
    var sessionManager: SessionManager? = null

    private var benefitsAdapter: BenefitsAdapter? = null
    private var listbenifits: List<String> = ArrayList()


    var selectedStateID: Int? = 9999
    var stateName: String? = ""
    var paidAmount: String? = "0"
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()
    private var onlyStateList: ArrayList<String>? = ArrayList()
    var edtState : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealingWithDistractionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {

        binding.toolbar.setNavigationOnClickListener {
            if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()


            } else {
            ProjectUtils.showToast(
                this@DealingWithDistractionsActivity,
                getString(R.string.no_internet_connection)
            )

        }

        }
        supportActionBar?.hide()

        skill_id = intent.getStringExtra("skill_id")

        PreferencesManager.initializeInstance(context)
        preferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(context = context)
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)



        iDealingDistractionPresenter = dealingWithDistractionImplementer(this)
        iDealingDistractionPresenter!!.skillDetails(token, skill_id)
        iDealingDistractionPresenter!!.state("IN")

        if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {

        setListeners()
        setRecyclerView()
        } else {
            ProjectUtils.showToast(
                this@DealingWithDistractionsActivity,
                getString(R.string.no_internet_connection)
            )

        }
    }


    override fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?) {

        onlyStateList!!.clear()

        for (i in stateResponcePOJO!!.data!!.indices) {
            onlyStateList!!.add(stateResponcePOJO!!.data!![i].name!!)
        }

        stateList = stateResponcePOJO!!.data!!

        if (intent.getStringExtra("otpuserFirstName") != null) {

            selectedStateID = (intent.getIntExtra("otpuserCurrentState", 0))
            edtState?.setText(stateList[(intent.getIntExtra("otpuserCurrentState", 0)) - 1].name)


        }


    }


    override fun onBackPressed() {
        if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {

            val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
        } else {
            ProjectUtils.showToast(
                this@DealingWithDistractionsActivity,
                getString(R.string.no_internet_connection)
            )

        }
    }

    private fun setRecyclerView() {
        subscriptionsAdapter = SubscriptionsAdapter(
            context,
            list,
            object : SubscriptionsAdapter.SubscriptionsAdapterInterface {
                override fun onItemSelected(
                    position: Int,
                    model: SubscriptionModel.Subscriptions
                ) {
                    if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {

                        preferencesManager!!.setStringValue(
                        Constants.SHARED_PREFERENCE_LOGIN_USER_SUBSCRIPTION_POSITION,
                        position.toString()
                    )
                    val intent = Intent(
                        this@DealingWithDistractionsActivity,
                        SubscriptionProcessACtivity::class.java
                    )
                    intent.putExtra("OnCallBack", "DealingWithDistractions")
                    intent.putExtra("skill_id", skill_id)
                    startActivity(intent)


                } else {
                    ProjectUtils.showToast(
                        this@DealingWithDistractionsActivity,
                        getString(R.string.no_internet_connection)
                    )

                }


                }

            })
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = subscriptionsAdapter
    }

    private fun showAlertState(model: SubscriptionModel.Subscriptions) {

        val alertDialog =
            androidx.appcompat.app.AlertDialog.Builder(this@DealingWithDistractionsActivity)
        val customLayout: android.view.View? =
            getLayoutInflater().inflate(R.layout.custom_state_dailog, null)
        alertDialog.setView(customLayout)

        val btnYes: Button = customLayout!!.findViewById(R.id.btnYes)
        val btnNo: Button = customLayout!!.findViewById(R.id.btnNo)
         edtState = customLayout.findViewById(R.id.edtState)


        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(false)
        alert.show()

        edtState?.setOnClickListener(View.OnClickListener {
            stateDialog(edtState!!)

        })

        btnYes.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()


        })

        btnNo.setOnClickListener(View.OnClickListener {


            val progressDialogLogout = ProgressDialog(this)
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog


            ApiClient.client.updateUserState("Bearer $token", selectedStateID)!!
                .enqueue(object :
                    Callback<OnlyMessageResponsePOJO?> {
                    override fun onResponse(
                        call: Call<OnlyMessageResponsePOJO?>,
                        response: Response<OnlyMessageResponsePOJO?>
                    ) {
                        progressDialogLogout.dismiss()
                        if (response.code() == 200) {

                            alert.dismiss()
                            alert.cancel()
                            preferencesManager!!.setStringValue(
                                Constants.SHARED_PREFERENCE_LOGIN_STATE, stateName
                            )

                            subscriptionID = model.id.toString()
                            changeColorLogic(model)


                        } else if (response.code() == 422) {
                            val gson = GsonBuilder().create()
                            var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                            try {
                                pojo = gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ValidationResponsePOJO::class.java
                                )
                                Toast.makeText(
                                    this@DealingWithDistractionsActivity,
                                    pojo.errors!!.get(0).message,
                                    Toast.LENGTH_LONG
                                ).show()


                            } catch (exception: IOException) {
                            }

                        } else {
                            Toast.makeText(
                                this@DealingWithDistractionsActivity,
                                "Internal Server Error",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onFailure(
                        call: Call<OnlyMessageResponsePOJO?>, t: Throwable
                    ) {
                        progressDialogLogout.dismiss()
                        Toast.makeText(
                            this@DealingWithDistractionsActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
                })


        })

    }


    private fun stateDialog(edtState: EditText) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose any State")


        val dataAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, onlyStateList!!)
        builder.setAdapter(dataAdapter)
        { dialog, which ->
            selectedStateID = stateList[which].id
            stateName = stateList[which].name
            edtState.setText(stateList[which!!].name)


        }
        val dialog = builder.create()
        dialog.show()


    }


    private fun changeColorLogic(model: SubscriptionModel.Subscriptions) {
        for (answerModel in list) {
            answerModel.selected = model.id == answerModel.id
        }
        subscriptionsAdapter?.updateAdapter(list)

        startPayment(model)


    }


    private fun addReadMore(addMoretext: String?, textView: TextView) {

        if (addMoretext!!.length > 130) {
            val ss = SpannableString(addMoretext!!.substring(0, 130) + "...Read more")
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    addReadLess(addMoretext, textView)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ds.setColor(getResources().getColor(R.color.login_button, getTheme()))
                    } else {
                        ds.setColor(getResources().getColor(R.color.login_button))
                    }
                }
            }
            ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.setText(ss)
            textView.setMovementMethod(LinkMovementMethod.getInstance())
        } else {
            textView.setText(addMoretext)

        }
    }

    private fun addReadLess(text: String, textView: TextView) {
        val ss = SpannableString("$text Read less")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadMore(text, textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(getResources().getColor(R.color.login_button, getTheme()))
                } else {
                    ds.setColor(getResources().getColor(R.color.login_button))
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(ss)
        textView.setMovementMethod(LinkMovementMethod.getInstance())
    }


    private fun setData() {
        binding.apply {
            saveSkillDetailInSession(skillDetailModel!!)

            // txtDescription.text = skillDetailModel?.data?.description

            if (skillDetailModel?.data?.description.isNullOrEmpty()) {
                addReadMore("No description", txtDescription)

            } else {
                addReadMore(skillDetailModel?.data?.description, txtDescription)

            }

            if (skillDetailModel!!.data!!.benefits.size > 0) {
                listbenifits =
                    skillDetailModel!!.data!!.benefits

                binding.txtBenefits.visibility = View.VISIBLE
                binding.recyclerviewbenifits.visibility = View.VISIBLE


                benefitsAdapter =
                    BenefitsAdapter(
                        context,
                        listbenifits,
                        object : BenefitsAdapter.BenefitsAdapterInterface {
                            override fun onItemSelected(position: Int, name: String) {
                            }
                        })
                binding.recyclerviewbenifits.layoutManager = LinearLayoutManager(context)
                binding.recyclerviewbenifits.adapter = benefitsAdapter
            } else {
                binding.txtBenefits.visibility = View.GONE
                binding.recyclerviewbenifits.visibility = View.GONE

            }



            if (skillDetailModel?.data?.type.equals("Free", ignoreCase = true)) {
                txtType.text = skillDetailModel?.data?.type

            } else {
                txtType.text = "Subscription required"

            }
            txtTopicTitle.text = skillDetailModel?.data?.name

            if (!skillDetailModel?.data?.type.equals("Free", ignoreCase = true)) {
                if (skillDetailModel?.data?.isSubscription!! || skillDetailModel?.data?.isAccessible!!) {
                    checkLearningCompleteLogic()
                    llSubscription.visibility = View.GONE
                    txtType.text = "Included in your subscription"
                    imgType.setImageResource(R.drawable.ic_subscribed)
                    binding.imgTrainingNext.setImageResource(R.drawable.right_arrow_black)
                    binding.imgLearningNext.setImageResource(R.drawable.right_arrow_black)

                } else {
                    rlLearning.alpha = 0.3F
                    rlTraining.alpha = 0.3F
                    txtLearningLabel.text = "Please subscribe to access this module."
                    txtTrainingLabel.text = "Please subscribe to access this training."
                    llSubscription.visibility = View.VISIBLE
                    iDealingDistractionPresenter!!.subscriptionList(token)


                    imgType.setImageResource(R.drawable.lock)
                    binding.imgTrainingNext.setImageResource(R.drawable.lock)
                    binding.imgLearningNext.setImageResource(R.drawable.lock)

                }
            } else {
                llSubscription.visibility = View.GONE
                imgType.setImageResource(R.drawable.ic_free)
                binding.imgTrainingNext.setImageResource(R.drawable.right_arrow_black)
                binding.imgLearningNext.setImageResource(R.drawable.right_arrow_black)

                checkLearningCompleteLogic()
            }


            val learningDuration =
                sessionManager?.getSkillDetailModel()?.learningVideoModel?.duration?.split(" ")
                    ?.toTypedArray()
            val trainingDuration =
                sessionManager?.getSkillDetailModel()?.trainingAudioModel?.duration?.split(" ")
                    ?.toTypedArray()



            if(learningDuration?.size==2){
                binding.txtLearningDuration.text = learningDuration?.get(0)
                binding.txtLearningDurationUnit.text = learningDuration?.get(1)
            }

            if(trainingDuration?.size==2){
                binding.txtTrainingDuration.text = trainingDuration?.get(0)
                binding.txtTrainingDurationUnit.text = trainingDuration?.get(1)

            }


            Glide.with(this@DealingWithDistractionsActivity)
                .load(skillDetailModel!!.data!!.image)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform()
                ).into(imgTop)
        }
    }

    private fun checkLearningCompleteLogic() {

        if (skillDetailModel!!.data!!.moduleType.equals("Yoga")) {
            binding.txtTrainingLabel.text = "You can visit your training modules any time you want."

        } else {
            if (!skillDetailModel?.data?.isLearningComplete!!) {
                binding.rlTraining.alpha = 0.3F
                binding.imgLearning.visibility = View.GONE
                binding.llLearningDuration.visibility = View.VISIBLE
                binding.txtLearningLabel.text =
                    "Finish this learning module to start your training exercise"
                binding.txtTrainingLabel.text =
                    "Finish the learning module to start your training exercise"
            } else {
                binding.imgLearning.visibility = View.VISIBLE
                binding.llLearningDuration.visibility = View.GONE
                binding.txtLearningLabel.text =
                    "You have finished this module. You can revisit it if you want."
                binding.txtTrainingLabel.text =
                    "You can visit your training modules any time you want."
            }
        }

    }

    private fun setListeners() {
        binding.apply {
            rlLearning.setOnClickListener {
                if (skillDetailModel != null && skillDetailModel?.data?.type.equals(
                        "Free",
                        ignoreCase = true
                    )
                ) {
                    startLearning()
                } else {
                    if (skillDetailModel?.data?.isSubscription!! || skillDetailModel?.data?.isAccessible!!) {
                        startLearning()
                    } else {
                        Toast.makeText(context, "Need subscription", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            rlTraining.setOnClickListener {
                if (skillDetailModel != null && skillDetailModel?.data?.type.equals(
                        "Free",
                        ignoreCase = true
                    )
                ) {
                    if (sessionManager?.getSkillDetailModel()?.trainingAudioModel != null) {
                        startTraining()
                    } else {
                        Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (skillDetailModel?.data?.isSubscription!! || skillDetailModel?.data?.isAccessible!!) {
                        startTraining()
                    } else {
                        Toast.makeText(context, "Need subscription", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun startTraining() {
        if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {

            if (skillDetailModel?.data!!.moduleType.equals("Yoga")) {
            val intent = Intent(applicationContext, TrainingStartActivity::class.java)
            startActivity(intent)

        } else {
            if (skillDetailModel?.data?.isLearningComplete!!) {
                val intent = Intent(applicationContext, TrainingStartActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please complete learning first", Toast.LENGTH_SHORT).show()
            }
        }

        } else {
            ProjectUtils.showToast(
                this@DealingWithDistractionsActivity,
                getString(R.string.no_internet_connection)
            )

        }

    }

    private fun startLearning() {

        if (ProjectUtils.checkInternetAvailable(this@DealingWithDistractionsActivity)!!) {

            if (sessionManager?.getSkillDetailModel()?.learningFirstQuestionModel != null) {
            val intent = Intent(applicationContext, SportInformation::class.java)
            intent.putExtra(
                "data",
                Gson().toJson(sessionManager?.getSkillDetailModel()?.learningFirstQuestionModel)
            )
            startActivity(intent)
        } else {
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show()
        }

        } else {
            ProjectUtils.showToast(
                this@DealingWithDistractionsActivity,
                getString(R.string.no_internet_connection)
            )

        }

    }


    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        skillDetailModel = skillDetailsResponsePOJO
        var model: SkillDetailModel = SkillDetailModel()


        if (skillDetailModel!!.data!!.moduleType.equals("Yoga")) {
            binding.rlLearning.visibility = View.GONE
            binding.rlTraining.visibility = View.VISIBLE
            binding.tvSubText.text = "Choose your subscription type to get access to meditation"


        } else {
            binding.rlLearning.visibility = View.VISIBLE
            binding.rlTraining.visibility = View.VISIBLE
            binding.tvSubText.text = "Choose your subscription type to get access to mental skills"


        }
        if (skillDetailModel != null) {
            setData()
        }

    }

    override fun onSubscriptionList(subscriptionModel: SubscriptionModel?) {
        list = subscriptionModel?.subscriptions!!
        SessionManager.getInstance(this).setUserSubscription(subscriptionModel)

        subscriptionsAdapter?.updateAdapter(list)
    }

    override fun createOrderSuccess(model: OrderModel?) {

    }

    override fun updateSubscription(subscriptionModel: SubscriptionModel?) {
        iDealingDistractionPresenter!!.skillDetails(token, skill_id)
        

    }


    private fun saveSkillDetailInSession(skillDetailsResponsePOJO: SkillDetailsResponsePOJO) {
        if (skillDetailsResponsePOJO.data?.subSkiils != null && skillDetailsResponsePOJO.data?.subSkiils?.size!! > 0) {
            var model: SkillDetailModel = SkillDetailModel()
            model.skillDetail = skillDetailsResponsePOJO
            if (skillDetailsResponsePOJO.data?.subSkiils!![0].data!!.isNotEmpty() && skillDetailsResponsePOJO.data?.subSkiils!![0].data?.size!! > 3) {
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
            if (skillDetailsResponsePOJO!!.data!!.isTrainingComplete == true) {
                model.isFromTrainingPath = true
            } else {
                model.isFromTrainingPath = false

            }




            sessionManager?.setSkillDetailModel(model)

        }

    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(context)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(context, appErrorMessage, Toast.LENGTH_LONG)
            .show()
    }

    /**
     * Razor Pay Payment Gateway Integration
     */

    private fun startPayment(model: SubscriptionModel.Subscriptions) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val activity: Activity = this
        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZOR_PAY_KEY)
        paidAmount = model.sellingPrice
        try {
            val options = JSONObject()
            options.put("name", SessionManager.getInstance(context).getUserModel()?.name)
            options.put("description", "Demoing Charges")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            if (BuildConfig.DEMO_PAYMENT) {
                options.put("amount", "100")
            } else {
                options.put("amount", (model.sellingPrice?.toDouble()?.times(100)).toString())
            }

            val preFill = JSONObject()
            preFill.put("email", "test@razorpay.com")
            preFill.put("contact", SessionManager.getInstance(context).getUserModel()?.phoneNo)
            options.put("prefill", preFill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        try {
            // changeOrderPaymentStatusAPI("2", "",paymentData)
            Toast.makeText(
                this,
                "Payment failed: " + code.toString() + " " + response,
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception in onPaymentError: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        try {
            fireEvent()
            iDealingDistractionPresenter!!.updateSubscriptions(token, subscriptionID)
            //  Toast.makeText(this, "Payment Success: " +  paymentData, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception in onPaymentSuccess: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fireEvent() {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, skillDetailModel?.data?.id.toString())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, skillDetailModel?.data?.name)
        bundle.putString(FirebaseAnalytics.Param.VALUE, paidAmount)

        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle)


    }


}
