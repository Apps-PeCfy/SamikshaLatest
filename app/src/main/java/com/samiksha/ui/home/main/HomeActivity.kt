package com.samiksha.ui.home.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.database.LocalCrudRepository
import com.samiksha.databinding.ActivityHomeBinding
import com.samiksha.networking.ApiClient
import com.samiksha.ui.downloads.DownloadsFragment
import com.samiksha.ui.drawer.contactus.ContactUs
import com.samiksha.ui.drawer.faqs.FAQs
import com.samiksha.ui.drawer.mypoints.MyPoints
import com.samiksha.ui.drawer.mysession.MySessions
import com.samiksha.ui.drawer.notification.NotificationFragment
import com.samiksha.ui.drawer.pojo.DrawerAdapter
import com.samiksha.ui.drawer.pojo.DrawerPojo
import com.samiksha.ui.drawer.privacypolicy.PrivacyPolicy
import com.samiksha.ui.drawer.readempoints.RedeemPoints
import com.samiksha.ui.drawer.readempoints.RewardFragment
import com.samiksha.ui.drawer.referAndEarn.ReferAndEarn
import com.samiksha.ui.drawer.settings.Setting
import com.samiksha.ui.drawer.subscription.Subscription
import com.samiksha.ui.drawer.subscription.SubscriptionProcessFragment
import com.samiksha.ui.drawer.termofuse.TermsOfUse
import com.samiksha.ui.drawer.website.WebSite
import com.samiksha.ui.home.pojo.AppVersionPOJO
import com.samiksha.ui.home.pojo.SignOutResponsePOJO
import com.samiksha.ui.informationScreen.chooseGoals.ChooseGoalsActivity
import com.samiksha.ui.login.LoginActivity
import com.samiksha.ui.otp.pojo.CheckOtpResponsePOJO
import com.samiksha.ui.register.pojo.ValidationResponsePOJO
import com.samiksha.ui.search.SearchActivity
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.ui.sportPsychology.session.OnlyMessageResponsePOJO
import com.samiksha.utils.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class HomeActivity : AppCompatActivity(), AdapterView.OnItemClickListener,
    PaymentResultWithDataListener {

  /*  @JvmField
    @BindView(R.id.toolbar_home)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.lvDrawer)
    var lvDrawer: ListView? = null

    @JvmField
    @BindView(R.id.tvConnection)
    var tvConnection: TextView? = null

    @JvmField
    @BindView(R.id.tvCommunity)
    var tvCommunity: TextView? = null

    @JvmField
    @BindView(R.id.tvEvent)
    var tvEvent: TextView? = null

    @JvmField
    @BindView(R.id.tvProgress)
    var tvProgress: TextView? = null

    @JvmField
    @BindView(R.id.ivSearch)
    var ivSearch: ImageView? = null

    @JvmField
    @BindView(R.id.ivSwap)
    var ivSwap: ImageView? = null


    @JvmField
    @BindView(R.id.ivSort)
    var ivSort: ImageView? = null

    @JvmField
    @BindView(R.id.userProfilePic)
    var userProfilePic: RoundedImageView? = null

    @JvmField
    @BindView(R.id.tvTotalReward)
    var tvTotalReward: TextView? = null

    @JvmField
    @BindView(R.id.tvSubscriptionMonth)
    var tvSubscriptionMonth: TextView? = null

    @JvmField
    @BindView(R.id.tvUserName)
    var tvUserName: TextView? = null

    @JvmField
    @BindView(R.id.bottomLayout)
    var bottomLayout: LinearLayout? = null


    @JvmField
    @BindView(R.id.drawer_layout)
    var mDrawerLayout: DrawerLayout? = null

*/
    private var drawerPojoArrayList: ArrayList<DrawerPojo>? = null
    private var drawerAdapter: DrawerAdapter? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    private var sessionManager: SessionManager? = null


    private var fragmentManager: FragmentManager? = null
    private var fragment: Fragment? = null
    private var fragmentTransaction: FragmentTransaction? = null
    var preferencesManager: PreferencesManager? = null

    var deviceID: String? = null
    var token: String? = null
    var orderID: Int? = 0
    var appSignatureHelper: AppSignatureHelper? = null

    private var localCrudRepository: LocalCrudRepository? = null
    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_home)
       // ButterKnife.bind(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarGradiant(this)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val action = intent.action

        if (action.equals("myDownloads")) {
            init()

        } else {
            if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

                init()

            } else {
                ProjectUtils.showToast(
                    this@HomeActivity,
                    getString(R.string.no_internet_connection)
                )
            }
        }


    }

    private fun init() {
        sessionManager = SessionManager.getInstance(this)
        PreferencesManager.initializeInstance(this@HomeActivity)
        preferencesManager = PreferencesManager.instance
        localCrudRepository = LocalCrudRepository.getInstance(this@HomeActivity)


        initView()
        setListData()
        setListeners()


        val action = intent.action
        if (action == "MyAccount") {

            binding.tvConnection.setTextColor(Color.parseColor("#ffffff"))
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.title = "Profile"
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceProfileFragment(MyAccountFragment())

        } else if (action == "MyPoints") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap?.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout!!.closeDrawer(Gravity.LEFT)
            replaceFragment(RedeemPoints())

        } else if (action == "MyAccountBack") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap?.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout!!.closeDrawer(Gravity.LEFT)
            replaceFragment(MyPoints())

        } else if (action == "TrainingPath") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.toolbarHome.title = "Training Path"
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(TrainingPathFragment())

        } else if (action == "EDITPROFILE") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.toolbarHome.title = "Edit Profile"
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(AccountFragment())

        } else if (action == "tvAboutSamiksha") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(WebSite())

        } else if (action == "tvSubscription") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(Subscription())

        } else if (action == "tvMySession") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(MySessions())

        } else if (action == "tvReferEarn") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(ReferAndEarn())

        } else if (action == "tvFaq") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(FAQs())

        } else if (action == "tvNotification") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(NotificationFragment())

        } else if (action == "tvSettings") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(Setting())

        } else if (action == "tvContactUs") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(ContactUs())

        } else if (action == "ivTermOfUse") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(TermsOfUse())

        } else if (action == "tvPrivacyPolicy") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(PrivacyPolicy())

        } else if (action == "RewardFragment") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(RewardFragment())

        } else if (action == "SubscriptionProcess") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(SubscriptionProcessFragment())

        } else if (action == "myDownloads") {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE

            binding.toolbarHome.title = "My Downloads"
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceProfileFragment(DownloadsFragment())

        } else {
            //replaceFragment(Homefragment())
            checkUserType()
            if (fragment is Homefragment) {
                return
            } else {
                fragment = Homefragment()
                replaceFragment()
            }

        }






        /* appSignatureHelper = AppSignatureHelper(this)
         appSignatureHelper!!.getAppSignatures()*/
    }

    private fun setListeners() {
        binding.ivSwap.setOnClickListener { switchUser() }
        binding.btnLogout.setOnClickListener {
            deviceID = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
            token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


            val progressDialogLogout = ProgressDialog(this@HomeActivity)
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
                                //    Toast.makeText(this@HomeActivity, response!!.body()!!.message, Toast.LENGTH_LONG).show()
                                val intent =
                                    Intent(this@HomeActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

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
                                    this@HomeActivity,
                                    pojo.errors!!.get(0).message,
                                    Toast.LENGTH_LONG
                                ).show()


                            } catch (exception: IOException) {
                            }

                        } else {
                            Toast.makeText(
                                this@HomeActivity,
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
                            this@HomeActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
                })


        }
        binding.btnAccount.setOnClickListener {
            binding.tvConnection.setTextColor(Color.parseColor("#ffffff"))
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))
            binding.toolbarHome.navigationIcon = null
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            //replaceFragment(MyAccountFragment())

            if (fragment is MyAccountFragment) {
                return@setOnClickListener
            } else {
                binding.toolbarHome.title = "Profile"
                fragment = MyAccountFragment()
                replaceProfileFragment()
            }

        }
        binding.btnModules.setOnClickListener {
            binding.tvConnection.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))


            binding.toolbarHome.navigationIcon = null
            binding.ivSearch.visibility = View.VISIBLE
            binding.ivSort.visibility = View.GONE
            checkUserType()
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            //replaceFragment(Homefragment())


            if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

                if (fragment is Homefragment) {
                    return@setOnClickListener
                } else {
                    binding.toolbarHome.title = "Mental Skills"
                    fragment = Homefragment()
                    replaceFragment()
                }
            } else {
                ProjectUtils.showToast(
                    this@HomeActivity,
                    getString(R.string.no_internet_connection)
                )

            }
        }
        binding.btnProgress.setOnClickListener {
            binding.tvConnection.setTextColor(Color.parseColor("#ffffff"))
            binding.tvCommunity.setTextColor(Color.parseColor("#ffffff"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#FC6D2D"))
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.toolbarHome.navigationIcon = null
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            //replaceFragment(ProgressFragment())

            if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

                if (fragment is ProgressFragment) {
                    return@setOnClickListener
                } else {
                    binding.toolbarHome.title = "Progress"
                    fragment = ProgressFragment()
                    replaceFragment()
                }
            } else {
                ProjectUtils.showToast(
                    this@HomeActivity,
                    getString(R.string.no_internet_connection)
                )

            }

        }
        binding.tvTotalReward.setOnClickListener {
            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.toolbarHome?.title = "Reward Points"
            binding.bottomLayout.visibility = View.GONE
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            replaceFragment(MyPoints())
        }
        binding.btnTrainingPath.setOnClickListener {
            binding.tvConnection.setTextColor(Color.parseColor("#ffffff"))
            binding.tvCommunity.setTextColor(Color.parseColor("#FC6D2D"))
            binding.tvEvent.setTextColor(Color.parseColor("#ffffff"))
            binding.tvProgress.setTextColor(Color.parseColor("#ffffff"))

            binding.ivSearch.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.ivSwap.visibility = View.GONE
            binding.toolbarHome.navigationIcon = null
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            //replaceFragment(TrainingPathFragment())


            if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

                if (fragment is TrainingPathFragment) {
                    return@setOnClickListener
                } else {
                    binding.toolbarHome.title = "Training Path"
                    fragment = TrainingPathFragment()
                    replaceFragment()
                }

            } else {
                ProjectUtils.showToast(
                    this@HomeActivity,
                    getString(R.string.no_internet_connection)
                )

            }

        }
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

        }
        binding.ivSort.setOnClickListener {
            val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)
            intent.putExtra("HomeSort", "IsCLicked")
            startActivity(intent)
            finish()

        }
    }

    private fun setStatusBarGradiant(activity: HomeActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.shape_roundedbtn)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }


    private fun setListData() {


        drawerPojoArrayList = ArrayList<DrawerPojo>()
        /*drawerPojoArrayList!!.add(
            DrawerPojo(
                0,
                this.getResources().getString(R.string.my_Training),
                R.drawable.nav_training
            )
        )*/


        drawerPojoArrayList!!.add(
            DrawerPojo(
                0,
                this.getResources().getString(R.string.my_Points),
                R.drawable.nav_my_points
            )
        )
        /* drawerPojoArrayList!!.add(
             DrawerPojo(
                 2,
                 this.getResources().getString (R.string.my_Favorites),
                 R.drawable.nav_my_favorites
             )
         )
        */ drawerPojoArrayList!!.add(
            DrawerPojo(
                1,
                this.getResources().getString(R.string.referandEarn),
                R.drawable.nav_refer_earn
            )
        )

        drawerPojoArrayList!!.add(
            DrawerPojo(
                2,
                this.getResources().getString(R.string.FAQ),
                R.drawable.nav_faqs
            )
        )

        drawerPojoArrayList!!.add(
            DrawerPojo(
                3,
                this.getResources().getString(R.string.Contactus),
                R.drawable.nav_contact_us
            )
        )


        drawerPojoArrayList!!.add(
            DrawerPojo(
                4,
                this.getResources().getString(R.string.Settings),
                R.drawable.nav_settings
            )
        )

        drawerPojoArrayList!!.add(
            DrawerPojo(
                5,
                this.getResources().getString(R.string.privacy_Policy),
                R.drawable.nav_privacy_policy
            )
        )

        drawerPojoArrayList!!.add(
            DrawerPojo(
                6,
                this.getResources().getString(R.string.termsofUse),
                R.drawable.nav_terms_of_use
            )
        )


        drawerPojoArrayList!!.add(
            DrawerPojo(
                7,
                this.getResources().getString(R.string.gotoWebsite),
                R.drawable.nav_go_to_web
            )
        )


        drawerAdapter =
            DrawerAdapter(this, drawerPojoArrayList!!)
        binding.lvDrawer.adapter = drawerAdapter

    }

    private fun initView() {
        binding.lvDrawer.onItemClickListener = this
        binding.toolbarHome!!.title = "Mental Skills"
        binding.toolbarHome.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.toolbarHome)
        binding.toolbarHome.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)


        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)

        orderID = preferencesManager!!.getIntegerValue(Constants.CHECK_ORDER_ID.toString())
        if(orderID!!>0){
            checkOrederStatus(orderID)
        }

        /*drawerToggle = object : ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar,
            R.string.drawer_open, R.string.drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        mDrawerLayout?.addDrawerListener(drawerToggle as ActionBarDrawerToggle)!!
        drawerToggle?.setDrawerIndicatorEnabled(true)
        drawerToggle?.setDrawerIndicatorEnabled(false)
        // drawerToggle?.setHomeAsUpIndicator(R.drawable.ic_action_menu)
        drawerToggle?.syncState()
        drawerToggle?.setToolbarNavigationClickListener(View.OnClickListener {
            if (mDrawerLayout!!.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout!!.closeDrawer(Gravity.LEFT)
            } else {


                tvTotalReward!!.text =
                    "Reward Points earned " + sessionManager?.getUserModel()?.totalRewards
                tvUserName?.text =
                    sessionManager?.getUserModel()?.firstName + " " + sessionManager?.getUserModel()?.lastName

                if (sessionManager?.getUserModel()?.subscription == null || sessionManager?.getUserModel()?.subscription?.name?.isEmpty()!!) {
                    tvSubscriptionMonth?.text = "Not subscribed yet"
                } else {
                    tvSubscriptionMonth?.text = sessionManager?.getUserModel()?.subscription?.name
                }


                Glide.with(this@HomeActivity!!)
                    .load(preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC))
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform()
                    ).into(userProfilePic!!)


                mDrawerLayout!!.openDrawer(Gravity.LEFT)
            }
        })*/


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


                }
            }

            override fun onFailure(
                call: Call<OnlyMessageResponsePOJO?>, t: Throwable
            ) {
                Toast.makeText(
                    this@HomeActivity,
                    t.message,
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        })

    }


    override fun onBackPressed() {
        if (binding.toolbarHome.title != "Mental Skills" && binding.toolbarHome.title != "Training Path" && binding.toolbarHome.title != "Progress" && binding.toolbarHome.title != "Profile") {
            super.onBackPressed()
        } else {
            ProjectUtils.showAlertDialog(this)
        }

    }

