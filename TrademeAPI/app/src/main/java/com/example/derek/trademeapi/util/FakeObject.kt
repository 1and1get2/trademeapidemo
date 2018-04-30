package com.example.derek.trademeapi.util

import com.example.derek.trademeapi.App
import com.example.derek.trademeapi.inject.scope.PerActivity
import javax.inject.Inject

/**
 * for testing dagger2
 * Created by derek on 7/03/18.
 */

@PerActivity
class FakeObject @Inject constructor(private val app: App) {
    fun doSomething(): String = "this is doing something ${if (checkMainThread()) "" else "not"} on main thread @ time: ${System.currentTimeMillis()}"
}