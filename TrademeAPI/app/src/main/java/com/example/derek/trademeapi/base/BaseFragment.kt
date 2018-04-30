package com.example.derek.trademeapi.base

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.example.derek.trademeapi.util.KotterKnife
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by derek on 15/10/17.
 */
abstract class BaseFragment : Fragment(), HasSupportFragmentInjector {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    protected fun addChildFragment(@IdRes containerViewId: Int, fragment: Fragment) =
            childFragmentManager.beginTransaction()
                    .add(containerViewId, fragment)
                    .commit()


    override fun onAttach(context: Context) {
        Timber.d("injected on onAttach(context: Context)")
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KotterKnife.reset(this)
    }

}