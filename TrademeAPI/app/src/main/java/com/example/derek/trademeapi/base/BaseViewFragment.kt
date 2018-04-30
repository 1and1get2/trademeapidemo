package com.example.derek.trademeapi.base

import android.os.Bundle
import com.example.derek.trademeapi.base.BaseFragment
import com.example.derek.trademeapi.base.BaseView
import com.example.derek.trademeapi.base.Presenter
import javax.inject.Inject

/**
 * Created by derek on 11/03/18.
 */

abstract class BaseViewFragment<T : Presenter> : BaseFragment(), BaseView<T>/*MVPView*/ {

    @Inject
    override lateinit var presenter: T


    override fun onViewStateRestored(savedInstanceState: Bundle?)
    {
        super.onViewStateRestored(savedInstanceState)
        presenter.onStart(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        presenter.onEnd()
        super.onDestroyView()
    }
}