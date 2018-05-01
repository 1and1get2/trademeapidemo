package com.example.derek.trademeapi.util

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import android.os.Looper
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.forEach


/**
 * Created by derek on 7/03/18.
 */


fun checkMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun Application.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
        val activeNetwork = connectivityManager.getActiveNetworkInfo()
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    return false
}

/**
    switch (currentNightMode) {
    case Configuration.UI_MODE_NIGHT_NO:
    // Night mode is not active, we're in day time
    case Configuration.UI_MODE_NIGHT_YES:
    // Night mode is active, we're at night!
    case Configuration.UI_MODE_NIGHT_UNDEFINED:
    // We don't know what mode we're in, assume notnight
    }
 */
fun Context.getCurrentNightMode(): Int {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
}

//fun Context.safeContext(): Context =
//        takeUnless { isDeviceProtectedStorage }?.run {
//            it.applicationContext.let {
//                ContextCompat.createDeviceProtectedStorageContext(it) ?: it
//            }
//        } ?: this

/** menu icon color tinting */

fun Menu.tintAllIcons(context: Context, @ColorRes color: Int) {
    tintAllIcons(ContextCompat.getColor(context, color))
}

fun Menu.tintAllIcons(@ColorInt color: Int) {
    forEach {
        it?.tintMenuIconColor(color)
    }
}

fun MenuItem.tintMenuIconColor(context: Context, @ColorRes color: Int) {
    tintMenuIconColor2(ContextCompat.getColor(context, color))
}

fun MenuItem.tintMenuIconColor2(@ColorInt color: Int) {
    icon?.apply {
        val wrapDrawable = DrawableCompat.wrap(icon)
        DrawableCompat.setTint(wrapDrawable, color)
        icon = wrapDrawable
    }
}
// alternatively
fun MenuItem.tintMenuIconColor(@ColorInt color: Int) {
    icon?.apply {
        icon.mutate()
        icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//        icon.alpha = 1
    }
}


// https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
open class GridLayoutColumnQty(context: Context, viewId: Int) {
    private val width: Int
    private val height: Int
    private var remaining: Int = 0
    private val displayMetrics: DisplayMetrics

    init {
        val view = View.inflate(context, viewId, null)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        width = view.getMeasuredWidth()
        height = view.getMeasuredHeight()
        displayMetrics = context.getResources().getDisplayMetrics()
    }

    fun calculateNoOfColumns(): Int {

        var numberOfColumns = displayMetrics.widthPixels / width
        remaining = displayMetrics.widthPixels - numberOfColumns * width
        //        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
        if (remaining / (2 * numberOfColumns) < 15) {
            numberOfColumns--
            remaining = displayMetrics.widthPixels - numberOfColumns * width
        }
        return numberOfColumns
    }

    fun calculateSpacing(): Int {

        val numberOfColumns = calculateNoOfColumns()
        //        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
        return remaining / (2 * numberOfColumns)
    }
}


class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
    }
}
