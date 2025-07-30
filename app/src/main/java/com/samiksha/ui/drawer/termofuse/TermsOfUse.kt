package com.samiksha.ui.drawer.termofuse

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.FragmentContentPageBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.privacypolicy.ContentResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class TermsOfUse: Fragment() {

  /*  @JvmField
    @BindView(R.id.webView)
    var webView: WebView? = null*/

    var preferencesManager: PreferencesManager? = null
    var sharedpreferences: SharedPreferences? = null
    var token: String? = null

    lateinit var binding: FragmentContentPageBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.fragment_content_page, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentContentPageBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Terms Of Use"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })


        PreferencesManager.initializeInstance(requireContext().applicationContext)
        sharedpreferences = requireActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE)

        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        callApi()



     /*   webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView!!.getSettings().setLoadsImagesAutomatically(true)
        webView!!.getSettings().setJavaScriptEnabled(true)
        webView!!.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webView!!.loadUrl("http://dev.samiksha.co/terms-of-use")
*/
        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                if(ProjectUtils.checkInternetAvailable(context)!!) {
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


    private fun callApi() {

        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog


        val call = ApiClient.client.pageContents( "Terms-Of-Use")
        call!!.enqueue(object : Callback<ContentResponsePOJO?> {
            override fun onResponse(
                call: Call<ContentResponsePOJO?>,
                response: Response<ContentResponsePOJO?>
            ) {
                progressDialog.dismiss()
                if (response.code() == 200) {
                    if (response.body() != null) {
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webView.loadDataWithBaseURL(
                            null,
                            response.body()!!.pageContent!!.content!!,
                            "text/html",
                            "utf-8",
                            null
                        )

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

                }
            }

            override fun onFailure(
                call: Call<ContentResponsePOJO?>, t: Throwable
            ) {
                progressDialog.dismiss()
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()

            }
        })
    }





}