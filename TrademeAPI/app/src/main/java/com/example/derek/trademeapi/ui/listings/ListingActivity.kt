package com.example.derek.trademeapi.ui.listings

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.example.derek.trademeapi.BR
import com.example.derek.trademeapi.R
import com.example.derek.trademeapi.base.BaseActivity
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import com.example.derek.trademeapi.ui.components.GridEndlessRecyclerViewScrollListener
import com.example.derek.trademeapi.ui.components.TopCategoryNavigationBar
import com.example.derek.trademeapi.ui.components.TopCategorySelector
import com.example.derek.trademeapi.util.GridLayoutColumnQty
import com.example.derek.trademeapi.util.bindView
import com.lapism.searchview.Search
import com.lapism.searchview.widget.SearchAdapter
import com.lapism.searchview.widget.SearchItem
import com.lapism.searchview.widget.SearchView
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by derek on 30/04/18.
 */
class ListingActivity : BaseActivity(), ListingView, CategorySelectListener {

    @Inject
    override lateinit var presenter: ListingPresenter

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val rootCoordinatorLayout: CoordinatorLayout by bindView(R.id.root)
    private val topCategoryNavigationBar: TopCategoryNavigationBar by bindView(R.id.top_navi_bar)
    private val topCategorySelector: TopCategorySelector by bindView(R.id.top_navi_bar_selector)
    private val listingRecyclerView: RecyclerView by bindView(R.id.recycler_view)

    private val searchView: SearchView by bindView(R.id.searchView)

    private val listingDiffUtilCallback = ListingDiffUtilCallback()
    private val listAdapter: Adapter by lazy { Adapter(listingList) }

    private val searchAdapter: SearchAdapter by lazy { SearchAdapter(getContext()) }

    private val listingList : ArrayList<Listing> = ArrayList()
    private var suggestionList: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setSupportActionBar(toolbar)

        val gridLayoutColumnQty = GridLayoutColumnQty(applicationContext, R.layout.view_listing_item)
        val column = gridLayoutColumnQty.calculateNoOfColumns()

        val dataLoader = object : GridEndlessRecyclerViewScrollListener.DataLoader {
            override fun onLoadMore(): Boolean = presenter.loadMoreListings(1)
        }
        val listingsLayoutManager = GridLayoutManager(getContext(), column)
        val listingScrollListener = GridEndlessRecyclerViewScrollListener(listingsLayoutManager, dataLoader)

        listingRecyclerView.apply {
            adapter = listAdapter
            layoutManager = listingsLayoutManager
            addOnScrollListener(listingScrollListener)
        }

        with(searchView){
            theme = R.style.AppTheme
            setOnQueryTextListener(
                    object : Search.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: CharSequence?): Boolean {
                            presenter.onQueryTextSubmit(query.toString())
                            searchView.close()
                            return true
                        }

                        override fun onQueryTextChange(newText: CharSequence?) {
                            presenter.onQueryTextChange(newText.toString())
                        }
                    })

            searchAdapter.setOnSearchItemClickListener{ position, title, subtitle ->
                Timber.d("setOnSearchItemClickListener $position, $title, $subtitle")
                setQuery(title, true)

                // TODO: this is a workaround for a bug in the library causing the suggestion list won't hide (or to show again immediately after hiding)
                postDelayed({
                    try {
                        val hideSuggestions = searchView.javaClass.getDeclaredMethod("hideSuggestions")
                        hideSuggestions.isAccessible = true
                        hideSuggestions.invoke(searchView)
                    } catch (e: Exception) {
                        Timber.e("Exception: $e")
                    }
                }, 150L)
            }
            searchView.adapter = searchAdapter
        }

        // gridLayoutManager.findFirstCompletelyVisibleItemPosition()
        topCategoryNavigationBar.setCategorySelectListener(this)
        topCategorySelector.setCategorySelectListener(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewCreated()
    }

    override fun onBackPressed() {
        Timber.d("searchView open:${searchView.isOpen}, isLaidOut: ${searchView.isLaidOut}")
        /*if (searchView.isOpen) {
            searchView.close()
        } else */super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Crashlytics.logException(Throwable("ListingActivity onPause"))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun getContext(): Context? = this.applicationContext

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate( R.menu.activity_listing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                searchView.open(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** search */
    override fun updateSearchSuggestion(suggestions: List<String>?) {
        Timber.d("searchSuggestionPublishProcessor list: $suggestions")
        this.suggestionList = suggestions

        if (suggestions != null && suggestions.isNotEmpty()) {
            val suggestionList = ArrayList<SearchItem>(suggestions.size)
            suggestions.forEach{s ->
                suggestionList.add(
                        SearchItem(getContext()).also { it.title = s }
                )
            }

            searchAdapter.suggestionsList = suggestionList
        } else {
            searchAdapter.suggestionsList = null
        }

        searchAdapter.notifyDataSetChanged()
    }



    /** CategorySelectListener */
    override fun onSelectCategory(newCategory: Category) {
        presenter.onSelectCategory(newCategory)
    }

    /** category */

    override fun setCurrentCategory(currentCategory: Category) {
        Timber.d("setCurrentCategory: $currentCategory")
        topCategoryNavigationBar.setCurrentCategory(currentCategory)
        topCategorySelector.setCurrentCategory(currentCategory)
    }


    /** listing */
    private inner class ListingDiffUtilCallback : DiffUtil.Callback() {
        var newListings: List<Listing>? = null
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            if (newListings == null) {
                throw RuntimeException("newListings has not been set")
            } else {
                return listingList[oldItemPosition] == newListings!![newItemPosition]
            }
        }

        override fun getOldListSize(): Int {
            return listingList.size
        }

        override fun getNewListSize(): Int {
            return newListings?.size ?: 0
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition)
        }
    }

    override fun updateListings(listings: List<Listing>) {
        listingDiffUtilCallback.newListings = listings
        val diff = DiffUtil.calculateDiff(listingDiffUtilCallback)
        this.listingList.clear()
        this.listingList.addAll(listings)
        listingDiffUtilCallback.newListings = null
        diff.dispatchUpdatesTo(listAdapter)
    }

    override fun scrollToPosition(position: Int) {
        listingRecyclerView.scrollToPosition(position)
    }

    override fun showProgress() {
//        Timber.d("loading started ....................... ")
    }

    override fun hideProgress(currentCount: Int, totalCount: Int) {
        val message = "content loading finished, $currentCount(current) / $totalCount(total)"
        Timber.d(" ....................... $message")
        Snackbar.make(rootCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
//        Timber.e("loading error: $message")
        Snackbar.make(rootCoordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    /** recycler view */


    class ItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Listing) {
            binding.setVariable(BR.listing, item)
            binding.executePendingBindings()
        }
    }

    class Adapter(private val listings: ArrayList<Listing>) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,
                    R.layout.view_listing_item, parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return listings.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val listing = listings[position]
            holder.bind(listing)
        }
    }
}

/**
 * for communications between list activity and navigation bar
 * */
interface CategorySelectListener {
    fun onSelectCategory(newCategory: Category)
}