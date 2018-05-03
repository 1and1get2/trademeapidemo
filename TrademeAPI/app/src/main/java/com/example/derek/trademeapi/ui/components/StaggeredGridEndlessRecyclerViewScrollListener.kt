package com.example.derek.trademeapi.ui.components

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


/**
 * https://gist.github.com/fatihsokmen/5ba507e6dfff88f74a6de4ffd074cb4e
 */
class StaggeredGridEndlessRecyclerViewScrollListener(private val layoutManager: StaggeredGridLayoutManager,
                                                     private val dataLoader: DataLoader) : RecyclerView.OnScrollListener() {
    private var previousItemCount: Int = 0
    private var loading: Boolean = false

    init {
        reset()
    }


    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
        if (dy > 0) {
            val itemCount = layoutManager.itemCount

            if (itemCount != previousItemCount) {
                loading = false
            }

            val lastVisibleItems: IntArray? = null
            layoutManager.findLastCompletelyVisibleItemPositions(lastVisibleItems)
            val lastVisibleItem = lastVisibleItems?.get(0)
            if (!loading && lastVisibleItem != null && lastVisibleItem >= itemCount - 1) {
                previousItemCount = itemCount
                loading = dataLoader.onLoadMore()
            }
        }
    }

    fun reset() {
        this.loading = false
        this.previousItemCount = -1
    }

    interface DataLoader {
        fun onLoadMore(): Boolean
    }
}