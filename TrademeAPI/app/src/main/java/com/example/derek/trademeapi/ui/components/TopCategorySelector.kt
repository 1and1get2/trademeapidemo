package com.example.derek.trademeapi.ui.components

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import com.example.derek.trademeapi.R
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.ui.listings.CategorySelectListener
import com.example.derek.trademeapi.util.px
import timber.log.Timber

/**
 * Created by derek on 3/05/18.
 */
class TopCategorySelector @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    private var currentCategory: Category? = null
    private var categorySelectListener: CategorySelectListener? = null

    private val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) }

    init {
        super.setLayoutManager(layoutManager)
        adapter = Adapter()
        addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
    }

    fun setCategorySelectListener(listener: CategorySelectListener) {
        categorySelectListener = listener
    }

    fun setCurrentCategory(currentCategory: Category) {
        if (this.currentCategory != null && currentCategory == this.currentCategory) {
            Timber.e("new category is the same as before")
            return
        }

        val oldSize = this.currentCategory?.subcategories?.size ?: 0
        val newSize = currentCategory.subcategories?.size ?: 0
        this.currentCategory = currentCategory

//        adapter.notifyItemRangeChanged(0, Math.max(oldSize, newSize))
        adapter.notifyDataSetChanged()
    }

    /** recycler view */
    class ViewHolder(item: TextView) : RecyclerView.ViewHolder(item)

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        private val layoutParams: RecyclerView.LayoutParams by lazy {
            val layoutParams = RecyclerView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            layoutParams
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                ViewHolder(TextView(parent.context).also {
                    it.layoutParams = RecyclerView.LayoutParams(layoutParams)
                    it.setPadding(7.px)
                    it.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            parent.context.resources.getDimensionPixelSize(
                                    R.dimen.view_listing_top_navi_bar_category_selector_font_size).toFloat())
                    it.setSingleLine(true)
                    it.gravity = Gravity.CENTER
                })

        override fun getItemCount(): Int = (currentCategory?.subcategories?.size
                ?: 0).also { Timber.d("getItemCount: $it") }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as? TextView)?.also {
                currentCategory?.subcategories?.get(position)?.also { category ->
                    it.text = category.name
                    it.setOnClickListener { categorySelectListener?.onSelectCategory(category) }
                }
            }
        }
    }
}