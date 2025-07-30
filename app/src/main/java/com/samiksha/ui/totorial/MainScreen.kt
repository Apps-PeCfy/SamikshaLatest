/*
 * Author : Kiran Poman.
 * Module : User Module
 * Version : V 1.0
 * Sprint : II
 * Date of Development : 12/12/2018 10:02:00
 * Date of Modified : 08/02/2019 12:02:00
 * Comments : work on Linked in Intedration
 */
package com.samiksha.ui.totorial

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.samiksha.R
import com.samiksha.ui.login.LoginActivity
import com.samiksha.ui.register.RegisterACtivity
import com.samiksha.utils.Constants
import com.samiksha.utils.PreferencesManager

class MainScreen : AppCompatActivity() {
    var arrayList: ArrayList<Int>? = null
    var tvGetStarted: TextView? = null
    lateinit var screens: IntArray
    lateinit var gifArray: IntArray
    var Skip: TextView? = null
    lateinit var dot: Array<TextView?>
    var layout_dot: LinearLayout? = null
    var vp: ViewPager? = null
    var myvpAdapter: MyViewPagerAdapter? = null
    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        @SuppressLint("SetTextI18n")
        override fun onPageSelected(position: Int) {
            if (position == screens.size - 1) {
                Skip!!.visibility = View.GONE
                layout_dot!!.visibility = View.GONE
                tvGetStarted!!.visibility = View.VISIBLE
            } else {
                addDot(position)
                tvGetStarted!!.visibility = View.GONE
                Skip!!.visibility = View.VISIBLE
                layout_dot!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    //PreferenceManager preferenceManager;
    private var mPreferencesManager: PreferencesManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        setContentView(R.layout.activity_main_screen)
        layout_dot = findViewById(R.id.layout_dot)

        PreferencesManager.initializeInstance(this)
        mPreferencesManager = PreferencesManager.instance
        vp = findViewById(R.id.view_pager)
        tvGetStarted = findViewById(R.id.tvGetStarted)
        Skip = findViewById(R.id.skip)

        arrayList = ArrayList()
        arrayList!!.add(R.color.red)
        arrayList!!.add(R.color.red)
        arrayList!!.add(R.color.red)
        addDot(0)

        screens = intArrayOf(
            R.layout.introscreen_1,
            R.layout.introscreen_2,
            R.layout.introscreen_3
        )
        gifArray = intArrayOf(
            R.raw.first,
            R.raw.second,
            R.raw.third
        )
        myvpAdapter = MyViewPagerAdapter()
        vp!!.setAdapter(myvpAdapter)
        vp!!.addOnPageChangeListener(viewPagerPageChangeListener)
    }

    private fun addDot(page_position: Int) {

        dot = arrayOfNulls(arrayList!!.size)
        layout_dot!!.removeAllViews()



        for (i in 0 until dot.size) {
            dot[i] = TextView(this)
            dot[i]!!.text = Html.fromHtml("&#9673;")
            //set default dot color
            dot[i]!!.setTextColor(resources.getColor(R.color.colorGray))
            layout_dot!!.addView(dot[i])
        }
        //set active dot color
        //set active dot color
        dot[page_position]!!.setTextColor(resources.getColor(R.color.login_button))

    }

    fun next(v: View?) {
        val i = getItem(+1)
        if (i < screens.size) {
            vp!!.currentItem = i
        } else {
            launchMain()
        }
    }

    fun getStarted(view: View?) {
        launchMain()
    }

    fun skip(view: View?) {
        launchMain()
    }

    private fun getItem(i: Int): Int {
        return vp!!.currentItem + i
    }

    private fun launchMain() {
        //preferenceManager.setFirstTimeLaunch(false);
        mPreferencesManager!!.setBooleanValue(
            Constants.SHARED_PREFERENCE_IS_APP_FIRST_TIME,
            false
        )
        val intent = Intent(applicationContext, RegisterACtivity::class.java)
        startActivity(intent)
        finish()
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var inflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater!!.inflate(R.layout.introscreen_1, container, false)

            var imageView = view.findViewById<ImageView>(R.id.userProfilePic)

            Glide.with(this@MainScreen)
                .load(gifArray[position])
                .into(imageView)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return gifArray.size
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            val v = `object` as View
            container.removeView(v)
        }

        override fun isViewFromObject(
            v: View,
            `object`: Any
        ): Boolean {
            return v === `object`
        }
    }
}