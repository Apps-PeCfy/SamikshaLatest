package com.samiksha.ui.drawer.subscription

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.commonMethod.UpdateProfile
import com.samiksha.databinding.FragmentSubscriptionProcessBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionPresenter
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionView
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionImplementer
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.main.HomeActivity
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

class SubscriptionProcessACtivity : AppCompatActivity(), IDealingDistractionView,
    PaymentResultWithDataListener {


    var iDealingDistractionPresenter: IDealingDistractionPresenter? = null

    var sessionManager: SessionManager? = null
    private var onlyStateList: ArrayList<String>? = ArrayList()
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()
    var selectedStateID: Int? = 9999
    var selectedposition: String? = "0"
    var CopuanName: String? = ""
    var CopuanAmt: Int? = 0
    var CopuanID: Int? = 8888
    var razorPayamt: Double? = 0.0
    var orderModel: OrderModel? = null

    private var subscriptionsAdapter: SubscriptionsAdapter? = null


  /*  @JvmField
    @BindView(R.id.tvPremium)
    var tvPremium: TextView? = null

    @JvmField
    @BindView(R.id.btnProceed)
    var btnProceed: Button? = null

    @JvmField
    @BindView(R.id.tvSubscriptiontxt)
    var tvSubscriptiontxt: TextView? = null

    @JvmField
    @BindView(R.id.tvSellingPrice)
    var tvSellingPrice: TextView? = null

    @JvmField
    @BindView(R.id.tvState)
    var tvState: TextView? = null

    @JvmField
    @BindView(R.id.tvAmtCoupan)
    var tvAmtCoupan: TextView? = null

    @JvmField
    @BindView(R.id.stateCard)
    var stateCard: CardView? = null

    @JvmField
    @BindView(R.id.cardDetails)
    var cardDetails: LinearLayout? = null
*/

    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var isActive = false
    private var mList: ArrayList<SubscriptionModel.Subscriptions> = ArrayList()
    private lateinit var selectedmList: SubscriptionModel.Subscriptions
    var subscriptionID: String? = null
    var onBackCall: String? = ""
    var skill_id: String? = null

    lateinit var binding: FragmentSubscriptionProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.fragment_subscription_process)
      //  ButterKnife.bind(this)

        binding = FragmentSubscriptionProcessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarGradiant(this)
        skill_id = intent.getStringExtra("skill_id")
        CopuanName = intent.getStringExtra("CopuanName")
        CopuanAmt = intent.getIntExtra("CopuanAmt", 0)
        CopuanID = intent.getIntExtra("CopuanID", 0)
        if (CopuanName != null) {
            binding.tvApplyCoupan.text = CopuanName
            binding.tvAmtCoupan!!.text = "- ₹ " + CopuanAmt.toString()

        } else {
            CopuanName = ""
            Log.d("CoupanNameCopuanName", CopuanName!!)

        }

        if (CopuanAmt == 0) {
            CopuanAmt = 0
        } else {
            CopuanAmt = intent.getIntExtra("CopuanAmt", 0)

        }

        if (CopuanName.isNullOrEmpty()) {
            binding.tvAmtCoupan!!.visibility = View.GONE
            binding.tvCoupan!!.visibility = View.GONE
        } else {
            binding.tvAmtCoupan!!.visibility = View.VISIBLE
            binding.tvCoupan!!.visibility = View.VISIBLE

        }

        sessionManager = SessionManager.getInstance(this)
        iDealingDistractionPresenter =
            dealingWithDistractionImplementer(this@SubscriptionProcessACtivity)
        iDealingDistractionPresenter!!.state("IN")
        if (intent.getStringExtra("OnCallBack") != null) {
            onBackCall = intent.getStringExtra("OnCallBack")
        }


        init()
        dataSet()

    }

    private fun setStatusBarGradiant(activity: SubscriptionProcessACtivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }


    private fun dataSet() {
        binding.tvPremium!!.text =
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].name
        binding.tvSubscriptiontxt!!.text =
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].description
        binding.tvSellingPrice!!.text =
            "₹ " + sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice

        subscriptionID =
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].id.toString()




        if ((preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_USER_STATE))!!.isNotEmpty()) {

            binding.cardDetails!!.visibility = View.VISIBLE

            if ((preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_USER_STATE)).equals(
                    "Maharashtra"
                )
            ) {

                binding.tvState!!.text =
                    preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_USER_STATE)
                val amount: Double =
                    sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()
                val res = amount / 100.0f * 9
                binding.tvAmtIGST.text = "₹ " + res
                binding.tvAmtSGST.text = "₹ " + res
                binding.tvTotalPurchase.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        res * 2
                    )
                binding.totalPayable.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        CopuanAmt!!.toDouble()
                    ) + "0"

                binding.tvIGST.visibility = View.VISIBLE
                binding.tvAmtIGST.visibility = View.VISIBLE
                binding.tvSGST.visibility = View.VISIBLE
                binding.tvAmtSGST.visibility = View.VISIBLE
                binding.tvGSTandTaxes.visibility = View.GONE
                binding.tvAmtGSTandTaxes.visibility = View.GONE


            } else {

                binding.tvState!!.text =
                    preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_USER_STATE)

                val amount: Double =
                    sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()
                val res = amount / 100.0f * 18
                binding.tvAmtGSTandTaxes.text = "₹ " + res
                binding.tvTotalPurchase.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        res
                    )
                binding.totalPayable.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        CopuanAmt!!.toDouble()
                    ) + "0"

                binding.tvIGST.visibility = View.GONE
                binding.tvAmtIGST.visibility = View.GONE
                binding.tvSGST.visibility = View.GONE
                binding.tvAmtSGST.visibility = View.GONE
                binding.tvGSTandTaxes.visibility = View.VISIBLE
                binding.tvAmtGSTandTaxes.visibility = View.VISIBLE
            }


        } else {
            binding.cardDetails!!.visibility = View.GONE
        }


    }

    private fun init() {

        val toolbar: Toolbar = this.findViewById(R.id.toolbar_home)
        toolbar.title = "Buy Plan"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar!!.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                preferencesManager?.setStringValue(Constants.SHARED_PREFERENCE_USER_STATE, "")

                if (onBackCall.equals("DealingWithDistractions")) {
                    val intent = Intent(
                        this@SubscriptionProcessACtivity,
                        DealingWithDistractionsActivity::class.java
                    )
                    intent.putExtra("skill_id", skill_id)
                    startActivity(intent)
                    finish()

                } else {
                    val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
                    intent!!.action = "tvSubscription"
                    startActivity(intent)

                }

            }
        })


        PreferencesManager.initializeInstance(this@SubscriptionProcessACtivity)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        selectedposition =
            preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_SUBSCRIPTION_POSITION)
        mList = sessionManager!!.getUserSubscription()!!.subscriptions


        if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {
            binding.tvState!!.visibility = ViewGroup.VISIBLE
            binding.stateCard!!.visibility = ViewGroup.VISIBLE
        } else {
            binding.tvState!!.visibility = ViewGroup.GONE
            binding.stateCard!!.visibility = ViewGroup.GONE
            binding.cardDetails!!.visibility = View.GONE
        }

        setListners()

    }

    private fun setListners() {
        binding.tvState.setOnClickListener(View.OnClickListener {
            stateDialog()
        })
        binding.tvState.setOnClickListener(View.OnClickListener {
            for (i in mList.indices) {
                if (mList[i].is_active) {
                    isActive = true
                }
            }


            if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {

                if (isActive) {
                    Toast.makeText(this, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
                    //   Toast.makeText(this, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
                    //  UpdateProfile.showToast(this)
                } else {
                    if (binding.tvState.text.isEmpty()) {
                        Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT)
                            .show()

                    } else {

                        var selectedState = stateList.find { it.name == binding.tvState.text }
                        Log.d("wssdxdddcSelect", selectedState!!.id.toString())

                        callUpdateStateAPI(selectedState!!.id)

                    }

                }
            } else {
                selectedmList = mList!!.get(selectedposition!!.toInt())
                callCreateOrderAPI(selectedmList)


            }
        })

        binding.tvApplyCoupan.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
            preferencesManager?.setStringValue(Constants.SUB_ACTIVITY, "SubscriptionActivity")
            preferencesManager?.setStringValue(Constants.SUBSCRIPTION_ID, subscriptionID)
            intent!!.action = "MyPoints"
            startActivity(intent)
            finish()
        })
    }

   /* @OnClick(R.id.tvState)
    fun tvState() {
        stateDialog()
    }

    @OnClick(R.id.tvApplyCoupan)
    fun tvApplyCoupan() {
        val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
        preferencesManager?.setStringValue(Constants.SUB_ACTIVITY, "SubscriptionActivity")
        preferencesManager?.setStringValue(Constants.SUBSCRIPTION_ID, subscriptionID)
        intent!!.action = "MyPoints"
        startActivity(intent)
        finish()
    }

    @OnClick(R.id.btnProceed)
    fun btnSaveSettings() {

        for (i in mList.indices) {
            if (mList[i].is_active) {
                isActive = true
            }
        }


        if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {

            if (isActive) {
                Toast.makeText(this, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
             //   Toast.makeText(this, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
              //  UpdateProfile.showToast(this)
            } else {
                if (binding.tvState!!.text.isEmpty()) {
                    Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT)
                        .show()

                } else {

                    var selectedState = stateList.find { it.name == binding.tvState!!.text }
                    Log.d("wssdxdddcSelect", selectedState!!.id.toString())

                    callUpdateStateAPI(selectedState!!.id)

                }

            }
        } else {
            selectedmList = mList!!.get(selectedposition!!.toInt())
            callCreateOrderAPI(selectedmList)


        }

    }*/

    private fun callUpdateStateAPI(id: Int) {

        val progressDialogLogout = ProgressDialog(this@SubscriptionProcessACtivity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog


        ApiClient.client.updateUserState("Bearer $token", id)!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {

                    preferencesManager?.setStringValue(
                        Constants.SHARED_PREFERENCE_LOGIN_STATE, binding.tvState!!.text.toString()
                    )

                    selectedmList = mList!!.get(selectedposition!!.toInt())
                    callCreateOrderAPI(selectedmList)


                } else if (response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@SubscriptionProcessACtivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@SubscriptionProcessACtivity,
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
                    this@SubscriptionProcessACtivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })


    }


    private fun stateDialog() {

        val builder = AlertDialog.Builder(this@SubscriptionProcessACtivity)
        builder.setTitle("Choose any State")


        val dataAdapter =
            ArrayAdapter(
                this@SubscriptionProcessACtivity,
                android.R.layout.simple_dropdown_item_1line,
                onlyStateList!!
            )
        builder.setAdapter(dataAdapter)
        { dialog, which ->
            selectedStateID = stateList[which].id
            binding.tvState!!.text = (stateList[which].name)
            Log.d("wssdxdddc", selectedStateID.toString())
            binding.cardDetails!!.visibility = View.VISIBLE

            preferencesManager?.setStringValue(
                Constants.SHARED_PREFERENCE_USER_STATE,
                (stateList[which].name)
            )
            if (selectedStateID == 22) {

                val amount: Double =
                    sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()
                val res = amount / 100.0f * 9
                binding.tvAmtIGST.text = "₹ " + res
                binding.tvAmtSGST.text = "₹ " + res
                binding.tvTotalPurchase.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        res * 2
                    ).minus(CopuanAmt!!)
                binding.totalPayable.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        CopuanAmt!!.toDouble()
                    ) + "0"

                binding.tvIGST.visibility = View.VISIBLE
                binding.tvAmtIGST.visibility = View.VISIBLE
                binding.tvSGST.visibility = View.VISIBLE
                binding.tvAmtSGST.visibility = View.VISIBLE
                binding.tvGSTandTaxes.visibility = View.GONE
                binding.tvAmtGSTandTaxes.visibility = View.GONE


            } else {


                val amount: Double =
                    sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()
                val res = amount / 100.0f * 18
                binding.tvAmtGSTandTaxes.text = "₹ " + res
                binding.tvTotalPurchase.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        res
                    )
                binding.totalPayable.text =
                    "₹ " + (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                        CopuanAmt!!.toDouble()
                    ) + "0"

                binding.tvIGST.visibility = View.GONE
                binding.tvAmtIGST.visibility = View.GONE
                binding.tvSGST.visibility = View.GONE
                binding.tvAmtSGST.visibility = View.GONE
                binding.tvGSTandTaxes.visibility = View.VISIBLE
                binding.tvAmtGSTandTaxes.visibility = View.VISIBLE
            }

        }
        val dialog = builder.create()
        dialog.show()


    }


    private fun callPayment(orderModel: OrderModel) {
        /*
                 You need to pass current activity in order to let Razorpay create CheckoutActivity
                */
        val activity: Activity = this@SubscriptionProcessACtivity
        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZOR_PAY_KEY)
        try {

            val options = JSONObject()
            options.put("name", SessionManager.getInstance(activity!!).getUserModel()?.name)
            options.put("order_id", orderModel?.data?.razorpayOrderId)
            options.put("description", "Demoing Charges")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            if (BuildConfig.DEMO_PAYMENT) {
                options.put("amount", "100")
            } else {
                options.put("amount", (razorPayamt?.times(100)).toString())
            }

            val preFill = JSONObject()
            preFill.put("email", SessionManager.getInstance(activity!!).getUserModel()?.email)
            preFill.put("contact", SessionManager.getInstance(activity!!).getUserModel()?.phoneNo)
            options.put("prefill", preFill)


            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }


    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
     //   checkOrederStatus(orderModel!!.data?.order?.id)
        try {
             failedOrderStatusAPI(code, response,paymentData)
            val obj = JSONObject(response)
            val errors = JSONObject(obj.getString("error"))
            Log.d("phonetypevalue ", errors.getString("description"))


            Toast.makeText(
                this,
                "Payment failed: " + errors.getString("description"),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                this@SubscriptionProcessACtivity,
                "Exception in onPaymentError: $e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
      //  checkOrederStatus(orderModel!!.data?.order?.id)

        try {
            if (orderModel?.data?.razorpayOrderId == paymentData?.orderId) {
                updateOrderStatusAPI(paymentData)
            }

         //   iDealingDistractionPresenter!!.updateSubscriptions(token, subscriptionID)
            fireEvent()
          /*  if (CopuanName!!.isNotEmpty()) {
                callCoupanAPI(CopuanID!!)
            }*/
            Toast.makeText(
                this@SubscriptionProcessACtivity,
                "Payment Success: " + paymentData,
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                this@SubscriptionProcessACtivity,
                "Exception in onPaymentSuccess: $e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun callCreateOrderAPI(subscriptionModel: SubscriptionModel.Subscriptions) {
        razorPayamt =
            (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                CopuanAmt!!.toDouble()
            )

        if (!orderModel?.data?.razorpayOrderId.isNullOrEmpty() && orderModel?.data?.order?.total_amount == razorPayamt) {
            preferencesManager!!.setIntegerValue(Constants.CHECK_ORDER_ID.toString(),(orderModel?.data?.order?.id!!))
            callPayment(orderModel!!)
        } else {
            if (BuildConfig.DEMO_PAYMENT) {
                iDealingDistractionPresenter?.createOrder(
                    token, "Subscription", subscriptionModel.id.toString(),
                    subscriptionModel.name, "", 1.0
                )
            } else {
                iDealingDistractionPresenter?.createOrder(
                    token, "Subscription", subscriptionModel.id.toString(),
                    subscriptionModel.name, "", razorPayamt
                )
            }
        }


    }

    private fun checkOrederStatus(id: Int?) {
        ApiClient.client.checkOrderStatus(
            "Bearer $token",
            id,

        )!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {

                        preferencesManager!!.setIntegerValue(Constants.CHECK_ORDER_ID.toString(),0)

                    }
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@SubscriptionProcessACtivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@SubscriptionProcessACtivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@SubscriptionProcessACtivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    private fun updateOrderStatusAPI(paymentData: PaymentData?) {
        var discountID = CopuanID.toString()

        if(CopuanID == 0){
            discountID = ""
        }

        ApiClient.client.updateOrderPayment(
            "Bearer $token",
            "Subscription",
            selectedmList.id.toString(),
            orderModel?.data?.order?.id.toString(),
            "Payment-Completed",
            paymentData?.paymentId,
            discountID
        )!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {

                        if (onBackCall.equals("DealingWithDistractions")) {
                            val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {

                            val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
                            intent!!.action = "tvSubscription"
                            startActivity(intent)
                            finish()
                        }

                    }
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@SubscriptionProcessACtivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@SubscriptionProcessACtivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@SubscriptionProcessACtivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    private fun failedOrderStatusAPI(code: Int, response: String?, paymentData: PaymentData?) {
        ApiClient.client.failedOrderStatus(
            "Bearer $token",
            orderModel?.data?.order?.id.toString(),
            "Failed",
            paymentData?.paymentId,
            code.toString(),
            response
        )!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {


                    }
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@SubscriptionProcessACtivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@SubscriptionProcessACtivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@SubscriptionProcessACtivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    private fun callCoupanAPI(CopuanID: Int) {
        val progressDialogLogout = ProgressDialog(this)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.userRewardCoupon(
            "Bearer $token",
            CopuanID
        )!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {


                    }
                } else if (response.code() == 400 || response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@SubscriptionProcessACtivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@SubscriptionProcessACtivity,
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
                    this@SubscriptionProcessACtivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })
    }


    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
    }

    override fun onSubscriptionList(subscriptionModel: SubscriptionModel?) {
    }

    override fun createOrderSuccess(model: OrderModel?) {
        if (model != null) {
            orderModel = model
            preferencesManager!!.setIntegerValue(Constants.CHECK_ORDER_ID.toString(),(orderModel?.data?.order?.id!!))
            callPayment(model)

        }
    }

    override fun updateSubscription(subscriptionModel: SubscriptionModel?) {
        if (onBackCall.equals("DealingWithDistractions")) {
            val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            val intent = Intent(this@SubscriptionProcessACtivity, HomeActivity::class.java)
            intent!!.action = "tvSubscription"
            startActivity(intent)
            finish()
        }


    }

    override fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?) {

        onlyStateList!!.clear()

        for (i in stateResponcePOJO!!.data!!.indices) {
            onlyStateList!!.add(stateResponcePOJO!!.data!![i].name!!)
        }

        stateList = stateResponcePOJO!!.data!!


    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(this)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(this, appErrorMessage, Toast.LENGTH_LONG)
            .show()
    }

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }


    private fun fireEvent() {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val fAmt =
            (sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice!!.toDouble()).minus(
                CopuanAmt!!.toDouble()
            )

        val bundle = Bundle()
        bundle.putString(
            FirebaseAnalytics.Param.ITEM_ID,
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].id.toString()
        )
        bundle.putString(
            FirebaseAnalytics.Param.ITEM_NAME,
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].name
        )
        bundle.putString(FirebaseAnalytics.Param.VALUE, fAmt.toString())


        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle)


    }

}