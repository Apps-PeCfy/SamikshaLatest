package com.samiksha.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.samiksha.R
import com.samiksha.databinding.ActivitySearchBinding
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.DealingWithDistractionsActivity
import com.samiksha.ui.home.dealingWithDistraction.dealingWithDistractionsLearning.SkillDetailModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.infoAfterLearning.startTraining.TrainingStartActivity
import com.samiksha.utils.*
import java.util.*

class SearchActivity : AppCompatActivity(), ISearchView {
    lateinit var binding: ActivitySearchBinding
    private var context: Context = this

    private var list: ArrayList<SearchModel.DataItem> = ArrayList()
    var mAdapter: SearchAdapter? = null

    var iSearchPresenter: ISearchPresenter? = null
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {

        setSupportActionBar(binding.toolbar)

        PreferencesManager.initializeInstance(context)
        preferencesManager = PreferencesManager.instance
        token = preferencesManager!!.getStringValue(Constants.SHARED_PREFERENCE_LOGIN_TOKEN)


        iSearchPresenter = SearchImplementer(this)
            iSearchPresenter?.searchSkills(token, binding.edtSearch.text.toString(), currentPage!!)


        setListeners()
        setData()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        mAdapter = SearchAdapter(context, list, object : SearchAdapter.SearchAdapterInterface {
            override fun onItemSelected(position: Int, model: SearchModel.DataItem) {

                if (model.type.equals("Coming soon")) {

                } else {

                        if (model.moduleType.equals("Yoga", ignoreCase = true)) {
                            iSearchPresenter?.skillDetails(token, model.id.toString())
                        } else {

                            val intent =
                                Intent(context, DealingWithDistractionsActivity::class.java)
                            intent.putExtra("skill_id", model.id.toString())

                            startActivity(intent)
                        }


                }
            }

            override fun onFavoriteClick(position: Int, model: SearchModel.DataItem) {
                if (model.isFavorite && model.favoriteId != null) {

                }
            }

        })
        binding.recyclerView.adapter = mAdapter
        var layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.swipeRefresh.setOnRefreshListener { resetAPI() }

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@SearchActivity.isLoading = true
                currentPage++
                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return 99
            }

            override fun isLastPage(): Boolean {
                return this@SearchActivity.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@SearchActivity.isLoading
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
            iSearchPresenter?.searchSkills(token, binding.edtSearch.text.toString(), currentPage!!)




    }

    private fun loadNextPage() {
            iSearchPresenter?.searchSkills(token, binding.edtSearch.text.toString(), currentPage!!)


    }

    private fun setData() {
    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.edtSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    resetAPI()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            }
        )
    }


    override fun searchSkillSuccess(model: SearchModel?) {
        if (currentPage == PAGE_START) {
            mAdapter!!.clear()
        }
        mAdapter!!.removeLoadingFooter()
        isLoading = false

        list = model?.skills?.data!!


        // if list<10 then last page true
        if (list.size < 50) {
            isLastPage = true
        }
        if (list.size > 0) {
            mAdapter!!.addAll(list)
        }
        if (list.size > 0 || currentPage != PAGE_START) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.llSearch.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.llSearch.visibility = View.VISIBLE
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


    override fun showWait() {
    }

    override fun removeWait() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onFailure(appErrorMessage: String?) {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}