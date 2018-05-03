package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.base.BasePresenter
import com.example.derek.trademeapi.base.BaseView
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing

/**
 * Created by derek on 1/05/18.
 */

interface ListingView : BaseView<ListingPresenter> {
    enum class Notify {
        INSERT, CLEAR, UPDATE, REMOVE
    }

    /** category */
    fun setCurrentCategory(currentCategory: Category)

    /** listings */
    fun updateListings(listings: MutableList<Listing>, from: Int?, to: Int?, operation: Notify?)
    fun scrollToPosition(position: Int)
    fun showProgress()
    fun hideProgress(currentCount: Int, totalCount: Int)
    fun showError(message: String)

    /** search */
    fun updateSearchSuggestion(suggestions: List<String>)
}


interface ListingPresenter : BasePresenter<ListingView> {
    /** category */
    fun onSelectCategory(currentCategory: Category?)

    /** listings */
    fun loadMoreListings(page: Int = 1) : Boolean // @return more to load
    fun scrollToTop()
    fun getListingSize(): Int
    fun getListingAtIndex(index: Int): Listing

    /** search */
    fun onQueryTextChange(newText: String?)
    fun onQueryTextSubmit(query: String?)
}