package com.samiksha.ui.drawer.contactus

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentContactUsBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.mypoints.MyPointsAdapter
import com.samiksha.ui.drawer.mypoints.pojo.MyRewardsResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContactUs : Fragment() {

  /*  @JvmField
    @BindView(R.id.btnSubmitContactUs)
    var btnSubmitContactUs: Button? = null

    @JvmField
    @BindView(R.id.editReview)
    var editReview: EditText? = null*/


    var preferencesManager: PreferencesManager? = null

    lateinit var binding: FragmentContactUsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_contact_us, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentContactUsBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()

        return binding.root
    }

   /* @OnClick(R.id.btnSubmitContactUs)
    fun dob() {
        callContactUsAPI()

       *//* if (editReview!!.text.toString().isEmpty()) {
            Toast.makeText(activity, "Enter message", Toast.LENGTH_SHORT).show()
        } else {

           }*//*
    }*/

    private fun callContactUsAPI() {
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
      var  token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        val progressDialogLogout = ProgressDialog(activity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.contactUs("Bearer $token",binding.editReview!!.text.toString())!!.enqueue(object :
            Callback<OnlyMessageResponsePOJO?> {
            override fun onResponse(
                call: Call<OnlyMessageResponsePOJO?>,
                response: Response<OnlyMessageResponsePOJO?>
            ) {
                progressDialogLogout.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Toast.makeText(
                            activity,
                            response.body()!!.message,
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(activity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intent)

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)
            }
        }
        return true
    }

    private fun setListners() {
        binding.btnSubmitContactUs.setOnClickListener(View.OnClickListener {
            callContactUsAPI()
        })
    }

    private fun initView() {
        setListners()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Contact Us"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })

    }


}