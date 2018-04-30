package com.example.derek.trademeapi.base

import android.content.Context

interface BaseView<T : Presenter> : MVPView {
    // https://discuss.kotlinlang.org/t/lateinit-modifier-is-not-allowed-on-custom-setter/1999
    var presenter : T

//    fun setPresenter(presenter: T) {
//        this._presenter = presenter
//    }

    fun getContext(): Context?
}
