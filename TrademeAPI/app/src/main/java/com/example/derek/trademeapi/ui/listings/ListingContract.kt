package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.base.BasePresenter
import com.example.derek.trademeapi.base.BaseView
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing

/**
 * Created by derek on 1/05/18.
 */

interface ListingView : BaseView<ListingPresenter> {
    /** category */
    fun setCurrentCategory(currentCategory: Category)

    /** listings */
//    fun updateListings(listings : MutableList<String>, from: Int?, to: Int?)
    fun updateListings(listings : MutableList<Listing>, from: Int?, to: Int?)
    fun scrollToTop()
}


interface ListingPresenter : BasePresenter<ListingView> {
    /** category */
    fun loadCategories()
    fun onSelectCategory(currentCategory: Category)

    /** listings */
    fun loadMoreListings(count : Int?)
    fun scrollToTop()
    fun getListingSize() : Int
    fun getListingAtIndex(index : Int) : Listing
}