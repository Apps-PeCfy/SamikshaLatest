package com.samiksha.ui.drawer.subscription

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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

class SubscriptionProcessFragment : Fragment(), IDealingDistractionView,
    PaymentResultWithDataListener {

    var iDealingDistractionPresenter: IDealingDistractionPresenter? = null

    var sessionManager: SessionManager? = null
    private var onlyStateList: ArrayList<String>? = ArrayList()
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()
    var selectedStateID: Int? = 9999
    var selectedposition: String? = "0"
    private var subscriptionsAdapter: SubscriptionsAdapter? = null



 /*   @JvmField
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
    var tvState: TextView? = null*/

    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var isActive = false
    private var mList: ArrayList<SubscriptionModel.Subscriptions> = ArrayList()
    private lateinit var selectedmList: SubscriptionModel.Subscriptions
    var subscriptionID: String? = null




    lateinit var binding: FragmentSubscriptionProcessBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_subscription_process, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentSubscriptionProcessBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        sessionManager = SessionManager.getInstance(requireActivity())
        iDealingDistractionPresenter =
            dealingWithDistractionImplementer(this@SubscriptionProcessFragment)
        iDealingDistractionPresenter!!.state("IN")


        init()
        dataSet()
        return binding.root
    }

    private fun dataSet() {
        binding.tvPremium.text = sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].name
        binding.tvSubscriptiontxt.text =
            sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].description
        binding.tvSellingPrice.text =
            "₹ " + sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].sellingPrice

        subscriptionID = sessionManager!!.getUserSubscription()!!.subscriptions[selectedposition!!.toInt()].id.toString()



    }

    private fun init() {

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Subscription"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvSubscription"
                activity!!.startActivity(intent)

            }
        })


        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        selectedposition = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_SUBSCRIPTION_POSITION)
        mList = sessionManager!!.getUserSubscription()!!.subscriptions


        if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {
            binding.tvState.visibility = ViewGroup.VISIBLE
            if((preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_STATE))!!.isNotEmpty()){
                binding.tvState.text = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_STATE)
            }

        }else{
            binding.tvState.visibility = ViewGroup.GONE
        }

        setListners()
    }


    /* @OnClick(R.id.tvState)
    fun tvState() {
        stateDialog()
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
            //    UpdateProfile.showToast(activity!!)
                Toast.makeText(activity, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
                //Toast.makeText(activity, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
            } else {
                if (tvState!!.text.isEmpty()) {
                    Toast.makeText(activity, "Please select state", Toast.LENGTH_SHORT)
                        .show()

                } else {

                   var selectedState = stateList.find { it.name == tvState!!.text }
                    Log.d("wssdxdddcSelect",selectedState!!.id.toString())

                    callUpdateStateAPI(selectedState!!.id)

                }

            }
        }else{
            selectedmList = mList!!.get(selectedposition!!.toInt())
            CallPayment(selectedmList)
        }

    }
*/
    private fun callUpdateStateAPI(id: Int) {

        val progressDialogLogout = ProgressDialog(requireActivity())
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

                    selectedmList = mList!!.get(selectedposition!!.toInt())

                    CallPayment(selectedmList)


                } else if (response.code() == 422) {
                    val gson = GsonBuilder().create()
                    var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                    try {
                        pojo = gson.fromJson(
                            response.errorBody()!!.string(),
                            ValidationResponsePOJO::class.java
                        )
                        Toast.makeText(
                            activity,
                            pojo.errors!!.get(0).message,
                            Toast.LENGTH_LONG
                        ).show()


                    } catch (exception: IOException) {
                    }

                } else {
                    Toast.makeText(
                        activity,
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
                    activity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })



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
                    //    UpdateProfile.showToast(activity!!)
                    Toast.makeText(activity, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
                    //Toast.makeText(activity, R.string.subscription_plan_error, Toast.LENGTH_LONG).show()
                } else {
                    if (binding.tvState.text.isEmpty()) {
                        Toast.makeText(activity, "Please select state", Toast.LENGTH_SHORT)
                            .show()

                    } else {

                        var selectedState = stateList.find { it.name == binding.tvState.text }
                        Log.d("wssdxdddcSelect",selectedState!!.id.toString())

                        callUpdateStateAPI(selectedState!!.id)

                    }

                }
            }else{
                selectedmList = mList!!.get(selectedposition!!.toInt())
                CallPayment(selectedmList)
            }
        })
    }
    private fun stateDialog() {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose any State")


        val dataAdapter =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                onlyStateList!!
            )
        builder.setAdapter(dataAdapter)
        { dialog, which ->
            selectedStateID = stateList[which].id
            binding.tvState!!.text = (stateList[which].name)

            Log.d("wssdxdddc",selectedStateID.toString())

        }
        val dialog = builder.create()
        dialog.show()


    }


    private fun CallPayment(modelpayment: SubscriptionModel.Subscriptions) {
        /*
                 You need to pass current activity in order to let Razorpay create CheckoutActivity
                */
        val activity: Activity = requireActivity()
        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZOR_PAY_KEY)
        try {
            val options = JSONObject()
            options.put("name", SessionManager.getInstance(activity!!).getUserModel()?.name)
            options.put("description", "Demoing Charges")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            if(BuildConfig.DEMO_PAYMENT){
                options.put("amount", "100")
            }else{
                options.put("amount", (modelpayment.sellingPrice?.toDouble()?.times(100)).toString())
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
        try {
            // changeOrderPaymentStatusAPI("2", "",paymentData)
            Toast.makeText(
                activity,
                "Payment failed: " + code.toString() + " " + response,
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception in onPaymentError: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        try {
            iDealingDistractionPresenter!!.updateSubscriptions(token, subscriptionID)
            Toast.makeText(activity, "Payment Success: " + paymentData, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception in onPaymentSuccess: $e", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
    }

    override fun onSubscriptionList(subscriptionModel: SubscriptionModel?) {
    }

    override fun createOrderSuccess(model: OrderModel?) {

    }

    override fun updateSubscription(subscriptionModel: SubscriptionModel?) {
        val intent = Intent(activity, HomeActivity::class.java)
        intent!!.action = "tvSubscription"
        requireActivity().startActivity(intent)
        requireActivity().finish()


    }

    override fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?) {

        onlyStateList!!.clear()

        for (i in stateResponcePOJO!!.data!!.indices) {
            onlyStateList!!.add(stateResponcePOJO!!.data!![i].name!!)
        }

        stateList = stateResponcePOJO!!.data!!


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


}