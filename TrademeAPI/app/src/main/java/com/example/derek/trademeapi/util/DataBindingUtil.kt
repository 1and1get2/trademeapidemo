package com.example.derek.trademeapi.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.derek.trademeapi.R

/**
 * Created by derek on 1/05/18.
 */

@BindingAdapter("app:imageUrl")
fun loadImage(v: ImageView, imgUrl: String?) {
/*    GlideApp.with(v.context)
            .load(imgUrl)
            .error(R.drawable.ic_broken_image_black)
            .placeholder(R.drawable.ic_image_black)
            .centerCrop()
            .dontAnimate()
            .dontTransform()
            .into(v)*/

    Glide.with(v.context)
            .setDefaultRequestOptions(glideRequestOptions)
            .load(imgUrl)

            .into(v)


}


val glideRequestOptions: RequestOptions by lazy {
    RequestOptions().also {
        it.error(R.drawable.ic_broken_image_black)
                .placeholder(R.drawable.ic_image_black)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
    }
}
