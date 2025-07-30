package com.samiksha.ui.drawer.referAndEarn

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.BuildConfig
import com.samiksha.R
import com.samiksha.databinding.FragmentReferEarnBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.ProjectUtils
import com.samiksha.utils.SessionManager


class ReferAndEarn : Fragment() {

    private var sessionManager: SessionManager? = null
    lateinit var binding: FragmentReferEarnBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     //   val rootView = inflater.inflate(R.layout.fragment_refer_earn, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = FragmentReferEarnBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()

        return binding.root
    }

    private fun initView() {
        sessionManager = SessionManager.getInstance(requireActivity())
        setListners()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Refer and Earn"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })

    }
    private fun setListners() {
        binding.btnSend.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                var shareMessage =
                    resources.getString(R.string.str_msg_share_app_test) + sessionManager?.getUserModel()?.referralCode
                        .toString() + " " + resources.getString(R.string.str_msg_share_app_test2)
                shareMessage =
                    shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&referrer=" +
                            sessionManager?.getUserModel()?.referralCode + "\n"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Samiksha Referral"))

            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )
            }
        })
    }

    /*@OnClick(R.id.btnSend)
    fun btnSend() {

        if (ProjectUtils.checkInternetAvailable(context)!!) {

            val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
        var shareMessage =
            resources.getString(R.string.str_msg_share_app_test) + sessionManager?.getUserModel()?.referralCode
                .toString() + " " + resources.getString(R.string.str_msg_share_app_test2)
        shareMessage =
            shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&referrer=" +
                    sessionManager?.getUserModel()?.referralCode + "\n"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Samiksha Referral"))

        } else {
            ProjectUtils.showToast(
                context,
                getString(R.string.no_internet_connection)
            )
        }




    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       Toast.makeText(activity,"onActivityResult",Toast.LENGTH_SHORT).show()
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