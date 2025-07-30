package com.samiksha.ui.drawer.faqs

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.samiksha.R
import com.samiksha.databinding.FragmentFaqsBinding
import com.samiksha.ui.drawer.faqs.POJO.FAQResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils
import java.util.ArrayList

class FAQs : Fragment(), IFAQView {

    var faqAdapter: FAQAdapter? = null

 /*   @JvmField
    @BindView(R.id.recycler_faqs)
    var recycler_faqs: RecyclerView? = null*/

    var ifaqPresenter: IFAQPresenter? = null
    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    private var getFaqList: List<FAQResponsePOJO.DataItem> = ArrayList()

    lateinit var binding: FragmentFaqsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val rootView = inflater.inflate(R.layout.fragment_faqs, container, false)
       // ButterKnife.bind(this, rootView)
        binding = FragmentFaqsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true)

        PreferencesManager.initializeInstance(requireActivity())
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)
        ifaqPresenter = FAQImplementer(this)

        initView()
            //API Call
        ifaqPresenter!!.getFaq(token)

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


    private fun initView() {


        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "FAQs"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })



    }

    override fun onFaqSUccess(faqResponsePOJO: FAQResponsePOJO?) {


        getFaqList = faqResponsePOJO!!.data
        if (getFaqList.isNotEmpty()) {
            faqAdapter = FAQAdapter(activity, getFaqList)
            binding.recyclerFaqs.layoutManager = LinearLayoutManager(activity)
            binding.recyclerFaqs.adapter = faqAdapter

            faqAdapter!!.notifyDataSetChanged()
        }


    }

    override fun showWait() {
        ProjectUtils.showProgressDialog(activity)
    }

    override fun removeWait() {
        ProjectUtils.dismissProgressDialog()
    }

    override fun onFailure(appErrorMessage: String?) {
        Toast.makeText(activity, appErrorMessage, Toast.LENGTH_LONG).show()
    }

}