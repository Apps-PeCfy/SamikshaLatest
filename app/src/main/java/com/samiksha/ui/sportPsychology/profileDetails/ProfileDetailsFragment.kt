package com.samiksha.ui.sportPsychology.profileDetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.samiksha.R
import com.samiksha.databinding.FragmentProfileDetailsBinding
import com.samiksha.ui.sportPsychology.UserResponsePOJO
import com.samiksha.ui.sportPsychology.academyMembers.AcademyMembersActivity
import com.samiksha.utils.*

class ProfileDetailsFragment : Fragment() {

    private var userData: UserResponsePOJO? = null
    private var sessionManager: SessionManager? = null

    var selectedposition: Int = 0
    var preferencesManager: PreferencesManager? = null

   /* @JvmField
    @BindView(R.id.tvUserName)
    var tvUserName: TextView? = null

    @JvmField
    @BindView(R.id.tvProfessionLevel)
    var tvProfessionLevel: TextView? = null

    @JvmField
    @BindView(R.id.iv_user)
    var iv_user: RoundedImageView? = null

    @JvmField
    @BindView(R.id.tvUserMobileNo)
    var tvUserMobileNo: TextView? = null

    @JvmField
    @BindView(R.id.tvUserDOB)
    var tvUserDOB: TextView? = null

    @JvmField
    @BindView(R.id.tvCity)
    var tvCity: TextView? = null

    @JvmField
    @BindView(R.id.tvGoals1)
    var tvGoals1: TextView? = null

    @JvmField
    @BindView(R.id.tvGoals2)
    var tvGoals2: TextView? = null

    @JvmField
    @BindView(R.id.tvGoals3)
    var tvGoals3: TextView? = null

    @JvmField
    @BindView(R.id.edtFirstName)
    var edtFirstName: TextView? = null

    @JvmField
    @BindView(R.id.edtLastName)
    var edtLastName: TextView? = null

    @JvmField
    @BindView(R.id.tvUserEmailID)
    var tvUserEmailID: TextView? = null

    @JvmField
    @BindView(R.id.tvUserGender)
    var tvUserGender: TextView? = null

    @JvmField
    @BindView(R.id.spnCoaches)
    var spnCoaches: TextView? = null

    @JvmField
    @BindView(R.id.tvUserProfession)
    var tvUserProfession: TextView? = null

    @JvmField
    @BindView(R.id.tvState)
    var tvState: TextView? = null

    @JvmField
    @BindView(R.id.tvUserSport)
    var tvUserSport: TextView? = null

    @JvmField
    @BindView(R.id.edtUserCity)
    var edtUserCity: TextView? = null

    @JvmField
    @BindView(R.id.ivCommunity)
    var ivCommunity: ImageView? = null
*/
    var genderSpinner = arrayOf<String?>("Male", "Female", "Other")
    var spngen = 0

    var roleSpinner = arrayOf<String?>("Coach", "Counsellor", "MasterUser")
    var spnrole = 0

    lateinit var binding: FragmentProfileDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_details, container, false)
      //  ButterKnife.bind(this, rootView)
      //  setHasOptionsMenu(true)
        binding = FragmentProfileDetailsBinding.inflate(inflater, container, false);

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        selectedposition =
            preferencesManager!!.getIntegerValue(Constants.SHARED_PREFERENCE_LOGIN_USER_SELECTED_POSITION)
        sessionManager = SessionManager.getInstance(requireContext())
        userData = sessionManager!!.getCoachOrCounsellorModel()

        preferencesManager!!.setStringValue(
            Constants.SHARED_PREFERENCE_SELECTED_USER_NAME,
            userData?.data?.get(selectedposition)!!.fullName
        )

        binding.tvUserName!!.text = userData?.data?.get(selectedposition)!!.fullName
        binding.tvUserProfession!!.text = userData?.data?.get(selectedposition)!!.professionalLevel?.name
        binding.tvUserSport!!.text = userData?.data?.get(selectedposition)!!.sport!!.name
        binding.edtUserCity!!.text = userData?.data?.get(selectedposition)!!.city?.name
        Glide.with(requireContext())
            .load(userData?.data?.get(selectedposition)!!.profilePic)
            .apply(RequestOptions()
                .centerCrop()
                .dontAnimate()
                .dontTransform()).into(binding.ivUser!!)

        binding.tvUserMobileNo!!.text = userData?.data?.get(selectedposition)!!.fullPhoneNo
        binding.tvUserDOB!!.text = userData?.data?.get(selectedposition)!!.dateOfBirth
        binding.tvCity!!.text = userData?.data?.get(selectedposition)!!.city?.name +", "+  userData?.data?.get(selectedposition)!!.state!!.name

        binding.tvProfessionLevel!!.text = userData?.data?.get(selectedposition)!!.professionalLevel!!.name
        if (!userData?.data.isNullOrEmpty() && !userData?.data?.get(selectedposition)?.goals.isNullOrEmpty()){
            binding.tvGoals1!!.text = userData?.data?.get(selectedposition)!!.goals?.get(0)!!.name
            binding.tvGoals2!!.text = userData?.data?.get(selectedposition)!!.goals?.get(1)!!.name
            binding.tvGoals3!!.text = userData?.data?.get(selectedposition)!!.goals?.get(2)!!.name
        }


        binding.edtFirstName!!.setText(userData?.data?.get(selectedposition)!!.firstName)
        binding.edtLastName!!.setText(userData?.data?.get(selectedposition)!!.lastName)
        binding.tvUserEmailID!!.setText(userData?.data?.get(selectedposition)!!.email)
        binding.tvUserGender!!.setText(userData?.data?.get(selectedposition)!!.gender)
        binding.spnCoaches!!.setText(userData?.data?.get(selectedposition)!!.role)


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
                val intent = Intent(activity, AcademyMembersActivity::class.java)
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