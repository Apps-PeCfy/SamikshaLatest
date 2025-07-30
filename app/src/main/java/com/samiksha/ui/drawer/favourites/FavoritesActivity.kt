package com.samiksha.ui.drawer.favourites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.samiksha.R
import com.samiksha.databinding.ActivityFavoritesBinding
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.main.HomeActivity
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.utils.*
import java.util.*

class FavoritesActivity : AppCompatActivity(), IFavoritesView {
    lateinit var binding: ActivityFavoritesBinding
    private var context: Context = this

    private var categoryArrayList: List<AllCategoriesResponsePOJO.DataItem> = ArrayList()
    private var list: ArrayList<FavouritesModel.DataItem> = ArrayList()
    var categoryAdapter: CategoryAdapter? = null
    var mAdapter: FavoritesAdapter? = null

    var iFavoritesPresenter: IFavoritesPresenter? = null
    var token: String? = null
    var preferencesManager: PreferencesManager? = null

    // PAGINATION VARS
    private val PAGE_START = 1

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private var isLoading = false

    // If current page is the last page (Pagination will stop after this page load)
    private var isLastPage = false

    // indicates the current page which Pagination is fetching.
    private var currentPage = PAGE_START


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }


    private fun init() {

        setSupportActionBar(binding.toolbar)

        PreferencesManager.initializeInstance(context)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        iFavoritesPresenter = FavoritesImplementer(this)
        iFavoritesPresenter?.allCategories(token)
        iFavoritesPresenter?.favoritesSkills(token, null, currentPage!!)

        setListeners()
        setData()
        setRecyclerView()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                if(ProjectUtils.checkInternetAvailable(this@FavoritesActivity)!!){
                val intent = Intent(this, HomeActivity::class.java)
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


    private fun setRecyclerView() {
        binding.recyclerViewCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        binding.recyclerViewCategory.itemAnimator = DefaultItemAnimator()


        categoryAdapter = CategoryAdapter(
            context,
            categoryArrayList,
            object : CategoryAdapter.CategoryAdapterInterface {
                override fun onItemSelected(
                    position: Int,
                    model: AllCategoriesResponsePOJO.DataItem
                ) {
                    changeColorLogic(model)
                }

            })
        binding.recyclerViewCategory.adapter = categoryAdapter


        mAdapter =
            FavoritesAdapter(context, list, object : FavoritesAdapter.FavoritesAdapterInterface {
                override fun onItemSelected(position: Int, model: FavouritesModel.DataItem) {
                    if (ProjectUtils.checkInternetAvailable(this@FavoritesActivity)!!) {

                        if(model.type.equals("Coming soon")){

                    }else {


                        if (model.moduleType.equals("Yoga", ignoreCase = true)) {
                            iFavoritesPresenter?.skillDetails(token, model.id.toString())
                        } else {

                            val intent =
                                Intent(context, DealingWithDistractionsActivity::class.java)
                            intent.putExtra("skill_id", model.id.toString())

                            startActivity(intent)
                        }
                    }

                    } else {
                        ProjectUtils.showToast(
                            this@FavoritesActivity,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }

                override fun onFavoriteClick(position: Int, model: FavouritesModel.DataItem) {
                    if (ProjectUtils.checkInternetAvailable(this@FavoritesActivity)!!) {

                        if (model.isFavorite && model.favoriteId != null) {
                        iFavoritesPresenter?.deleteFromFavorites(token, model.favoriteId.toString())
                    }
                    } else {
                        ProjectUtils.showToast(
                            this@FavoritesActivity,
                            getString(R.string.no_internet_connection)
                        )
                    }
                }

            })
        binding.recyclerView.adapter = mAdapter
        var layoutManager = GridLayoutManager(context, 1)
        binding.recyclerView.layoutManager = layoutManager
        binding.swipeRefresh.setOnRefreshListener { resetAPI() }

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@FavoritesActivity.isLoading = true
                currentPage++
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return 99
            }

            override fun isLastPage(): Boolean {
                return this@FavoritesActivity.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@FavoritesActivity.isLoading
            }
        })

        loadNextPage()
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

            SessionManager.getInstance(context).setSkillDetailModel(model)

            val intent = Intent(context, TrainingStartActivity::class.java)
            startActivity(intent)

        }

    }

    /**
     * Pagination
     **/

    private fun resetAPI(): Unit {
        // To clear already set adapter and new list on swipe refresh
        binding.swipeRefresh.isRefreshing = true
        mAdapter!!.clear()
        currentPage = PAGE_START
        isLastPage = false
        iFavoritesPresenter?.favoritesSkills(token, null, currentPage)
    }

    private fun loadNextPage() {
        iFavoritesPresenter?.favoritesSkills(token, null, currentPage)
    }

    private fun setData() {
    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }


        binding.btnExploreMentalSkills.setOnClickListener {

            if (ProjectUtils.checkInternetAvailable(this@FavoritesActivity)!!) {

                val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            } else {
                ProjectUtils.showToast(
                    this@FavoritesActivity,
                    getString(R.string.no_internet_connection)
                )
            }

        }
    }

    private fun changeColorLogic(model: AllCategoriesResponsePOJO.DataItem) {
        for (answerModel in categoryArrayList) {
            answerModel.checked = model.name == answerModel.name
        }
        categoryAdapter?.updateAdapter(categoryArrayList)
    }

    override fun categoriesSuccess(allCategoriesResponsePOJO: AllCategoriesResponsePOJO?) {
        categoryArrayList = allCategoriesResponsePOJO!!.data!!
        preferencesManager!!.setcategoryList(categoryArrayList)
        categoryAdapter?.updateAdapter(categoryArrayList)

    }

    override fun favoritesSkillSuccess(model: FavouritesModel?) {
        if (currentPage == PAGE_START) {
            mAdapter!!.clear()
        }
        mAdapter!!.removeLoadingFooter()
        isLoading = false

        list = model?.data?.skills?.data!!


        // if list<10 then last page true
        if (list.size < 50) {
            isLastPage = true
        }
        if (list.size > 0) {
            binding.llNoData.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            mAdapter!!.addAll(list)
        } else {
            binding.llNoData.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        if (list.size > 0 || currentPage != PAGE_START) {
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.GONE
        }


        // Add loading footer if last page is false
        if (!isLastPage) {
            mAdapter!!.addLoadingFooter()
        }
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onSkillDetails(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
        saveSkillDetailInSession(skillDetailsResponsePOJO!!)
    }

    override fun deleteFromFavoriteSuccess(skillDetailsResponsePOJO: SkillDetailsResponsePOJO?) {
      //  Toast.makeText(context, skillDetailsResponsePOJO?.message, Toast.LENGTH_LONG).show()
        //   resetAPI()

        iFavoritesPresenter?.favoritesSkills(token, null, currentPage!!)

    }


    override fun showWait() {
    }

    override fun removeWait() {
    }

    override fun onFailure(appErrorMessage: String?) {
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}