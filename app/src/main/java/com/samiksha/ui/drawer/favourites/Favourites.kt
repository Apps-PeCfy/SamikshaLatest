package com.samiksha.ui.drawer.favourites

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.GridView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.samiksha.R
import com.samiksha.databinding.FragmentFavourtiesBinding
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.utils.ProjectUtils

class Favourites : Fragment() {


  /*  @JvmField
    @BindView(R.id.recycler_favourties)
    var recycler_favourties: GridView? = null*/
    lateinit var binding: FragmentFavourtiesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavourtiesBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true)
        setHasOptionsMenu(true)
        initView()
        return binding.root
    }

    private fun initView() {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar_home)
        toolbar.title = "Favourites"
        setListners()
     //   val customAdapter = FavoritesAdapter(requireActivity(), 3)
     //   recycler_favourties!!.adapter = customAdapter

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


   /* @OnClick(R.id.btnModules)
    fun btnModules() {


        btnModules.setTextColor(getResources().getColor(R.color.ratingBar))
        btnDailyExercise.setTextColor(getResources().getColor(R.color.favBtn))
        btnYoga.setTextColor(getResources().getColor(R.color.favBtn))
        favFragment(3)
    }*/

    private fun setListners() {
        binding.btnModules.setOnClickListener(View.OnClickListener {
            binding.btnModules.setTextColor(getResources().getColor(R.color.ratingBar))
            binding.btnDailyExercise.setTextColor(getResources().getColor(R.color.favBtn))
            binding.btnYoga.setTextColor(getResources().getColor(R.color.favBtn))
            favFragment(3)
        })

        binding.btnDailyExercise.setOnClickListener(View.OnClickListener {
            binding.btnDailyExercise.setTextColor(getResources().getColor(R.color.ratingBar))
            binding.btnModules.setTextColor(getResources().getColor(R.color.favBtn))
            binding.btnYoga.setTextColor(getResources().getColor(R.color.favBtn))
            favFragment(5)

        })

        binding.btnYoga.setOnClickListener(View.OnClickListener {
            binding.btnYoga.setTextColor(getResources().getColor(R.color.ratingBar))
            binding.btnModules.setTextColor(getResources().getColor(R.color.favBtn))
            binding.btnDailyExercise.setTextColor(getResources().getColor(R.color.favBtn))
            favFragment(6)

        })


    }

    private fun favFragment(i: Int) {
        val recycler_favourties: GridView = requireActivity().findViewById(R.id.recycler_favourties)!!
     //   val customAdapter = FavoritesAdapter(requireActivity(), i)
     //   recycler_favourties!!.adapter = customAdapter
    }

    /*@OnClick(R.id.btnDailyExercise)
    fun btnDailyExercise() {
        btnDailyExercise.setTextColor(getResources().getColor(R.color.ratingBar))
        btnModules.setTextColor(getResources().getColor(R.color.favBtn))
        btnYoga.setTextColor(getResources().getColor(R.color.favBtn))

        favFragment(5)

    }
    @OnClick(R.id.btnYoga)
    fun btnYoga() {
        btnYoga.setTextColor(getResources().getColor(R.color.ratingBar))
        btnModules.setTextColor(getResources().getColor(R.color.favBtn))
        btnDailyExercise.setTextColor(getResources().getColor(R.color.favBtn))

        favFragment(6)
    }
*/

}