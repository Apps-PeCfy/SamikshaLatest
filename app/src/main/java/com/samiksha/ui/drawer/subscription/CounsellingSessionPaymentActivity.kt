package com.samiksha.ui.drawer.subscription

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.ActivityCounsellingSessionPaymentBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionPresenter
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionView
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionImplementer
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.home.pojo.CounsellingResponsePOJO
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
import java.util.ArrayList

class CounsellingSessionPaymentActivity : AppCompatActivity(), IDealingDistractionView,
    PaymentResultWithDataListener {
    lateinit var binding: ActivityCounsellingSessionPaymentBinding
    var iDealingDistractionPresenter: IDealingDistractionPresenter? = null
    private var onlyStateList: ArrayList<String>? = ArrayList()
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()
    var selectedStateID: Int? = 9999
    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var sessionManager: SessionManager? = null
    private var counsellingResponse: ArrayList<CounsellingResponsePOJO.DataItem> = ArrayList()

    var CopuanName: String? = ""
    var CopuanAmt: Int? = 0
    var CopuanID: Int? = 8888
    var razorPayamt: Double? = 0.0
    var orderModel: OrderModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_counselling_session_payment)
     //   ButterKnife.bind(this)
        binding = ActivityCounsellingSessionPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarGradiant(this)


        CopuanName = intent.getStringExtra("CopuanName")
        CopuanAmt = intent.getIntExtra("CopuanAmt", 0)
        CopuanID = intent.getIntExtra("CopuanID", 0)
        if (CopuanName != null) {
            binding.tvApplyCoupan.text = CopuanName
            binding.tvAmtCoupan!!.text = "- ₹ " + CopuanAmt.toString()
            Log.d("CoupanNameCopuanName", CopuanName!!)
        } else {
            CopuanName = ""
            Log.d("CoupanNameCopuanName", CopuanName!!)

        }

        if (CopuanName.isNullOrEmpty()) {
            binding.tvAmtCoupan!!.visibility = View.GONE
            binding.tvCoupan!!.visibility = View.GONE
        } else {
            binding.tvAmtCoupan!!.visibility = View.VISIBLE
            binding.tvCoupan!!.visibility = View.VISIBLE

        }



        PreferencesManager.initializeInstance(this@CounsellingSessionPaymentActivity)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        sessionManager = SessionManager.getInstance(this)

        iDealingDistractionPresenter =
            dealingWithDistractionImplementer(this@CounsellingSessionPaymentActivity)
        iDealingDistractionPresenter!!.state("IN")
        init()
        callCounsellingSessionListAPI()

    }


   /* @OnClick(R.id.tvState)
    fun tvState() {
        stateDialog()
    }
*/
    private fun stateDialog() {

        val builder = AlertDialog.Builder(this@CounsellingSessionPaymentActivity)
        builder.setTitle("Choose any State")


        val dataAdapter =
            ArrayAdapter(
                this@CounsellingSessionPaymentActivity,
                android.R.layout.simple_dropdown_item_1line,
                onlyStateList!!
            )
        builder.setAdapter(dataAdapter)
        { dialog, which ->
            selectedStateID = stateList[which].id
            binding.tvState!!.text = (stateList[which].name)
            Log.d("wssdxdddc", selectedStateID.toString())
            binding.cardDetails!!.visibility = View.VISIBLE

            if (selectedStateID == 22) {

                val amount = counsellingResponse[0].regularPrice
                val res = amount.toDouble() / 100.0f * 9
                val res1 = amount / 100.0f * 82
                binding.tvAmtIGST.text = "₹ " + res
                binding.tvAmtSGST.text = "₹ " + res
                binding.tvTotalPurchase.text = "₹ " + res1
                binding.totalPayable.text =
                    "₹ " + (counsellingResponse[0].regularPrice!!.toDouble()).minus(CopuanAmt!!.toDouble()) + "0"



                binding.tvIGST.visibility = View.VISIBLE
                binding.tvAmtIGST.visibility = View.VISIBLE
                binding.tvSGST.visibility = View.VISIBLE
                binding.tvAmtSGST.visibility = View.VISIBLE
                binding.tvGSTandTaxes.visibility = View.GONE
                binding.tvAmtGSTandTaxes.visibility = View.GONE


            } else {


                val amount = counsellingResponse[0].regularPrice
                val res = amount.toDouble() / 100.0f * 18
                val res1 = amount / 100.0f * 82
                binding.tvAmtGSTandTaxes.text = "₹ " + (res)
                binding.tvTotalPurchase.text = "₹ " + res1
                binding.totalPayable.text =
                    "₹ " + (counsellingResponse[0].regularPrice!!.toDouble()).minus(CopuanAmt!!.toDouble()) + "0"



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


   /* @OnClick(R.id.btnProceed)
    fun btnSaveSettings() {



            if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {


                if (binding.tvState!!.text.isEmpty()) {
                    Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    var selectedState = stateList.find { it.name == binding.tvState!!.text }
                    callUpdateStateAPI(selectedState!!.id)
                }


            } else {
                callCreateOrderAPI()
              //  startPayment(counsellingResponse)

            }



    }
*/
    private fun callCreateOrderAPI(){
        val fAmt =
            (counsellingResponse[0].regularPrice!!.toDouble()).minus(CopuanAmt!!.toDouble())
        razorPayamt = fAmt

        if (!orderModel?.data?.razorpayOrderId.isNullOrEmpty() && orderModel?.data?.order?.total_amount == razorPayamt) {
            preferencesManager!!.setIntegerValue(Constants.CHECK_ORDER_ID.toString(),(orderModel?.data?.order?.id!!))
            startPayment(orderModel!!)
           } else {
            if(BuildConfig.DEMO_PAYMENT){
                iDealingDistractionPresenter?.createOrder(token, "CounsellingSession", counsellingResponse[0].id.toString(),
                    counsellingResponse[0].name, "", 1.0)
            }else{
                iDealingDistractionPresenter?.createOrder(token, "CounsellingSession", counsellingResponse[0].id.toString(),
                    counsellingResponse[0].name, "", razorPayamt)
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
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@CounsellingSessionPaymentActivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }


    private fun startPayment(model: OrderModel) {
        /*
                 You need to pass current activity in order to let Razorpay create CheckoutActivity
                */
        val activity: Activity = this@CounsellingSessionPaymentActivity
        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZOR_PAY_KEY)
        try {
            
            val options = JSONObject()
            options.put("name", SessionManager.getInstance(activity!!).getUserModel()?.name)
            options.put("order_id", model?.data?.razorpayOrderId)
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

    private fun callUpdateStateAPI(id: Int) {

        val progressDialogLogout = ProgressDialog(this@CounsellingSessionPaymentActivity)
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

                    preferencesManager!!.setStringValue(
                        Constants.SHARED_PREFERENCE_LOGIN_STATE, binding.tvState!!.text.toString()
                    )
                   // startPayment(counsellingResponse)

                    callCreateOrderAPI()
                } else if (response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
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
                    this@CounsellingSessionPaymentActivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })


    }


 /*   @OnClick(R.id.tvApplyCoupan)
    fun tvApplyCoupan() {
        val intent = Intent(this@CounsellingSessionPaymentActivity, HomeActivity::class.java)
        preferencesManager!!.setStringValue(
            Constants.SUB_ACTIVITY,
            "CounsellingSessionPaymentActivity"
        )
        intent!!.action = "MyPoints"
        startActivity(intent)
        finish()
    }*/


    private fun callCounsellingSessionListAPI() {
        val progressDialogLogout = ProgressDialog(this)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.getCounsellingSessionsList("Bearer $token")!!.enqueue(object :
            Callback<CounsellingResponsePOJO?> {
            override fun onResponse(
                call: Call<CounsellingResponsePOJO?>,
                response: Response<CounsellingResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {
                        counsellingResponse =
                            response!!.body()!!.counsellingsessionlist!!.data as ArrayList<CounsellingResponsePOJO.DataItem>
                        if (response!!.body()!!.counsellingsessionlist!!.data!!.isNotEmpty()) {
                            binding.tvSellingPrice.text = "₹ " +
                                    response!!.body()!!.counsellingsessionlist!!.data?.get(0)!!.regularPrice.toString()
                            binding.tvPremium.text =
                                response!!.body()!!.counsellingsessionlist!!.data?.get(0)!!.name.toString()
                            binding.tvSubscriptiontxt.text =
                                response!!.body()!!.counsellingsessionlist!!.data?.get(0)!!.description.toString()
                            dataset()
                            // startPayment(response)

                        } else {
                            Toast.makeText(
                                this@CounsellingSessionPaymentActivity,
                                "Data Not Found",
                                Toast.LENGTH_LONG
                            ).show()

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
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<CounsellingResponsePOJO?>, t: Throwable
            ) {
                progressDialogLogout.dismiss()
                Toast.makeText(
                    this@CounsellingSessionPaymentActivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }

    private fun dataset() {
        if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {
            binding.tvState!!.visibility = ViewGroup.VISIBLE
            binding.stateCard!!.visibility = ViewGroup.VISIBLE
            /*  if ((preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_STATE))!!.isNotEmpty()) {
                  tvState!!.text =
                      preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_STATE)
                  cardDetails!!.visibility = View.VISIBLE

                  if ((tvState!!.text).equals("Maharashtra")) {

                      val amount = counsellingResponse[0].regularPrice
                      val res = amount.toDouble() / 100.0f * 9
                      val res1 = amount / 100.0f * 82
                      tvAmtIGST.text = "₹ " + res
                      tvAmtSGST.text = "₹ " + res
                      tvTotalPurchase.text = "₹ " + res1
                      totalPayable.text =
                          "₹ " + (counsellingResponse[0].regularPrice!!.toDouble()).minus(CopuanAmt!!.toDouble()) + "0"

                      tvIGST.visibility = View.VISIBLE
                      tvAmtIGST.visibility = View.VISIBLE
                      tvSGST.visibility = View.VISIBLE
                      tvAmtSGST.visibility = View.VISIBLE
                      tvGSTandTaxes.visibility = View.GONE
                      tvAmtGSTandTaxes.visibility = View.GONE


                  } else {


                      val amount = counsellingResponse[0].regularPrice
                      val res = amount.toDouble() / 100.0f * 18
                      val res1 = amount / 100.0f * 82
                      tvAmtGSTandTaxes.text = "₹ " + (res)
                      tvTotalPurchase.text = "₹ " + res1
                      totalPayable.text =
                          "₹ " + (counsellingResponse[0].regularPrice!!.toDouble()).minus(CopuanAmt!!.toDouble()) + "0"

                      tvIGST.visibility = View.GONE
                      tvAmtIGST.visibility = View.GONE
                      tvSGST.visibility = View.GONE
                      tvAmtSGST.visibility = View.GONE
                      tvGSTandTaxes.visibility = View.VISIBLE
                      tvAmtGSTandTaxes.visibility = View.VISIBLE
                  }


              } else {
                  cardDetails!!.visibility = View.GONE
              }
  */
        } else {
            binding.tvState!!.visibility = ViewGroup.GONE
            binding.stateCard!!.visibility = ViewGroup.GONE
            binding.cardDetails!!.visibility = View.GONE
        }
    }


    private fun init() {
        setListners()
        val toolbar: Toolbar = this.findViewById(R.id.toolbar_home)
        toolbar.title = "Book Consultation"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar!!.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val intent =
                    Intent(this@CounsellingSessionPaymentActivity, HomeActivity::class.java)
                intent!!.action = "HomeFragment"
                startActivity(intent)

            }
        })


        /* if (sessionManager!!.getUserModel()!!.state!!.name!!.isNotEmpty()) {
             tvState!!.text = sessionManager!!.getUserModel()!!.state!!.name
         }
 */

    }

    private fun setListners() {
        binding.tvState.setOnClickListener(View.OnClickListener {
            stateDialog()
        })

        binding.tvApplyCoupan.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CounsellingSessionPaymentActivity, HomeActivity::class.java)
            preferencesManager!!.setStringValue(
                Constants.SUB_ACTIVITY,
                "CounsellingSessionPaymentActivity"
            )
            intent!!.action = "MyPoints"
            startActivity(intent)
            finish()
        })

        binding.btnProceed.setOnClickListener(View.OnClickListener {

            if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {


                if (binding.tvState.text.isEmpty()) {
                    Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    var selectedState = stateList.find { it.name == binding.tvState.text }
                    callUpdateStateAPI(selectedState!!.id)
                }


            } else {
                callCreateOrderAPI()
                //  startPayment(counsellingResponse)

            }

        })
    }

    private fun setStatusBarGradiant(activity: CounsellingSessionPaymentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }

    override fun onBackPressed() {
        ProjectUtils.showAlertDialog(this)
    }

    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
    }

    override fun onSubscriptionList(subscriptionModel: SubscriptionModel?) {
    }

    override fun createOrderSuccess(model: OrderModel?) {
        if (model != null) {
            orderModel = model
            preferencesManager!!.setIntegerValue(Constants.CHECK_ORDER_ID.toString(),(orderModel?.data?.order?.id!!))
            startPayment(model)

        }
    }

    override fun updateSubscription(subscriptionModel: SubscriptionModel?) {
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

    override fun onPaymentSuccess(p0: String?, paymentData: PaymentData?) {
    //    checkOrederStatus(orderModel?.data?.order?.id)

        try {

            if(orderModel?.data?.razorpayOrderId == paymentData?.orderId){
                updateOrderStatusAPI(paymentData)
            }
            fireEvent()
            if (CopuanName!!.isNotEmpty()) {
                //callCoupanAPI(CopuanID!!)
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                this@CounsellingSessionPaymentActivity,
                "Exception in onPaymentSuccess: $e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    

    override fun onPaymentError(p0: Int, response: String?, p2: PaymentData?) {
     //   checkOrederStatus(orderModel?.data?.order?.id)

        try {
            failedOrderStatusAPI(p0, response,p2)
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
                this@CounsellingSessionPaymentActivity,
                "Exception in onPaymentError: $e",
                Toast.LENGTH_SHORT
            ).show()
        }
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

                        preferencesManager!!.setStringValue(
                            Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS,
                            "Payment-Completed"
                        )


                        val intent =
                            Intent(this@CounsellingSessionPaymentActivity, HomeActivity::class.java)
                        intent!!.action = "tvMySession"
                        startActivity(intent)

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
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
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
                    this@CounsellingSessionPaymentActivity,
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
            "CounsellingSession",
            counsellingResponse[0].id.toString(),
            orderModel?.data?.order?.id.toString(),
            "Payment-Completed",
            paymentData?.paymentId,
            discountID)!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        preferencesManager!!.setStringValue(
                            Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS,
                            "Payment-Completed"
                        )


                        val intent =
                            Intent(this@CounsellingSessionPaymentActivity, HomeActivity::class.java)
                        intent!!.action = "tvMySession"
                        startActivity(intent)
                        finish()
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
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@CounsellingSessionPaymentActivity,
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
                            this@CounsellingSessionPaymentActivity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        this@CounsellingSessionPaymentActivity,
                        "Internal Server Error",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@CounsellingSessionPaymentActivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }


    private fun fireEvent() {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val bundle = Bundle()
        if (!counsellingResponse.isNullOrEmpty()) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, counsellingResponse[0].id.toString())
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, counsellingResponse[0].name)
            bundle.putString(
                FirebaseAnalytics.Param.VALUE,
                counsellingResponse[0].regularPrice.toString()
            )
        }

        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle)

        mFirebaseAnalytics.logEvent("book_session", bundle)

    }
}