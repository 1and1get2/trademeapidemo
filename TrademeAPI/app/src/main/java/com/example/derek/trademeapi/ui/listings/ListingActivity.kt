package com.example.derek.trademeapi.ui.listings

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
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
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by derek on 30/04/18.
 */
class ListingActivity : BaseActivity(), ListingView, CategorySelectListener, SearchView.OnQueryTextListener {

    @Inject
    override lateinit var presenter: ListingPresenter

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val rootCoordinatorLayout: CoordinatorLayout by bindView(R.id.root)
    private val topCategoryNavigationBar: TopCategoryNavigationBar by bindView(R.id.top_navi_bar)
    private val topCategorySelector: TopCategorySelector by bindView(R.id.top_navi_bar_selector)
    private val searchListView: ListView by bindView(R.id.list_item)
    private var searchView: SearchView? = null

    private val adapter: Adapter by lazy { Adapter(presenter) }

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var listingScrollListener: RecyclerView.OnScrollListener


    private val listingRecyclerView: RecyclerView by bindView(R.id.recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        setSupportActionBar(toolbar)

        val gridLayoutColumnQty = GridLayoutColumnQty(applicationContext, R.layout.view_listing_item)
        val column = gridLayoutColumnQty.calculateNoOfColumns()
        listingRecyclerView.adapter = adapter

        listingRecyclerView.layoutManager = GridLayoutManager(getContext(), column)
                .also { layoutManager = it }

        listingScrollListener = GridEndlessRecyclerViewScrollListener(layoutManager,
                object : GridEndlessRecyclerViewScrollListener.DataLoader {
                    override fun onLoadMore(): Boolean = presenter.loadMoreListings(1)
                })

        listingRecyclerView.addOnScrollListener(listingScrollListener)


        // gridLayoutManager.findFirstCompletelyVisibleItemPosition()
        topCategoryNavigationBar.setCategorySelectListener(this)
        topCategorySelector.setCategorySelectListener(this)
//        Timber.d("presenter: $presenter")
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun getContext(): Context? = this.applicationContext

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate( R.menu.activity_listing, menu)
        menu?.also {
            val searchActionMenuItem = menu.findItem(R.id.action_search)
            searchView = searchActionMenuItem.actionView as? SearchView
            searchView?.setOnQueryTextListener(this)
        }

        return true
    }

    /** search */
    override fun onQueryTextSubmit(query: String?): Boolean {
        Timber.d("onQueryTextSubmit: $query")
        return false
    }

    class SearchListAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v = convertView as? TextView ?: TextView(parent.context)
            return v
        }

        override fun getItem(position: Int): Any {
            TODO("not implemented")
        }

        override fun getItemId(position: Int): Long {
            TODO("not implemented")
        }

        override fun getCount(): Int {
            TODO("not implemented")
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.d("onQueryTextChange: $newText")
        return true
    }

    /** CategorySelectListener */
    override fun onSelectCategory(newCategory: Category) {
        presenter.onSelectCategory(newCategory)
    }

    override fun onSearchTermChanged(newSearch: String?) {
        Timber.d("onSearchTermChanged: $newSearch")
    }

    /** category */

    override fun setCurrentCategory(currentCategory: Category) {
//        Timber.d("setCurrentCategory: $currentCategory")
        topCategoryNavigationBar.setCurrentCategory(currentCategory)
        topCategorySelector.setCurrentCategory(currentCategory)
    }


    /** listing */

    override fun updateListings(listings: MutableList<Listing>, from: Int?, to: Int?, operation: ListingView.Notify?) {
//        Timber.d("updateListings length: ${listings.size} from: $from, to: $to")

        when (operation) {
            ListingView.Notify.INSERT -> {
                adapter.notifyItemInserted(from ?: 0)
            }
            ListingView.Notify.CLEAR -> {
                adapter.notifyItemRangeRemoved(0, to ?: listings.size)
            }
            ListingView.Notify.UPDATE -> {
                adapter.notifyItemRangeChanged(from ?: 0, to ?: listings.size)
            }
            ListingView.Notify.REMOVE -> {
                adapter.notifyItemRangeRemoved(from ?: 0, to ?: listings.size)
            }
            null -> {
                adapter.notifyDataSetChanged()
            }
        }
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
        Timber.e("loading error: $message")
        Snackbar.make(rootCoordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    /** recycler view */


    class ItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Listing) {
            binding.setVariable(BR.listing, item)
            binding.executePendingBindings()
        }
    }

    class Adapter(private val presenter: ListingPresenter) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,
                    R.layout.view_listing_item, parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return presenter.getListingSize()
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val listing = presenter.getListingAtIndex(position)
            holder.bind(listing)
        }
    }
}

/**
 * for communications between list activity and navigation bar
 * */
interface CategorySelectListener {
    fun onSelectCategory(newCategory: Category)
    fun onSearchTermChanged(newSearch: String?)
}