package com.samiksha.ui.drawer.website

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.samiksha.R
import com.samiksha.databinding.FragmentContentPageBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.PreferencesManager
import com.samiksha.utils.ProjectUtils


class WebSite : Fragment() {

  /*  @JvmField
    @BindView(R.id.webView)
    var webView: WebView? = null*/

    var preferencesManager: PreferencesManager? = null
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
        toolbar.title = "About Samiksha"
        toolbar.setNavigationIcon(R.drawable.left_arrow)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent!!.action = "MyAccount"
                activity!!.startActivity(intent)

            }
        })




        if (Build.VERSION.SDK_INT >= 19) {
            binding.webView!!.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            binding.webView!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webView.loadUrl("https://samiksha.co")

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_home, menu!!)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
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
        }
        return true
    }


}