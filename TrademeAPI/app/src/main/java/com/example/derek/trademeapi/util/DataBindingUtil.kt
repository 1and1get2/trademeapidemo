package com.example.derek.trademeapi.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by derek on 1/05/18.
 */

@BindingAdapter("app:imageUrl")
fun loadImage(v: ImageView, imgUrl: String) {
    Glide.with(v.context).load(imgUrl).into(v)
}