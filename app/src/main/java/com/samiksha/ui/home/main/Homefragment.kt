package com.samiksha.ui.home.main

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.FragmentHomeBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.drawer.subscription.CounsellingSessionPaymentActivity
import com.samiksha.ui.home.HomeImplementer
import com.samiksha.ui.home.IHomePresenter
import com.samiksha.ui.home.IHomeView
import com.samiksha.ui.home.categories.CategortAdapter
import com.samiksha.ui.home.categories.CategorySelectedAdapter
import com.samiksha.ui.home.categories.MoreSkillAdapter
import com.samiksha.ui.home.categories.MyCompletedAdapter
import com.samiksha.ui.home.categories.PreferencesAdapter
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.home.pojo.CategoriesResponsePOJO
import com.samiksha.ui.home.pojo.CounsellingResponsePOJO
import com.samiksha.ui.home.pojo.TestimonialsResponsePOJO
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.ui.informationScreen.chooseGoals.ChooseGoalsActivity
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

class Homefragment : Fragment(), PreferencesAdapter.IClickListener,
    CategortAdapter.IClickListener, CategorySelectedAdapter.IClickListener, IHomeView,
    MoreSkillAdapter.IClickListener, MyCompletedAdapter.MyCompleteClickListener,
    PaymentResultWithDataListener {


    private var categortArrayList: List<AllCategoriesResponsePOJO.DataItem> = ArrayList()
    private var preferenceCategortArrayList: List<CategoriesResponsePOJO.DataItem> = ArrayList()
    private var myTrainingCategortArrayList: List<CategoriesResponsePOJO.DataItem> = ArrayList()
    private var moreCategortArrayList: List<CategoriesResponsePOJO.DataItem> = ArrayList()
    private var selectedCategortArrayList: List<CategoriesResponsePOJO.DataItem> = ArrayList()
    private var testoMonialsList: List<TestimonialsResponsePOJO.TestimonialsItem> = ArrayList()


    var recyclerView: RecyclerView? = null
    var recyclerViewpreferences: RecyclerView? = null
    var preferencesAdapter: PreferencesAdapter? = null
    var moreSkillAdapter: MoreSkillAdapter? = null
    var myCompletedAdapter: MyCompletedAdapter? = null
    var categortAdapter: CategortAdapter? = null
    var categorySelectedAdapter: CategorySelectedAdapter? = null
    var testomonialAdapter: TestomonialAdapter? = null

    private var counsellingResponse: ArrayList<CounsellingResponsePOJO.DataItem> = ArrayList()


   /* @JvmField
    @BindView(R.id.llBasedOnPerformance)
    var llBasedOnPerformance: LinearLayout? = null

    @JvmField
    @BindView(R.id.btnBuyNow)
    var btnBuyNow: Button? = null

    @JvmField
    @BindView(R.id.llMoreMentalSkill)
    var llMoreMentalSkill: LinearLayout? = null

    @JvmField
    @BindView(R.id.llMyCompleted)
    var llMyCompleted: LinearLayout? = null

    @JvmField
    @BindView(R.id.recycler_view)
    var recycler_view: RecyclerView? = null

    @JvmField
    @BindView(R.id.moreProgressbar)
    var moreProgressbar: ProgressBar? = null

    @JvmField
    @BindView(R.id.myTrainingProgressbar)
    var myTrainingProgressbar: ProgressBar? = null

    @JvmField
    @BindView(R.id.preferenceProgressbar)
    var preferenceProgressbar: ProgressBar? = null

    @JvmField
    @BindView(R.id.tvNoData)
    var tvNoData: TextView? = null

    @JvmField
    @BindView(R.id.recycler_view_more_modules)
    var recycler_view_more_modules: RecyclerView? = null

    @JvmField
    @BindView(R.id.recycler_view_testomonials)
    var recycler_view_testomonials: RecyclerView? = null

    @JvmField
    @BindView(R.id.recyclerviewMyTraining)
    var recyclerviewMyTraining: RecyclerView? = null

    @JvmField
    @BindView(R.id.ivSort)
    var ivSort: ImageView? = null

    @JvmField
    @BindView(R.id.imgViewInfo)
    var imgViewInfo: ImageView? = null

    @JvmField
    @BindView(R.id.txtDailyMotivation)
    var txtDailyMotivation: TextView? = null

    @JvmField
    @BindView(R.id.txtDailyMotivationWriter)
    var txtDailyMotivationWriter: TextView? = null

    @JvmField
    @BindView(R.id.llBookSession)
    var llBookSession: LinearLayout? = null

      @JvmField
    @BindView(R.id.recycler_view_preferences)
    var recycler_view_preferences: RecyclerView? = null

*/
    var llHomeMain: LinearLayout? = null



    var iHomePresenter: IHomePresenter? = null
    var token: String? = null
    var preferencesManager: PreferencesManager? = null


    var isClicked: Boolean? = false
    var isSelectedCategory: String? = "All Skills"
    var sessionManager: SessionManager? = null

    private var onlyStateList: ArrayList<String>? = ArrayList()
    private var stateList: List<StateResponsePOJO.DataItem> = ArrayList()
    var selectedStateID: Int? = 9999
    var stateName: String? = ""
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val rootView = inflater.inflate(R.layout.fragment_home, container, false)
       // ButterKnife.bind(this, rootView)
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        llHomeMain = binding.root.findViewById(R.id.llHomeMain)
        sessionManager = SessionManager.getInstance(requireActivity())
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        preferencesManager!!.setIntegerValue(Constants.USER_LEARNING_POINT.toString(), 0)
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        if (preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS)
                .equals("Payment-Completed")
        ) {
            binding.llBookSession!!.visibility = View.GONE
        } else {
            binding.llBookSession!!.visibility = View.VISIBLE
        }


        val activity: Activity? = activity

        iHomePresenter = HomeImplementer(this)

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (activity != null) {
                iHomePresenter!!.allTestimonials(token)
                iHomePresenter!!.allcategories(token)
                iHomePresenter!!.allskills(token)
                iHomePresenter!!.state("IN")
            }
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

        setListners()

        return binding.root
    }
    private fun setListners() {
        binding.imgViewInfo.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                showViewInfo()
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
        binding.btnShare.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent.action = "tvReferEarn"
                startActivity(intent)

                /*   var shareIntent = Intent(Intent.ACTION_SEND)
                   shareIntent.type = "text/plain"
                   shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                   var shareMessage = "Join Samiksha\nApp Link "
                   shareMessage =
                       shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                   shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                   startActivity(Intent.createChooser(shareIntent, "Samiksha Referral"))*/

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.btnRateUs.setOnClickListener(View.OnClickListener {
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
        binding.ivSort.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, ChooseGoalsActivity::class.java)
                intent.putExtra("HomeSort", "IsCLicked")
                startActivity(intent)
                requireActivity().finish()
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
        binding.btnBuyNow.setOnClickListener(View.OnClickListener {

            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, CounsellingSessionPaymentActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                /* if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {
                     showAlertState()
                 } else {
                     callPaymentAPI()

                 }*/

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }

        })
    }


 /*   @OnClick(R.id.imgViewInfo)
    fun imgViewInfo() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            showViewInfo()
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }*/

    private fun showViewInfo() {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        val customLayout: View? =
            getLayoutInflater().inflate(R.layout.custom_view_info, null)
        alertDialog.setView(customLayout)

        val txtInfo: TextView = customLayout!!.findViewById(R.id.txtInfo)
        val txtMental: TextView = customLayout!!.findViewById(R.id.txtMental)
        val imgCloseViewInfo: ImageView = customLayout!!.findViewById(R.id.imgCloseViewInfo)
        val tvOK: TextView = customLayout!!.findViewById(R.id.tvOK)

        txtMental.text = "Mental Skills"
        txtInfo.text = "It is highly recommended to complete 6 mental skills in 3 months"
        val alert = alertDialog.create()
        alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 60)
        alert.window!!.setBackgroundDrawable(inset)
        alert.setCanceledOnTouchOutside(true)
        llHomeMain!!.alpha = 0.1f

        alert.show()
        imgCloseViewInfo.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()
        })
        tvOK.setOnClickListener(View.OnClickListener {
            alert.dismiss()
            alert.cancel()
        })

        alert.setOnCancelListener {
            llHomeMain!!.alpha = 1.0f
        }



    }

