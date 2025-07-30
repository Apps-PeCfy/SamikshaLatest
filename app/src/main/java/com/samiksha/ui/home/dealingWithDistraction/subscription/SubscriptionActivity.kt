package com.samiksha.ui.home.dealingWithDistraction.subscription

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.ActivitySubscriptionBinding

class SubscriptionActivity : AppCompatActivity(), SubscriptionAdapter.IClickListener {

    var recyclerViewpreferences: RecyclerView? = null

   /* @JvmField
    @BindView(R.id.toolbar)
    var mToolbar: Toolbar? = null
*/
    lateinit var binding: ActivitySubscriptionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_subscription)
     //   ButterKnife.bind(this)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.hide()
        initRecyclerViewSubscription()
    }

    private fun initRecyclerViewSubscription() {

        recyclerViewpreferences = findViewById<View>(R.id.recycler_subscription) as RecyclerView
        recyclerViewpreferences!!.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewpreferences!!.itemAnimator = DefaultItemAnimator()
        val adapter = SubscriptionAdapter(applicationContext)
        recyclerViewpreferences!!.adapter = adapter
        adapter!!.setClickListener(this)

    }




    override fun click() {
        val intent = Intent(applicationContext, SubscriptionLearningACtivity::class.java)
        startActivity(intent)

    }
}
