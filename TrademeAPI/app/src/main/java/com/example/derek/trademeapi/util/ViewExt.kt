package com.example.derek.trademeapi.util

import android.content.Context
import android.content.res.Resources
import android.support.design.widget.Snackbar
import android.util.TypedValue
import android.view.View


/**
 * Created by derek on 6/02/18.
 */

fun View.showSnackBar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun Context.toPixelFromDip(value: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

fun View.toPixelFromDip(value: Float) = context.toPixelFromDip(value)


val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()