/*
    @OnClick(R.id.ivSort)
    fun ivSort() {
        val intent = Intent(applicationContext, ChooseGoalsActivity::class.java)
        intent.putExtra("HomeSort", "IsCLicked")
        startActivity(intent)
        finish()


    }

    @OnClick(R.id.ivSearch)
    fun ivSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.btnTrainingPath)
    fun btnTrainingPath() {

        tvConnection!!.setTextColor(Color.parseColor("#ffffff"))
        tvCommunity!!.setTextColor(Color.parseColor("#FC6D2D"))
        tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
        tvProgress!!.setTextColor(Color.parseColor("#ffffff"))

        ivSearch?.visibility = View.GONE
        ivSort?.visibility = View.GONE
        ivSwap?.visibility = View.GONE
        toolbar!!.navigationIcon = null
        mDrawerLayout!!.closeDrawer(Gravity.LEFT)
        //replaceFragment(TrainingPathFragment())


        if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

            if (fragment is TrainingPathFragment) {
                return
            } else {
                toolbar!!.title = "Training Path"
                fragment = TrainingPathFragment()
                replaceFragment()
            }

        } else {
            ProjectUtils.showToast(
                this@HomeActivity,
                getString(R.string.no_internet_connection)
            )

        }


    }

    @OnClick(R.id.tvTotalReward)
    fun tvTotalReward() {
        ivSearch?.visibility = View.GONE
        ivSort?.visibility = View.GONE
        ivSwap?.visibility = View.GONE
        toolbar?.title = "Reward Points"
        bottomLayout?.visibility = View.GONE
        mDrawerLayout?.closeDrawer(Gravity.LEFT)
        replaceFragment(MyPoints())


    }

    @OnClick(R.id.btnProgress)
    fun btnProgress() {
        tvConnection!!.setTextColor(Color.parseColor("#ffffff"))
        tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
        tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
        tvProgress!!.setTextColor(Color.parseColor("#FC6D2D"))
        ivSearch?.visibility = View.GONE
        ivSort?.visibility = View.GONE
        ivSwap?.visibility = View.GONE
        toolbar!!.navigationIcon = null
        mDrawerLayout!!.closeDrawer(Gravity.LEFT)
        //replaceFragment(ProgressFragment())

        if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

            if (fragment is ProgressFragment) {
                return
            } else {
                toolbar!!.title = "Progress"
                fragment = ProgressFragment()
                replaceFragment()
            }
        } else {
            ProjectUtils.showToast(
                this@HomeActivity,
                getString(R.string.no_internet_connection)
            )

        }


    }

    @OnClick(R.id.btnModules)
    fun btnModules() {
        tvConnection!!.setTextColor(Color.parseColor("#FC6D2D"))
        tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
        tvEvent!!.setTextColor(Color.parseColor("#ffffff"))
        tvProgress!!.setTextColor(Color.parseColor("#ffffff"))


        toolbar!!.navigationIcon = null
        ivSearch?.visibility = View.VISIBLE
        ivSort?.visibility = View.GONE
        checkUserType()
        mDrawerLayout!!.closeDrawer(Gravity.LEFT)
        //replaceFragment(Homefragment())


        if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

            if (fragment is Homefragment) {
                return
            } else {
                toolbar!!.title = "Mental Skills"
                fragment = Homefragment()
                replaceFragment()
            }
        } else {
            ProjectUtils.showToast(
                this@HomeActivity,
                getString(R.string.no_internet_connection)
            )

        }


    }*/

    /* @OnClick(R.id.btnAccount)
     fun btnAccount() {
         toolbar?.title = "Account"
         ivSearch?.visibility = View.GONE
         ivSort?.visibility = View.GONE
         mDrawerLayout!!.closeDrawer(Gravity.LEFT)
         replaceFragment(AccountFragment())

     }
    */

    /*@OnClick(R.id.btnAccount)
    fun btnAccount() {
        tvConnection!!.setTextColor(Color.parseColor("#ffffff"))
        tvCommunity!!.setTextColor(Color.parseColor("#ffffff"))
        tvEvent!!.setTextColor(Color.parseColor("#FC6D2D"))
        tvProgress!!.setTextColor(Color.parseColor("#ffffff"))
        toolbar!!.navigationIcon = null
        ivSearch?.visibility = View.GONE
        ivSort?.visibility = View.GONE
        ivSwap?.visibility = View.GONE
        mDrawerLayout!!.closeDrawer(Gravity.LEFT)
        //replaceFragment(MyAccountFragment())

        if (fragment is MyAccountFragment) {
            return
        } else {
            toolbar?.title = "Profile"
            fragment = MyAccountFragment()
            replaceProfileFragment()
        }


    }
*/
    private fun replaceProfileFragment() {

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.container_framelayout, fragment!!)
        fragmentTransaction?.commit()

    }

    fun replaceProfileFragment(fragment: Fragment?) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.container_framelayout, fragment!!)
        fragmentTransaction!!.commit()

    }


    fun replaceFragment(fragment: Fragment?) {
        if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction!!.replace(R.id.container_framelayout, fragment!!)
            fragmentTransaction!!.commit()
        } else {
            ProjectUtils.showToast(
                this@HomeActivity,
                getString(R.string.no_internet_connection)
            )

        }

    }

    private fun replaceFragment() {
        if (ProjectUtils.checkInternetAvailable(this@HomeActivity)!!) {

            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container_framelayout, fragment!!)
            fragmentTransaction?.commit()

        } else {
            ProjectUtils.showToast(
                this@HomeActivity,
                getString(R.string.no_internet_connection)
            )

        }
    }

    @SuppressLint("WrongConstant")
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {


            0 -> {
                binding.bottomLayout.visibility = View.GONE
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap?.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(MyPoints())


            }


            1 -> {

                binding.bottomLayout.visibility = View.GONE
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(ReferAndEarn())

            }

            2 -> {
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(FAQs())


            }

            3 -> {
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(ContactUs())


            }

            4 -> {
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(Setting())


            }


            5 -> {
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)
                replaceFragment(PrivacyPolicy())


            }

            6 -> {
                binding.ivSearch.visibility = View.GONE
                binding.ivSort.visibility = View.GONE
                binding.ivSwap.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.drawerLayout.closeDrawer(Gravity.START)

                replaceFragment(TermsOfUse())


            }

            else -> {
            }
        }
    }


    /*  @OnClick(R.id.btnLogout)
      fun logOut() {


          deviceID = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
          token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


          val progressDialogLogout = ProgressDialog(this@HomeActivity)
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
                              //    Toast.makeText(this@HomeActivity, response!!.body()!!.message, Toast.LENGTH_LONG).show()
                              val intent =
                                  Intent(this@HomeActivity, LoginActivity::class.java)
                              startActivity(intent)
                              finish()

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
                                  this@HomeActivity,
                                  pojo.errors!!.get(0).message,
                                  Toast.LENGTH_LONG
                              ).show()


                          } catch (exception: IOException) {
                          }

                      } else {
                          Toast.makeText(
                              this@HomeActivity,
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
                          this@HomeActivity,
                          t.message,
                          Toast.LENGTH_LONG
                      )
                          .show()

                  }
              })

      }*/

    private fun switchUser() {

        deviceID = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_DEVICEID)
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        val progressDialogLogout = ProgressDialog(this@HomeActivity)
        progressDialogLogout.setCancelable(false) // set cancelable to false
        progressDialogLogout.setMessage("Please Wait") // set message
        progressDialogLogout.show() // show progress dialog

        ApiClient.client.switchUser(
            "Bearer $token",
            sessionManager?.getUserModel()?.switchUserCountryCode,
            sessionManager?.getUserModel()?.switchUserMobile
        )!!
            .enqueue(object : Callback<CheckOtpResponsePOJO?> {
                override fun onResponse(
                    call: Call<CheckOtpResponsePOJO?>,
                    response: Response<CheckOtpResponsePOJO?>
                ) {
                    progressDialogLogout.dismiss()
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            switchUserFunction(response.body())
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
                                this@HomeActivity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(
                    call: Call<CheckOtpResponsePOJO?>, t: Throwable
                ) {
                    progressDialogLogout.dismiss()
                    Toast.makeText(
                        this@HomeActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
            })

    }

    private fun switchUserFunction(checkOtpResponsePOJO: CheckOtpResponsePOJO?) {
        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_TOKEN,
            checkOtpResponsePOJO!!.token
        )


        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_PROFILE_PIC,
            checkOtpResponsePOJO!!.user!!.profilePic
        )

        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_STATE,
            checkOtpResponsePOJO!!.user!!.state!!.name
        )

        preferencesManager!!.setIntegerValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_LANGUAGE_ID,
            checkOtpResponsePOJO!!.user!!.languageId!!
        )




        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_REWARD,
            checkOtpResponsePOJO!!.user!!.totalRewards.toString()
        )



        preferencesManager!!.createLoginSession(checkOtpResponsePOJO?.user)
        SessionManager.getInstance(this).setUserModel(checkOtpResponsePOJO?.user!!)

        if (checkOtpResponsePOJO!!.user!!.userType.equals("App") && checkOtpResponsePOJO.redirectTo.equals(
                "Home"
            )
        ) {
            preferencesManager!!.setStringValue(Constants.SHARED_PREFERENCE_ISLOGIN, "true")
        }

        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_COUNSELLING_SESSION_STATUS,
            checkOtpResponsePOJO!!.user!!.counsellingSessionPaymentStatus
        )


        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_DEVICEID,
            deviceID
        )
        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_LOGIN_USER_ROLE,
            checkOtpResponsePOJO.user!!.role
        )


        if (checkOtpResponsePOJO.user!!.role.equals("MasterUser")) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(applicationContext, AcademyMembersActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    override fun onPaymentError(code: Int, response: String, paymentData: PaymentData?) {
        try {
            // changeOrderPaymentStatusAPI("2", "",paymentData)


            val obj = JSONObject(response)
            val errors = JSONObject(obj.getString("error"))
            Log.d("phonetypevalue ", errors.getString("description"))


            Toast.makeText(
                this,
                "Payment failed: " + errors.getString("description"),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Exception in onPaymentError: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        try {
            //  Toast.makeText(this, "Payment Success: " +  paymentData, Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Exception in onPaymentSuccess: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        checkAppVersion()
        checkForOfflineSyncData()
    }

    private fun checkForOfflineSyncData() {
        var list =
            localCrudRepository?.getAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id)
        if (ProjectUtils.checkInternetAvailable(this) == true && !list.isNullOrEmpty()) {
            callUploadDataAPI()
        }
    }

    private fun checkUserType() {
        if (sessionManager?.getUserModel()?.switchUserMobile.isNullOrEmpty()) {
            binding.ivSwap.visibility = View.GONE
        } else {
            binding.ivSwap.visibility = View.VISIBLE
        }
    }

    private fun checkAppVersion() {
        ApiClient.client.getAppVersion("Android")
            ?.enqueue(object : Callback<AppVersionPOJO?> {
                override fun onResponse(
                    call: Call<AppVersionPOJO?>,
                    response: Response<AppVersionPOJO?>
                ) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            val currentVersion: Double =
                                BuildConfig.VERSION_NAME.replace(".", "").toDouble()
                            val serverVersion: Double =
                                response.body()?.data?.version?.replace(".", "")?.toDouble()!!

                            if (currentVersion < serverVersion) {
                                if (Constants.LATER_UPDATE_CLICK) {
                                    // Do nothing
                                } else {
                                    showUpdateDialog(response.body()?.data!!)
                                }
                            }

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
                                this@HomeActivity,
                                pojo.errors!!.get(0).message,
                                Toast.LENGTH_LONG
                            ).show()


                        } catch (exception: IOException) {
                        }

                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Internal Server Error",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(
                    call: Call<AppVersionPOJO?>, t: Throwable
                ) {

                }
            })
    }

    fun showUpdateDialog(model: AppVersionPOJO.DataItem) {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(
            "Update"
        ) { dialog, which ->
            openPlayStore()
            dialog.dismiss()
        }
        if (model.is_mandatory.equals("false", ignoreCase = true)) {
            builder.setNegativeButton(
                "Later"
            ) { dialog, which ->
                Constants.LATER_UPDATE_CLICK = true
                dialog.dismiss()
            }
        }
        builder.setTitle("Update App!")
        builder.setMessage(model.message)
        builder.setCancelable(false)
        builder.show()
    }

    private fun openPlayStore() {
        try {
            var link = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        } catch (anfe: ActivityNotFoundException) {
        }
    }

    private fun callUploadDataAPI() {
        token = preferencesManager?.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        val gson = Gson()
        val jsonString =
            gson.toJson(localCrudRepository?.getAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id))
        var params: JSONObject = JSONObject()
        params.put("data", JSONArray(jsonString))
        params.put("token", token)


        APIManager.getInstance(this@HomeActivity).postAPI(
            BuildConfig.API_URL + "submitAllAnswer",
            params,
            UserResponsePOJO::class.java,
            this@HomeActivity,
            object : APIManager.APIManagerInterface {
                override fun onSuccess(resultObj: Any?, jsonObject: JSONObject) {
                    var model: UserResponsePOJO = resultObj as UserResponsePOJO
                    localCrudRepository?.deleteAllFeedbackAnswerModel(sessionManager?.getUserModel()?.id)
                }

                override fun onError(error: String?) {
                    if (error != null) {
                        Log.d("Main", error)
                    }
                }

            })
    }

}


