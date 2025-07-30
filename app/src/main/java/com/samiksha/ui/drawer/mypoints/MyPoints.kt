package com.samiksha.ui.drawer.mypoints

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentMyPointsBinding
import com.samiksha.networking.ApiClient.client
import com.samiksha.ui.drawer.mypoints.pojo.MyRewardsResponsePOJO
import com.samiksha.ui.drawer.readempoints.RedeemPoints
import com.samiksha.ui.drawer.subscription.Subscription
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MyPoints : Fragment() {

    var myPointsAdapter: MyPointsAdapter? = null

   /* @JvmField
    @BindView(R.id.recycler_myPoints)
    var recycler_myPoints: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvRedeemPoints)
    var tvRedeemPoints: TextView? = null

    @JvmField
    @BindView(R.id.txtTotalRedeemPoint)
    var txtTotalRedeemPoint: TextView? = null

    @JvmField
    @BindView(R.id.rlRedeemPoints)
    var rlRedeemPoints: RelativeLayout? = null*/

    var token: String? = null
    var noReward: String? = null
    var preferencesManager: PreferencesManager? = null

    private var getRewardList: List<MyRewardsResponsePOJO.DataItem>? = ArrayList()

    lateinit var binding: FragmentMyPointsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_my_points, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentMyPointsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
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



  /*  @OnClick(R.id.tvRedeemPoints)
    fun tvRedeemPoints() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setStringValue(Constants.SUB_ACTIVITY,"Subscription")
        val intent = Intent(activity, HomeActivity::class.java)
        intent!!.action = "MyPoints"
        requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }*/



    private fun initView() {
        setListners()
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        noReward = preferencesManager!!.getStringValue(Constants.NOT_REWARD)



        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Reward Points"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(noReward.equals("No Reward")){
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent!!.action = "MyAccount"
                    activity!!.startActivity(intent)

                }else{
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent.action = "RewardFragment"
                    startActivity(intent)
                }

            }
        })




        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        client.getuserRewards("Bearer $token")!!.enqueue(object : Callback<MyRewardsResponsePOJO?> {
            override fun onResponse(
                call: Call<MyRewardsResponsePOJO?>,
                response: Response<MyRewardsResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {



                        getRewardList = response.body()!!.data

                        for (i in getRewardList!!.indices) {
                            if(getRewardList!!.get(i).key.equals("Total rewards")){
                                binding.txtTotalRedeemPoint.text = getRewardList!!.get(i).points.toString()

                                if(getRewardList!!.get(i).points>199){
                                    binding.rlRedeemPoints.visibility = View.VISIBLE

                                }
                            }
                        }

                        myPointsAdapter = MyPointsAdapter(requireActivity(), getRewardList)
                        binding.recyclerMyPoints.layoutManager = LinearLayoutManager(activity)
                        binding.recyclerMyPoints.adapter = myPointsAdapter

                        myPointsAdapter!!.notifyDataSetChanged()


                    }
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
                call: Call<MyRewardsResponsePOJO?>, t: Throwable
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
        binding.tvRedeemPoints.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                preferencesManager!!.setStringValue(Constants.SUB_ACTIVITY,"Subscription")
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyPoints"
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
    }
}