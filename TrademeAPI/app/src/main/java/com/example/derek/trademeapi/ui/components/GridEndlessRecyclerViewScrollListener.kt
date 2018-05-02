package com.example.derek.trademeapi.ui.components

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView


/**
 * https://gist.github.com/fatihsokmen/5ba507e6dfff88f74a6de4ffd074cb4e
 */
class GridEndlessRecyclerViewScrollListener(private val gridLayoutManager: GridLayoutManager, private val dataLoader: DataLoader) : RecyclerView.OnScrollListener() {
    private var previousItemCount: Int = 0
    private var loading: Boolean = false

    init {
        reset()
    }


    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
        if (dy > 0) {
            val itemCount = gridLayoutManager.itemCount

            if (itemCount != previousItemCount) {
                loading = false
            }

            if (!loading && gridLayoutManager.findLastVisibleItemPosition() >= itemCount - 1) {
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