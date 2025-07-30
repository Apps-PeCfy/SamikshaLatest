package com.samiksha.ui.drawer.readempoints

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.samiksha.R
import com.samiksha.databinding.ActivityRewardBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils

class RewardFragment : Fragment() {

    /*@JvmField
    @BindView(R.id.imgReward)
    var imgReward: ImageView? = null

    @JvmField
    @BindView(R.id.btnRedeemPoint)
    var btnRedeemPoints: Button? = null

    @JvmField
    @BindView(R.id.tvRewardPoint)
    var tvRewardPoint: TextView? = null

    @JvmField
    @BindView(R.id.btnExploreMental)
    var btnExploreMental: Button? = null*/

    var preferencesManager: PreferencesManager? = null

    var rewardPoint: Int = 0

    lateinit var binding: ActivityRewardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  val rootView = inflater.inflate(R.layout.activity_reward, container, false)
     //   ButterKnife.bind(this, rootView)
        binding = ActivityRewardBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        initView()
        return binding.root
    }


   /* @OnClick(R.id.btnRedeemPoint)
    fun btnRedeemPoint() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

            preferencesManager!!.setStringValue(Constants.NOT_REWARD, "Reward")
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

    @OnClick(R.id.btnExploreMental)
    fun btnExploreMental() {
        if (ProjectUtils.checkInternetAvailable(context)!!) {

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
*/

    private fun initView() {
        setListners()
        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        rewardPoint = preferencesManager!!.getIntegerValue(Constants.USER_LEARNING_POINT.toString())
        binding.tvRewardPoint.text = "You earned " + (rewardPoint + 10) + " reward points"

        Glide.with(this).load(R.drawable.img_reward).into(binding.imgReward)


        val toolbarReward: Toolbar = requireActivity().findViewById(R.id.toolbar_home)

        toolbarReward!!.title = ""
        toolbarReward!!.setTitleTextColor(Color.WHITE)
        toolbarReward!!.setTitleTextAppearance(requireActivity(), R.style.RobotoBoldTextAppearance)

    }

    private fun setListners() {
        binding.btnRedeemPoint.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                preferencesManager!!.setStringValue(Constants.NOT_REWARD, "Reward")
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
        binding.btnRedeemPoint.setOnClickListener(View.OnClickListener {
            if (ProjectUtils.checkInternetAvailable(context)!!) {

                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    context,
                    getString(R.string.no_internet_connection)
                )

            }
        })
    }
}