/*
    @OnClick(R.id.btn_share)
    fun shareApp() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.action = "tvReferEarn"
            startActivity(intent)

         *//*   var shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
            var shareMessage = "Join Samiksha\nApp Link "
            shareMessage =
                shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Samiksha Referral"))*//*

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }*/

/*
    @OnClick(R.id.btnRateUs)
    fun btnRateUs() {
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
*/


 /*   @OnClick(R.id.ivSort)
    fun ivSort() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, ChooseGoalsActivity::class.java)
            intent.putExtra("HomeSort", "IsCLicked")
            startActivity(intent)
            requireActivity().finish()
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }*/


  /*  @OnClick(R.id.btnBuyNow)
    fun btnBuyNow() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, CounsellingSessionPaymentActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            *//* if (sessionManager!!.getUserModel()!!.countryPhoneCode.equals("91")) {
                 showAlertState()
             } else {
                 callPaymentAPI()

             }*//*

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }*/


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

            if (edtState.text.isNotEmpty()) {
                val progressDialogLogout = ProgressDialog(requireActivity())
                progressDialogLogout.setCancelable(false) // set cancelable to false
                progressDialogLogout.setMessage("Please Wait") // set message
                progressDialogLogout.show() // show progress dialog


                ApiClient.client.updateUserState("Bearer $token", selectedStateID)!!
                    .enqueue(object :
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

                                callPaymentAPI()

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

            } else {
                Toast.makeText(
                    activity,
                    "Please select state",
                    Toast.LENGTH_LONG
                ).show()

            }


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


    private fun callPaymentAPI() {
        val progressDialogLogout = ProgressDialog(activity)
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
                        if (response!!.body()!!.counsellingsessionlist!!.data!!.isNotEmpty()) {
                            counsellingResponse =
                                response.body()?.counsellingsessionlist?.data as ArrayList<CounsellingResponsePOJO.DataItem>
                            startPayment(response)

                        } else {
                            Toast.makeText(
                                activity,
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
                call: Call<CounsellingResponsePOJO?>, t: Throwable
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


    override fun clickMoreSkill(model: CategoriesResponsePOJO.DataItem) {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (model.type.equals("Coming soon")) {

            } else {


                val intent = Intent(activity, DealingWithDistractionsActivity::class.java)
                intent.putExtra("skill_id", model.id.toString())

                startActivity(intent)

            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }


    }

    override fun onFavoriteClick(model: CategoriesResponsePOJO.DataItem) {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (model.isFavorite && model.favoriteId != null) {
                iHomePresenter!!.deleteFromFavorites(token, model.favoriteId.toString())
            } else {
                iHomePresenter!!.addToFavorites(token, "mental_skill", model.id.toString())
            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }

    }


    override fun categoryClick(value: String?, position: Int) {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            binding.imgViewInfo.visibility = View.VISIBLE

            if (position == 0) {

                binding.tvPreference.text = "Recommended for you"
                binding.tvPreference.visibility = View.VISIBLE
                binding.tvCategories.visibility = View.VISIBLE

                iHomePresenter!!.allskills(token)

            } else if (position == 1) {

                isSelectedCategory = value
                binding.tvPreference.text = "Recommended for you"
                binding.tvPreference.visibility = View.VISIBLE
                binding.tvCategories.visibility = View.VISIBLE
                isClicked = false

                iHomePresenter!!.yogaSelected(token, value)

            } else if (value == "Yoga") {
                binding.imgViewInfo.visibility = View.GONE

                isSelectedCategory = value

                binding.tvPreference.text = "Meditation exercises for you"

                binding.tvPreference.visibility = View.VISIBLE
                binding.tvCategories.visibility = View.VISIBLE

                iHomePresenter!!.yogaSelected(token, value)

            } else {

                isSelectedCategory = value


                binding.tvPreference.text = "Recommended for you"

                binding.tvPreference.visibility = View.VISIBLE
                binding.tvCategories.visibility = View.VISIBLE
                iHomePresenter!!.selectedCategories(token, value)
            }

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }

    override fun CategorySelected() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val intent = Intent(activity, DealingWithDistractionsActivity::class.java)
            startActivity(intent)
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }


    override fun categoriesSuccess(allCategoriesResponsePOJO: AllCategoriesResponsePOJO?) {

        if (allCategoriesResponsePOJO != null) {
            sessionManager?.setAllCategories(allCategoriesResponsePOJO)
        }

        //   SessionManager.getInstance(requireContext()).setSkillDetailModel(model)




        categortArrayList = allCategoriesResponsePOJO!!.data!!

        preferencesManager!!.setcategoryList(categortArrayList)

        binding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        if (activity != null) {
            categortAdapter = CategortAdapter(requireActivity(), categortArrayList!!)
            binding.recyclerView.adapter = categortAdapter
            categortAdapter!!.setClickListener(this)
        }


    }

    override fun allSkillSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?) {

        ProjectUtils.dismissProgressDialog()
        preferenceCategortArrayList = categoriesResponsePOJO!!.mentalSkills!!.data!!
        myTrainingCategortArrayList = categoriesResponsePOJO!!.myActivityList!!.data!!
        moreCategortArrayList = categoriesResponsePOJO!!.moreSkills!!.data!!
        selectedCategortArrayList = categoriesResponsePOJO!!.mentalSkills!!.data!!


        if (isClicked == false) {
            preferenceHorizontal(preferenceCategortArrayList)
            if (preferenceCategortArrayList.size > 0) {
                binding.recyclerViewPreferences.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                binding.llBasedOnPerformance.visibility = View.VISIBLE


            } else {
                binding.recyclerViewPreferences.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.llBasedOnPerformance.visibility = View.GONE
            }
        }

        if (moreCategortArrayList.isNotEmpty()) {
            binding.llMoreMentalSkill.visibility = View.VISIBLE
            moreHorizontal(moreCategortArrayList)

        } else {
            binding.llMoreMentalSkill.visibility = View.GONE
            moreHorizontal(moreCategortArrayList)

        }



        if (myTrainingCategortArrayList.isNotEmpty()) {
            binding.llMyCompleted.visibility = View.VISIBLE
            myTrainingHorizontal(myTrainingCategortArrayList)

        } else {
            binding.llMyCompleted.visibility = View.GONE
            myTrainingHorizontal(myTrainingCategortArrayList)

        }


    }

    override fun selectedCategoriesSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?) {
        moreCategortArrayList = categoriesResponsePOJO!!.moreSkills!!.data!!
        selectedCategortArrayList = categoriesResponsePOJO!!.mentalSkills!!.data!!
        myTrainingCategortArrayList = categoriesResponsePOJO!!.myActivityList!!.data!!

        if (selectedCategortArrayList.size > 0) {
            binding.tvNoData.visibility = View.GONE
            binding.llBasedOnPerformance.visibility = View.VISIBLE
            binding.recyclerViewPreferences.visibility = View.VISIBLE
            preferenceHorizontal(selectedCategortArrayList)
        } else {
            binding.llBasedOnPerformance.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerViewPreferences.visibility = View.GONE
        }





        if (moreCategortArrayList.isNotEmpty()) {
            binding.llMoreMentalSkill.visibility = View.VISIBLE
            moreHorizontal(moreCategortArrayList)

        } else {
            binding.llMoreMentalSkill.visibility = View.GONE
            moreHorizontal(moreCategortArrayList)

        }



        if (myTrainingCategortArrayList.isNotEmpty()) {
            binding.llMyCompleted.visibility = View.VISIBLE
            myTrainingHorizontal(myTrainingCategortArrayList)

        } else {
            binding.llMyCompleted.visibility = View.GONE
            myTrainingHorizontal(myTrainingCategortArrayList)

        }

    }


    override fun yogaSuccess(categoriesResponsePOJO: CategoriesResponsePOJO?) {
        // txtDailyMotivation!!.text = "allCategoriesResponsePOJO!!.dailyMotivation!!.motivation"
        // txtDailyMotivationWriter!!.text = "KP"


        moreCategortArrayList = categoriesResponsePOJO!!.moreSkills!!.data!!
        selectedCategortArrayList = categoriesResponsePOJO!!.mentalSkills!!.data!!
        myTrainingCategortArrayList = categoriesResponsePOJO!!.myActivityList!!.data!!

        if (selectedCategortArrayList.size > 0) {
            binding.llBasedOnPerformance.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
            binding.recyclerViewPreferences.visibility = View.VISIBLE
            preferenceHorizontal(selectedCategortArrayList)
        } else {
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerViewPreferences.visibility = View.GONE
            binding.llBasedOnPerformance.visibility = View.GONE

        }





        if (moreCategortArrayList.isNotEmpty()) {
            binding.llMoreMentalSkill.visibility = View.VISIBLE
            moreHorizontal(moreCategortArrayList)

        } else {
            binding.llMoreMentalSkill.visibility = View.GONE
            moreHorizontal(moreCategortArrayList)

        }



        if (myTrainingCategortArrayList.isNotEmpty()) {
            binding.llMyCompleted.visibility = View.VISIBLE
            myTrainingHorizontal(myTrainingCategortArrayList)

        } else {
            binding.llMyCompleted.visibility = View.GONE
            myTrainingHorizontal(myTrainingCategortArrayList)

        }


    }

    override fun testimonialsSuccess(testimonialsResponsePOJO: TestimonialsResponsePOJO?) {

        testoMonialsList = testimonialsResponsePOJO!!.testimonials!!

        binding.recyclerViewTestomonials.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        binding.recyclerViewTestomonials.itemAnimator = DefaultItemAnimator()

        if (activity != null) {
            testomonialAdapter = TestomonialAdapter(requireActivity(), testoMonialsList)
            binding.recyclerViewTestomonials.adapter = testomonialAdapter
        }

    }

    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        saveSkillDetailInSession(skillDetailsResponsePOJO!!)
    }

    override fun addFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        //  Toast.makeText(activity, skillDetailsResponsePOJO?.message, Toast.LENGTH_LONG).show()
        if (isSelectedCategory.equals("Yoga") || isSelectedCategory.equals("Basic Skill")) {
            iHomePresenter!!.yogaSelected(token, isSelectedCategory)

        } else if (isSelectedCategory.equals("All Skills")) {
            iHomePresenter!!.allskills(token)

        } else {

            iHomePresenter!!.selectedCategories(token, isSelectedCategory)

        }
    }

    override fun deleteFromFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        // Toast.makeText(activity, skillDetailsResponsePOJO?.message, Toast.LENGTH_LONG).show()
        if (isSelectedCategory.equals("Yoga") || isSelectedCategory.equals("Basic Skill")) {
            iHomePresenter!!.yogaSelected(token, isSelectedCategory)

        } else if (isSelectedCategory.equals("All Skills")) {
            iHomePresenter!!.allskills(token)


        } else {
            iHomePresenter!!.selectedCategories(token, isSelectedCategory)

        }

    }

    override fun onStateSuccess(stateResponcePOJO: StateResponsePOJO?) {
        onlyStateList!!.clear()

        for (i in stateResponcePOJO!!.data!!.indices) {
            onlyStateList!!.add(stateResponcePOJO!!.data!![i].name!!)
        }

        stateList = stateResponcePOJO!!.data!!

    }

    private fun saveSkillDetailInSession(skillDetailsResponsePOJO: SkillDetailsResponsePOJO) {
        if (skillDetailsResponsePOJO.data?.subSkiils != null && skillDetailsResponsePOJO?.data?.subSkiils?.size!! > 0) {
            var model: SkillDetailModel = SkillDetailModel()
            model.skillDetail = skillDetailsResponsePOJO
            if (skillDetailsResponsePOJO.data?.subSkiils!![0].data!!.isNotEmpty()) {
                model.learningFirstQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(0)
                model.learningVideoModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(1)
                model.learningSecondQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(2)
                model.learningThirdQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![0].data?.get(3)
            }

            if (skillDetailsResponsePOJO.data?.subSkiils?.size!! > 1 && skillDetailsResponsePOJO.data?.subSkiils!![1].data!!.isNotEmpty() && skillDetailsResponsePOJO.data?.subSkiils!![1].data?.size!! > 2) {
                model.trainingAudioModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(0)
                model.trainingQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(1)
                model.trainingSecondQuestionModel =
                    skillDetailsResponsePOJO.data?.subSkiils!![1].data?.get(2)
            }

            SessionManager.getInstance(requireContext()).setSkillDetailModel(model)

            if (skillDetailsResponsePOJO!!.data!!.isTrainingComplete == true) {
                model.isFromTrainingPath = true
            } else {
                model.isFromTrainingPath = false

            }

            if (model.trainingAudioModel != null && model.trainingAudioModel?.file != null) {
                val intent = Intent(activity, TrainingStartActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Data not available for this skill", Toast.LENGTH_SHORT)
                    .show()
            }


        }

    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(activity)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        ProjectUtils.dismissProgressDialog()
        if (isVisible) {
            Toast.makeText(context, appErrorMessage, Toast.LENGTH_LONG).show()
        }

    }



    private fun moreHorizontal(categortArrayListMore: List<CategoriesResponsePOJO.DataItem>) {
        binding.recyclerViewMoreModules.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewMoreModules.itemAnimator = DefaultItemAnimator()

        if (activity != null) {
            moreSkillAdapter = MoreSkillAdapter(requireActivity(), categortArrayListMore)
            binding.recyclerViewMoreModules.adapter = moreSkillAdapter
            moreSkillAdapter!!.setClickListener(this)

            binding.moreProgressbar.visibility = View.GONE
        }

    }

    private fun myTrainingHorizontal(categortArrayListMore: List<CategoriesResponsePOJO.DataItem>) {
        binding.recyclerviewMyTraining.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerviewMyTraining.itemAnimator = DefaultItemAnimator()

        if (activity != null) {
            myCompletedAdapter = MyCompletedAdapter(requireActivity(), categortArrayListMore)
            binding.recyclerviewMyTraining.adapter = myCompletedAdapter
            myCompletedAdapter!!.setClickListener(this)

            binding.myTrainingProgressbar.visibility = View.GONE
        }

    }


    private fun preferenceHorizontal(CategortArrayListpreference: List<CategoriesResponsePOJO.DataItem>) {
        binding.recyclerViewPreferences.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewPreferences.itemAnimator = DefaultItemAnimator()


        if (activity != null) {
            preferencesAdapter = PreferencesAdapter(requireActivity(), CategortArrayListpreference)
            binding.recyclerViewPreferences.adapter = preferencesAdapter
            preferencesAdapter!!.setClickListener(this)
            binding.preferenceProgressbar.visibility = View.GONE
        }

    }

    override fun myCompleteclick(model: CategoriesResponsePOJO.DataItem) {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            if (model.type.equals("Coming soon")) {

            } else {

                if (model.moduleType.equals("Yoga", ignoreCase = true)) {
                    iHomePresenter?.skillDetails(token, model.id.toString())
                } else {

                    val intent = Intent(context, DealingWithDistractionsActivity::class.java)
                    intent.putExtra("skill_id", model.id.toString())

                    startActivity(intent)
                }
            }
        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }
    }


    private fun startPayment(responceBody: Response<CounsellingResponsePOJO?>) {
        /*
                 You need to pass current activity in order to let Razorpay create CheckoutActivity
                */
        val activity: Activity = requireActivity()
        val checkout = Checkout()
        checkout.setKeyID(Constants.RAZOR_PAY_KEY)
        try {
            val options = JSONObject()
            options.put("name", SessionManager.getInstance(activity).getUserModel()?.name)
            options.put("description", "Demoing Charges")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put(
                "amount",
                (((responceBody.body()?.counsellingsessionlist?.data?.get(0)?.regularPrice).toString()).toDouble()
                    ?.times(100)).toString()
            )


            val preFill = JSONObject()
            preFill.put("email", "test@razorpay.com")
            preFill.put("contact", SessionManager.getInstance(activity).getUserModel()?.phoneNo)
            options.put("prefill", preFill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(code: Int, response: String?, p2: PaymentData?) {
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

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {

        try {
            fireEvent()
            //  Toast.makeText(this, "Payment Success: " +  paymentData, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception in onPaymentSuccess: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fireEvent() {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())


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
    }

}