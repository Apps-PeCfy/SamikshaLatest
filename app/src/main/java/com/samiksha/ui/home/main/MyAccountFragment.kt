package com.samiksha.ui.home.main

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.GsonBuilder
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.FragmentMyAccountBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.contactus.ContactUs
import com.samiksha.ui.drawer.faqs.FAQs
import com.samiksha.ui.drawer.favourites.FavoritesActivity
import com.samiksha.ui.drawer.mypoints.MyPoints
import com.samiksha.ui.drawer.privacypolicy.PrivacyPolicy
import com.samiksha.ui.drawer.referAndEarn.ReferAndEarn
import com.samiksha.ui.drawer.settings.Setting
import com.samiksha.ui.drawer.subscription.Subscription
import com.samiksha.ui.drawer.termofuse.TermsOfUse
import com.samiksha.ui.drawer.website.WebSite
import com.samiksha.ui.home.pojo.SignOutResponsePOJO
import com.samiksha.ui.login.LoginActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MyAccountFragment : Fragment() {


    /*@JvmField
    @BindView(R.id.iv_user)
    var iv_user: RoundedImageView? = null

    @JvmField
    @BindView(R.id.edtFirstName)
    var edtFirstName: TextView? = null

    @JvmField
    @BindView(R.id.tvTotalReward)
    var tvTotalReward: TextView? = null


    @JvmField
    @BindView(R.id.llhelpsupprt)
    var llhelpsupprt: LinearLayout? = null

    @JvmField
    @BindView(R.id.llLegal)
    var llLegal: LinearLayout? = null

 @JvmField
    @BindView(R.id.rlMainProfile)
    var rlMainProfile: RelativeLayout? = null


    @JvmField
    @BindView(R.id.imgLegal)
    var imgLegal: ImageView? = null

    @JvmField
    @BindView(R.id.imgHelpSupport)
    var imgHelpSupport: ImageView? = null

*/
    private var sessionManager: SessionManager? = null
    var isClicked: String? = "No"
    var isClickedhelp: String? = "No"
    var isClickedLegal: String? = "No"

    var preferencesManager: PreferencesManager? = null

    var deviceID: String? = null
    var token: String? = null
    lateinit var binding: FragmentMyAccountBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    //    val rootView = inflater.inflate(R.layout.fragment_my_account, container, false)
      //  ButterKnife.bind(this, rootView)
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

    //    callProfileAPI()
        setData()
        setListners()
        return binding.root
    }

    private fun callProfileAPI() {


        ApiClient.client.getProfile(
            "Bearer $token"
        )!!.enqueue(object :
            Callback<CheckOtpResponsePOJO?> {
            override fun onResponse(
                call: Call<CheckOtpResponsePOJO?>,
                response: Response<CheckOtpResponsePOJO?>
            ) {
                if (response.code() == 200) {
                    binding.rlMainProfile.visibility = View.VISIBLE

                    preferencesManager!!.createLoginSession(response.body()!!.user)
                    SessionManager.getInstance(requireContext())
                        .setUserModel(response.body()!!.user!!)

                    preferencesManager!!.setStringValue(
                        Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC,
                        response.body()!!.user!!.profilePic
                    )



                    Glide.with(this@MyAccountFragment)
                        .load(response.body()!!.user!!.profilePic)
                        .apply(
                            RequestOptions()
                                .centerCrop()
                                .dontAnimate()
                                .dontTransform()
                        ).into(binding.ivUser)



                    setData()


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
                        )
                            .show()


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
                call: Call<CheckOtpResponsePOJO?>,
                t: Throwable
            ) {

            }
        })

    }

    private fun setData() {

        sessionManager = SessionManager.getInstance(requireContext())

        binding.edtFirstName.setText(sessionManager?.getUserModel()?.firstName + " " + sessionManager?.getUserModel()?.lastName)
        binding.tvTotalReward.setText("Total reward points " + sessionManager?.getUserModel()?.totalRewards.toString())

        Glide.with(this@MyAccountFragment)
            .load(sessionManager?.getUserModel()?.profilePic)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
            ).into(binding.ivUser)


        binding.rlMainProfile.visibility = View.VISIBLE


    }

    /*@OnClick(R.id.tvSubscription)
    fun tvSubscription() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvSubscription"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvAboutSamiksha)
    fun tvAboutSamiksha() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvAboutSamiksha"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvDeleteAccount)
    fun tvDeleteAccount() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setCancelable(false)
            alertDialog.setTitle(getString(R.string.app_name))
            alertDialog.setMessage("Do you want to delete account")
            alertDialog.setPositiveButton("Yes") { dialog, which ->
                callDeleteAccountAPI()
                dialog.dismiss()

            }
            alertDialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            alertDialog.show()
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }
*/
    private fun callDeleteAccountAPI() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            deviceID = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)


            val progressDialogLogout = ProgressDialog(activity)
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog

            ApiClient.client.deleteAccount("Bearer $token", deviceID, "Android")!!
                .enqueue(object : Callback<SignOutResponsePOJO?> {
                    override fun onResponse(
                        call: Call<SignOutResponsePOJO?>,
                        response: Response<SignOutResponsePOJO?>
                    ) {
                        progressDialogLogout.dismiss()
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                preferencesManager!!.clear()

                                preferencesManager!!.setBooleanValue(
                                    Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                                    false
                                )

                                val intent =
                                    Intent(activity, LoginActivity::class.java)
                                startActivity(intent)
                                activity?.finish()

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
                        call: Call<SignOutResponsePOJO?>, t: Throwable
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

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }

    private fun setListners() {
        binding.tvSubscription.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvSubscription"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvAboutSamiksha.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvAboutSamiksha"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvDeleteAccount.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setCancelable(false)
                alertDialog.setTitle(getString(R.string.app_name))
                alertDialog.setMessage("Do you want to delete account")
                alertDialog.setPositiveButton("Yes") { dialog, which ->
                    callDeleteAccountAPI()
                    dialog.dismiss()

                }
                alertDialog.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

                alertDialog.show()
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvEdit.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "EDITPROFILE"
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.txtMyDownloads.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.action = "myDownloads"
            requireActivity().startActivity(intent)

        })
        binding.tvMyFav.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, FavoritesActivity::class.java)
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.tvReferEarn.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvReferEarn"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.tvRateUs.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val uri: Uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.tvNotification.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvNotification"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvRedeamPoints.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                preferencesManager!!.setStringValue(Constants.NOT_REWARD, "No Reward")
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccountBack"
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvTotalReward.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                preferencesManager!!.setStringValue(Constants.NOT_REWARD, "No Reward")
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccountBack"
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }



        })
        binding.tvFaq.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {


                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvFaq"
                requireActivity().startActivity(intent)

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }


        })
        binding.tvSettings.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvSettings"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvContactUs.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {


                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvContactUs"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.ivTermOfUse.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "ivTermOfUse"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvPrivacyPolicy.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvPrivacyPolicy"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.btnLogout.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                deviceID =
                    preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)


                val progressDialogLogout = ProgressDialog(activity)
                progressDialogLogout.setCancelable(false) // set cancelable to false
                progressDialogLogout.setMessage("Please Wait") // set message
                progressDialogLogout.show() // show progress dialog

                ApiClient.client.signOut("Bearer $token", deviceID, "Android")!!
                    .enqueue(object : Callback<SignOutResponsePOJO?> {
                        override fun onResponse(
                            call: Call<SignOutResponsePOJO?>,
                            response: Response<SignOutResponsePOJO?>
                        ) {
                            progressDialogLogout.dismiss()
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    preferencesManager!!.clear()

                                    preferencesManager!!.setBooleanValue(
                                        Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                                        false
                                    )

                                    val intent =
                                        Intent(activity, LoginActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()

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
                            call: Call<SignOutResponsePOJO?>, t: Throwable
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

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.tvMySession.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "tvMySession"
                requireActivity().startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.helpsupport.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                if (isClickedhelp.equals("No")) {
                    isClickedhelp = "Yes"
                    isClickedLegal = "No"
                    isClicked = "No"

                    binding.imgHelpSupport.setImageResource(R.drawable.up_arrow)
                    binding.imgLegal.setImageResource(R.drawable.down_arrow)

                    binding.llhelpsupprt.visibility = View.VISIBLE
                    binding.llLegal.visibility = View.GONE
                } else {
                    isClicked = "No"
                    isClickedhelp = "No"
                    isClickedLegal = "No"
                    binding.llhelpsupprt.visibility = View.GONE
                    binding.imgHelpSupport.setImageResource(R.drawable.down_arrow)
                    binding.imgHelpSupport.setImageResource(R.drawable.down_arrow)
                    binding.imgLegal.setImageResource(R.drawable.down_arrow)


                }

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.rlLegal.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                if (isClickedLegal.equals("No")) {
                    isClickedLegal = "Yes"
                    isClicked = "No"
                    isClickedhelp = "No"

                    binding.llhelpsupprt!!.visibility = View.GONE
                    binding.llLegal!!.visibility = View.VISIBLE
                    binding.imgLegal!!.setImageResource(R.drawable.up_arrow)
                    binding.imgHelpSupport!!.setImageResource(R.drawable.down_arrow)


                } else {
                    isClicked = "No"
                    isClickedhelp = "No"
                    isClickedLegal = "No"
                    binding.llLegal!!.visibility = View.GONE
                    binding.imgLegal!!.setImageResource(R.drawable.down_arrow)
                    binding.imgHelpSupport!!.setImageResource(R.drawable.down_arrow)
                    binding.imgLegal!!.setImageResource(R.drawable.down_arrow)


                }

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })


    }

   /* @OnClick(R.id.tvEdit)
    fun tvEdit() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "EDITPROFILE"
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.txt_my_downloads)
    fun myDownloadsClick() {

        val intent = Intent(activity, HomeActivity::class.java)
        intent.action = "myDownloads"
        requireActivity().startActivity(intent)

    }

    @OnClick(R.id.tvMyFav)
    fun tvMyFav() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, FavoritesActivity::class.java)
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvReferEarn)
    fun tvReferEarn() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvReferEarn"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.tvRateUs)
    fun tvRateUs() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val uri: Uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    )
                )
            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvNotification)
    fun tvNotification() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvNotification"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvRedeamPoints)
    fun tvRedeamPoints() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setStringValue(Constants.NOT_REWARD, "No Reward")
            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "MyAccountBack"
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvTotalReward)
    fun tvTotalReward() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setStringValue(Constants.NOT_REWARD, "No Reward")
            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "MyAccountBack"
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvFaq)
    fun tvFaq() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {


            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvFaq"
            requireActivity().startActivity(intent)

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvSettings)
    fun tvSettings() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvSettings"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.tvContactUs)
    fun tvContactUs() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {


            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvContactUs"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.ivTermOfUse)
    fun ivTermOfUse() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "ivTermOfUse"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.tvPrivacyPolicy)
    fun tvPrivacyPolicy() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvPrivacyPolicy"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    @OnClick(R.id.btnLogout)
    fun btnLogout() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            deviceID =
                preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)


            val progressDialogLogout = ProgressDialog(activity)
            progressDialogLogout.setCancelable(false) // set cancelable to false
            progressDialogLogout.setMessage("Please Wait") // set message
            progressDialogLogout.show() // show progress dialog

            ApiClient.client.signOut("Bearer $token", deviceID, "Android")!!
                .enqueue(object : Callback<SignOutResponsePOJO?> {
                    override fun onResponse(
                        call: Call<SignOutResponsePOJO?>,
                        response: Response<SignOutResponsePOJO?>
                    ) {
                        progressDialogLogout.dismiss()
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                preferencesManager!!.clear()

                                preferencesManager!!.setBooleanValue(
                                    Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
                                    false
                                )

                                val intent =
                                    Intent(activity, LoginActivity::class.java)
                                startActivity(intent)
                                activity?.finish()

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
                        call: Call<SignOutResponsePOJO?>, t: Throwable
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

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }


    @OnClick(R.id.tvMySession)
    fun tvMySession() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent!!.action = "tvMySession"
            requireActivity().startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.helpsupport)
    fun helpsupport() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (isClickedhelp.equals("No")) {
                isClickedhelp = "Yes"
                isClickedLegal = "No"
                isClicked = "No"

                imgHelpSupport!!.setImageResource(R.drawable.up_arrow)
                imgLegal!!.setImageResource(R.drawable.down_arrow)

                llhelpsupprt!!.visibility = View.VISIBLE
                llLegal!!.visibility = View.GONE
            } else {
                isClicked = "No"
                isClickedhelp = "No"
                isClickedLegal = "No"
                llhelpsupprt!!.visibility = View.GONE
                imgHelpSupport!!.setImageResource(R.drawable.down_arrow)
                imgHelpSupport!!.setImageResource(R.drawable.down_arrow)
                imgLegal!!.setImageResource(R.drawable.down_arrow)


            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }

    @OnClick(R.id.rlLegal)
    fun rlLegal() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (isClickedLegal.equals("No")) {
                isClickedLegal = "Yes"
                isClicked = "No"
                isClickedhelp = "No"

                llhelpsupprt!!.visibility = View.GONE
                llLegal!!.visibility = View.VISIBLE
                imgLegal!!.setImageResource(R.drawable.up_arrow)
                imgHelpSupport!!.setImageResource(R.drawable.down_arrow)


            } else {
                isClicked = "No"
                isClickedhelp = "No"
                isClickedLegal = "No"
                llLegal!!.visibility = View.GONE
                imgLegal!!.setImageResource(R.drawable.down_arrow)
                imgHelpSupport!!.setImageResource(R.drawable.down_arrow)
                imgLegal!!.setImageResource(R.drawable.down_arrow)


            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }
*/

}