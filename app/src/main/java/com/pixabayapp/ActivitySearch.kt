package com.pixabayapp

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gconnect.Resource
import com.example.gconnect.ResourceState
import com.pixabayapp.Adapter.AdapterSearchResult
import com.pixabayapp.Model.SearchResult
import com.pixabayapp.ViewModel.VmImageSearch
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject

/**
 * Search activity to show search result.
 */
class ActivitySearch : BaseActivity() {

    // Holds reference of the tag value to show activity name in log
    private val TAG = ActivitySearch::class.java!!.getSimpleName()

    // Holds reference of the ViewModel instance created for class VmImageSearch
    private val vmImageSearch: VmImageSearch by inject()

    // Holds reference of the text to be searched.
    var searchString: String = ""

    // Holds reference of the searched text result.
    var searchResultHits = ArrayList<SearchResult.Hit>()

    // Page number to load data for searched images from server
    var pageNoSearchResult: Int = 1

    // Holds reference of the page number.
    var count: Int = 0

    // Holds reference of the loading value
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inItUI()
    }

    /**
     * Initialize UI.
     */
    private fun inItUI() {

        // Check network availability
        if (isNetworkAvailable()) {
            Log.v(TAG, "inItUI() :: Network available")
            inItUIImageSearch()
            observeViewModelImageSearch()
        } else {
            Log.v(TAG, "inItUI() :: Network not available")
            Toast.makeText(this, getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show()
        }


        etSearch.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(200))

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (!p0!!.isEmpty()) {
                    searchString = p0.toString()
                    pageNoSearchResult = 1
                    searchResultHits.clear()
                    imgvClose.visibility = View.VISIBLE
                } else {
                    clearRecyclerView()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        imgvClose.setOnClickListener {
            clearRecyclerView()
        }

        // Added on scroll listener to images recyclerView.
        rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager!!.findLastCompletelyVisibleItemPosition() === searchResultHits.size - 1) {
                        Log.v(
                            TAG,
                            String.format(
                                "onScrolled():: searchString = %s, pageNo = %d",
                                searchString,
                                pageNoSearchResult
                            )
                        )
                        vmImageSearch.getImageSearchResult(searchString, pageNoSearchResult.toString())
                        isLoading = true
                    }
                }
            }
        })


        btnSearch.setOnClickListener {

            searchResultHits.clear()
            rvSearch.removeAllViews()
            rvSearch.adapter?.notifyDataSetChanged()
            pageNoSearchResult = 1
            searchString = etSearch.text.trim().toString()

            if (isNetworkAvailable()) {
                if (searchString.equals(""))
                Toast.makeText(this, getString(R.string.enter_search_text), Toast.LENGTH_SHORT).show()
                else{
                    hidekeyboard()
                    vmImageSearch.getImageSearchResult(searchString, pageNoSearchResult.toString())
                }
            } else {
                Toast.makeText(this, R.string.internet_not_available, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Get image search list.
     */
    fun getSearchResultList(): java.util.ArrayList<SearchResult.Hit> {
        Log.v(TAG, "getSearchResultList()")
        return searchResultHits
    }

    /**
     * Clear previous search result.
     */
    private fun clearRecyclerView() {

        pageNoSearchResult = 1
        etSearch.text.clear()
        searchString = ""
        searchResultHits.clear()
        rvSearch.removeAllViews()
        rvSearch.adapter?.notifyDataSetChanged()
        imgvClose.visibility = View.GONE
    }


    /**
     * Observe images data change from server.
     */
    private fun observeViewModelImageSearch() {
        Log.v(TAG, "observeViewModelImageSearch()")

        vmImageSearch.searchAPIResult.observe(this, Observer<Resource<SearchResult>> { result ->
            if (result != null) {
                Log.v(TAG, String.format("observeViewModelImageSearch() :: result not null"))
                setSearchResultData(result)
            }
        })

    }

    // Set image data to UI
    private fun setSearchResultData(resource: Resource<SearchResult>) {
        Log.v(TAG, String.format("setSearchResultData() :: result"))

        rvSearch.layoutManager = GridLayoutManager(this, 2)

        resource?.let {

            when (it.state) {
                ResourceState.LOADING -> showProgressDialog()
                ResourceState.SUCCESS -> dismissProgressDialog()
                ResourceState.ERROR -> dismissProgressDialog()
            }

            it.data?.let {

                // Check received data size and increment page number by one to load next page data
                if (resource.data!!.hits != null && resource.data!!.hits?.size > 0) {
                    Log.v(
                        TAG,
                        String.format(
                            "setSearchResultData() :: searchResult hits size = %d",
                            resource.data!!.hits?.size
                        )
                    )

                    count = searchResultHits.size

                    searchResultHits.addAll(resource.data!!.hits)

                    Toast.makeText(this, "Page " + pageNoSearchResult, Toast.LENGTH_SHORT).show()

                    pageNoSearchResult = pageNoSearchResult + 1
                }

                // Set updated data to images recycler view
                if (rvSearch.adapter == null && searchResultHits.size > 0) {

                    rvSearch.adapter = AdapterSearchResult(
                        this,
                        searchResultHits,
                        { partItem: SearchResult.Hit -> partItemClicked(partItem) })
                } else {
                    Log.v(TAG, "setSearchResultData() :: notifyDataSetChanged()")
                    rvSearch.adapter!!.notifyDataSetChanged()
                    rvSearch.scrollToPosition(count - 2)
                    isLoading = false
                }
            }

            it.message?.let {
                Toast.makeText(this, "Error: " + resource.message, Toast.LENGTH_SHORT).show()
            }
        }

        Log.v(TAG, String.format("handleNowPlayingResult() :: searchResultHits size = %d", searchResultHits?.size))

    }


    /**
     * Handle recycler view item click event.
     * @imageListItem data set used to create recycler view item.
     */
    private fun partItemClicked(searchResultHit: SearchResult.Hit) {

    }

    /**
     * Load image detail from server.
     * Register observer to receive data change.
     */
    private fun inItUIImageSearch() {
        Log.v(TAG, String.format("inItUIImageSearch() ::"))
    }


    override fun getToolbarTitle(): String? {
        Log.v(TAG, "getToolbarTitle() :: SearchResult")
        return "Search"
    }

    override fun getLayoutResource(): Int {
        Log.v(TAG, "getLayoutResource()")
        return R.layout.activity_search
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}