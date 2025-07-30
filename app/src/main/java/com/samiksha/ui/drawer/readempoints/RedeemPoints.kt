package com.samiksha.ui.drawer.readempoints

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentRedeemPointsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.readempoints.pojo.CoupanListResponsePOJO
import com.samiksha.ui.drawer.subscription.CounsellingSessionPaymentActivity
import com.samiksha.ui.drawer.subscription.SubscriptionProcessACtivity
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RedeemPoints : Fragment(), AdapterView.OnItemSelectedListener {

    var myRedeemPointsAdapter: RedeemPointsAdapter? = null

  /*  @JvmField
    @BindView(R.id.recycler_myPoints)
    var recycler_myPoints: RecyclerView? = null

    @JvmField
    @BindView(R.id.rl_home)
    var rl_home: RelativeLayout? = null

    @JvmField
    @BindView(R.id.ll_no_data)
    var ll_no_data: LinearLayout? = null

    @JvmField
    @BindView(R.id.tvOffers)
    var tvOffers: TextView? = null*/

    var token: String? = null
    var preferencesManager: PreferencesManager? = null
    var coupanList: List<CoupanListResponsePOJO.DataItem>? = ArrayList()
    var coupanList1: ArrayList<CoupanListResponsePOJO.DataItem>? = ArrayList()
    var sessionManager: SessionManager? = null
    lateinit var binding: FragmentRedeemPointsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     //   val rootView = inflater.inflate(R.layout.fragment_redeem_points, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentRedeemPointsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
    }


    private fun initView() {

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        sessionManager = SessionManager.getInstance(requireActivity())

        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Redeem Points"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)
                        .equals("SubscriptionActivity")
                ) {
                    preferencesManager!!.setStringValue(Constants.SUB_ACTIVITY, "")
                    val intent = Intent(activity, SubscriptionProcessACtivity::class.java)
                    activity!!.startActivity(intent)
                    activity!!.finish()
                } else if (preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)
                        .equals("CounsellingSessionPaymentActivity")
                ) {
                    preferencesManager!!.setStringValue(Constants.SUB_ACTIVITY, "")
                    val intent = Intent(activity, CounsellingSessionPaymentActivity::class.java)
                    activity!!.startActivity(intent)
                    activity!!.finish()
                } else {
                    preferencesManager!!.setStringValue(Constants.SUB_ACTIVITY, "")
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent!!.action = "MyAccountBack"
                    activity!!.startActivity(intent)
                }


            }
        })


        callcoupanList()


    }


    override fun onDestroy() {
        Log.d("onDetach", "onDestroyf")
        super.onDestroy()

    }

    private fun callcoupanList() {

        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog

        ApiClient.client.getRewardCouponListWithOutParameter("Bearer $token")!!.enqueue(object :
            Callback<CoupanListResponsePOJO?> {
            override fun onResponse(
                call: Call<CoupanListResponsePOJO?>,
                response: Response<CoupanListResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {


                    if (response.body()?.rewardCouponList?.data!!.isNotEmpty()) {

                     //   tvOffers!!.visibility = View.GONE
                        binding.llNoData.visibility = View.GONE
                        binding.rlHome.visibility = View.VISIBLE
                        coupanList = response!!.body()!!.rewardCouponList!!.data

                        for (i in coupanList!!.indices) {
                            var getCallingActivity =
                                preferencesManager!!.getStringValue(Constants.SUB_ACTIVITY)

                            if (getCallingActivity.equals("SubscriptionActivity") || getCallingActivity.equals("CounsellingSessionPaymentActivity"))
                                {
                                if (coupanList?.get(i)!!.discountType.equals("Subscription")) {
                                    coupanList1!!.add(coupanList!!.get(i))

                                }


                            }else{
                                if (coupanList?.get(i)!!.discountType.equals("Subscription")) {
                                    }else{
                                    coupanList1!!.add(coupanList!!.get(i))

                                }


                            }                            }


                        if(coupanList1!!.size>0) {

                            binding.llNoData.visibility = View.GONE
                            binding.rlHome.visibility = View.VISIBLE


                            myRedeemPointsAdapter = RedeemPointsAdapter(
                                requireActivity(),
                                coupanList1,
                                sessionManager!!.getUserModel()!!.subscription
                            )
                            binding.recyclerMyPoints.layoutManager = LinearLayoutManager(activity)
                            binding.recyclerMyPoints.adapter = myRedeemPointsAdapter

                            myRedeemPointsAdapter!!.notifyDataSetChanged()
                        }else{
                            binding.llNoData.visibility = View.VISIBLE
                            binding.rlHome.visibility = View.GONE
                        }

                        } else {
                           // tvOffers!!.visibility = View.VISIBLE
                        binding.llNoData.visibility = View.VISIBLE
                        binding.rlHome.visibility = View.GONE

                        }


                    } else if (response.code() == 422) {
                        val gson = GsonBuilder().create()
                        var pojo: ValidationResponsePOJO? = ValidationResponsePOJO()
                        try {
                            pojo = gson.fromJson(
                                response.errorBody()!!.string(),
                                ValidationResponsePOJO::class.java
                            )
                            Toast.makeText(activity, pojo.message, Toast.LENGTH_LONG).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<CoupanListResponsePOJO?>,
                    t: Throwable
                ) {
                    progressDialog.dismiss()
                    Log.d("response", t.stackTrace.toString())
                }

            })


        }


                override fun onNothingSelected(p0: AdapterView<*>?) {
        }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
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

    }