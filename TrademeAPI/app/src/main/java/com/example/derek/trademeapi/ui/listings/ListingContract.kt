package com.example.derek.trademeapi.ui.listings

import android.support.v7.widget.RecyclerView
import com.example.derek.trademeapi.base.BasePresenter
import com.example.derek.trademeapi.base.BaseView
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing

/**
 * Created by derek on 1/05/18.
 */

interface ListingView : BaseView<ListingPresenter> {
    fun setCurrentCategory(currentCategory: Category)

    fun getListingsAdapter() : RecyclerView.Adapter<*> //?
    fun updateListings(listings : MutableList<Listing>, from: Int?, to: Int?)
}


interface ListingPresenter : BasePresenter<ListingView> {
    fun loadCategories()
    fun onSelectCategory(currentCategory: Category)


    fun loadMoreListings(count : Int?)
}