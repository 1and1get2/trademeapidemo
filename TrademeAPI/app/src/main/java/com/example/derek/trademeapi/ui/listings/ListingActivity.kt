package com.example.derek.trademeapi.ui.listings

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.derek.trademeapi.R
import com.example.derek.trademeapi.base.BaseActivity
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import com.example.derek.trademeapi.util.bindView
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by derek on 30/04/18.
 */
class ListingActivity : BaseActivity(), ListingView{

    @Inject override lateinit var presenter: ListingPresenter

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listing_activity)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun setCurrentCategory(currentCategory: Category) {
        Timber.d("setCurrentCategory: $currentCategory")
    }

    override fun getListingsAdapter(): RecyclerView.Adapter<*> {
        TODO("not implemented")
    }

    override fun updateListings(listings: MutableList<Listing>, from: Int?, to: Int?) {
        Timber.d("updateListings length: ${listings.size} from: $from, to: $to")
    }

    override fun getContext(): Context? = this.applicationContext
}