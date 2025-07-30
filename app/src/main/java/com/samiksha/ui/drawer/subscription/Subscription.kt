package com.samiksha.ui.drawer.subscription

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.FragmentSubscriptionBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionPresenter
import com.samiksha.ui.home.dealingWithDistraction.IDealingDistractionView
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionImplementer
import com.samiksha.ui.home.dealingWithDistraction.pojo.OrderModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SubscriptionModel.Subscriptions
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


class Subscription : Fragment(), SubscriptionsAdapter.SubscriptionClick, IDealingDistractionView,
    PaymentResultWithDataListener {


    var iDealingDistractionPresenter: IDealingDistractionPresenter? = null
    private var subscriptionsAdapter: SubscriptionsAdapter? = null

/*
    @JvmField
    @BindView(R.id.recycler_subscription)
    var recyclerViewSubscription: RecyclerView? = null

    @JvmField
    @BindView(R.id.btnSaveSettings)
    var btnSaveSettings: Button? = null

    @JvmField
    @BindView(R.id.homeView)
    var homeView: ScrollView? = null*/

    var sessionManager: SessionManager? = null
    private var onlyStateList: ArrayList<String>? = ArrayList()
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()


    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    private var getSubscriptionsList: List<Subscriptions>? = ArrayList()
    private var mList: ArrayList<SubscriptionModel.Subscriptions> = ArrayList()
    private lateinit var selectedmList: SubscriptionModel.Subscriptions

    var selectedStateID: Int? = 9999
    var selectedsunPosition: Int = 9999
    var stateName: String? = ""

    var subscriptionID: String? = null
    var isActive = false

    lateinit var binding: FragmentSubscriptionBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_subscription, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        sessionManager = SessionManager.getInstance(requireActivity())
        iDealingDistractionPresenter = dealingWithDistractionImplementer(this@Subscription)
        initView()
        initRecyclerViewSubscription()
        callStateAPI()

        /*  if(sessionManager!!.getUserSubscription()!!.subscriptions.isEmpty()){
              callStateAPI()
          }else{
              setData()
          }
        */  return binding.root
    }
    private fun initView() {
        setListners()
    }

    private fun setListners() {
        binding.btnSaveSettings.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                var isActive = false
                for (i in sessionManager!!.getUserSubscription()!!.subscriptions.indices) {
                    if (sessionManager!!.getUserSubscription()!!.subscriptions[i].is_active) {
                        isActive = true
                    }
                }

                if (!isActive) {
                    preferencesManager!!.setStringValue(Constants.SHARED_PREFERENCE_USER_STATE,"")
                    val intent = Intent(activity, SubscriptionProcessACtivity::class.java)
                    requireActivity().startActivity(intent)
                }

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
    }
    private fun setData() {
        for (i in mList.indices) {
            if (mList[i].is_active) {
                isActive = true
                binding.btnSaveSettings.alpha = 0.5f
                binding.btnSaveSettings.isEnabled = false
            } else {
                binding.btnSaveSettings.isEnabled = true
            }
        }



        binding.recyclerSubscription.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerSubscription.itemAnimator = DefaultItemAnimator()
        subscriptionsAdapter = SubscriptionsAdapter(requireActivity(), mList!!)
        binding.recyclerSubscription.adapter = subscriptionsAdapter
        subscriptionsAdapter!!.setClickListener(this@Subscription)


    }

    private fun callStateAPI() {

        iDealingDistractionPresenter!!.state("IN")
        iDealingDistractionPresenter!!.subscriptionList(token)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initRecyclerViewSubscription() {

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Subscription"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


    }


  /*  @OnClick(R.id.btnSaveSettings)
    fun btnSaveSettings() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            var isActive = false
        for (i in sessionManager!!.getUserSubscription()!!.subscriptions.indices) {
            if (sessionManager!!.getUserSubscription()!!.subscriptions[i].is_active) {
                isActive = true
            }
        }

        if (!isActive) {
            preferencesManager!!.setStringValue(Constants.SHARED_PREFERENCE_USER_STATE,"")
            val intent = Intent(activity, SubscriptionProcessACtivity::class.java)
            requireActivity().startActivity(intent)
        }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }
*/

    private fun showAlertState() {

        val alertDialog = AlertDialog.Builder(requireActivity())
        val customLayout: View? =
            getLayoutInflater().inflate(R.layout.custom_state_dailog, null)
        alertDialog.setView(customLayout)

        val btnYes: Button = customLayout!!.findViewById(R.id.btnYes)
        val btnNo: Button = customLayout!!.findViewById(R.id.btnNo)
        val edtState: EditText = customLayout!!.findViewById(R.id.edtState)


        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(false)
        alert.show()

        edtState.setOnClickListener(View.OnClickListener {
            stateDialog(edtState)

        })

        btnYes.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()


        })

        btnNo.setOnClickListener(View.OnClickListener {


            val progressDialogLogout = ProgressDialog(requireActivity())
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog


            ApiClient.client.updateUserState("Bearer $token", selectedStateID)!!.enqueue(object :
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

                        selectedmList = mList!!.get(selectedsunPosition)

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


        })

    }


    private fun stateDialog(edtState: EditText) {

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
            stateName = stateList[which].name

            edtState.setText(stateList[which!!].name)


        }
        val dialog = builder.create()
        dialog.show()


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


    override fun click(id: String, position: Int) {

        subscriptionID = id
        selectedsunPosition = position
        Log.d("subscriptionIDsss", id)

        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_SUBSCRIPTION_POSITION,
            selectedsunPosition.toString()
        )
    }


    private fun CallPayment(modelpayment: Subscriptions) {
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
        binding.homeView.visibility = View.VISIBLE
        preferencesManager!!.createSubscription(subscriptionModel)
        sessionManager?.setUserSubscription(subscriptionModel!!)


        mList = subscriptionModel!!.subscriptions

        for (i in mList.indices) {
            if (mList[i].is_active) {
                isActive = true
                binding.btnSaveSettings.alpha = 0.5f
                binding.btnSaveSettings.isEnabled = false
            } else {
                binding.btnSaveSettings.isEnabled = true
            }
        }


        getSubscriptionsList = subscriptionModel!!.subscriptions

        binding.recyclerSubscription.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerSubscription.itemAnimator = DefaultItemAnimator()
        subscriptionsAdapter = SubscriptionsAdapter(requireActivity(), getSubscriptionsList!!)
        binding.recyclerSubscription.adapter = subscriptionsAdapter
        subscriptionsAdapter!!.setClickListener(this@Subscription)


    }

    override fun createOrderSuccess(model: OrderModel?) {

    }

    override fun updateSubscription(subscriptionModel: SubscriptionModel?) {
        subscriptionsAdapter!!.notifyDataSetChanged()
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