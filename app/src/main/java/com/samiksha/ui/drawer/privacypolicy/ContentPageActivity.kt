package com.samiksha.ui.drawer.privacypolicy

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.GsonBuilder
import com.samiksha.R
import com.samiksha.databinding.ActivityContentPageBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContentPageActivity : AppCompatActivity() {

   /* @JvmField
    @BindView(R.id.webView)
    var webView: WebView? = null

    @JvmField
    @BindView(R.id.toolbar)
    var mToolbar: Toolbar? = null*/

    var contentPadeText: String? = null

    lateinit var binding: ActivityContentPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_page)
      //  ButterKnife.bind(this)
      //  setStatusBarGradiant(this)
        binding = ActivityContentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentPadeText = intent.getStringExtra("ContentPageData")

        binding.toolbar.title = contentPadeText
        binding.toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }


        if(contentPadeText.equals("Privacy Policy")){

            callApi("Privacy-Policy")
        }else{
            callApi("Terms-Of-Use")
        }

    }

    private fun callApi(contentData: String) {

        val progressDialog = ProgressDialog(this@ContentPageActivity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog


        val call = ApiClient.client.pageContents( contentData)
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
                        Toast.makeText(this@ContentPageActivity, pojo.message, Toast.LENGTH_LONG).show()


                    } catch (exception: IOException) {
                    }

                }
            }

            override fun onFailure(
                call: Call<ContentResponsePOJO?>, t: Throwable
            ) {
                progressDialog.dismiss()
                Toast.makeText(this@ContentPageActivity, t.message, Toast.LENGTH_LONG).show()
                Log.d("response", t.message.toString())
            }
        })
    }


    private fun setStatusBarGradiant(activity: ContentPageActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }
}
