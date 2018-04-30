package com.example.derek.trademeapi.util

import android.app.*
import android.support.annotation.IdRes

/**
 * Created by derek on 7/03/18.
 */

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun Activity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    fragmentManager.transact {
        replace(frameId, fragment)
    }
}



fun Activity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setActionBar(findViewById(toolbarId))
    actionBar?.run {
        action()
    }
}


fun Activity.addFragment(fragment: Fragment, @IdRes containerViewId: Int) {
    fragmentManager.transact {
        add(containerViewId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun Activity.addFragment(fragment: Fragment, tag: String) {
    fragmentManager.transact {
        add(fragment, tag)
    }
}