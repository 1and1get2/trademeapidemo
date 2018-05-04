package com.example.derek.trademeapi

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject



/**
 * Created by derek on 15/10/17.
 */

class App : Application(), HasActivityInjector {
    companion object {
        init {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().create(this).inject(this)


        /** Crashlytics */
        Fabric.with(this, Crashlytics())
        Crashlytics.setString("BUILD_TYPE", BuildConfig.BUILD_TYPE)
        Crashlytics.logException(Throwable("App started"))

        Timber.plant(LogTree())

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }

        RxJavaPlugins.setErrorHandler({
            Timber.e(it)
        })

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }


/*        if (BuildConfig.DEBUG && false) { // todo: console flooded with logs, skiped for now
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }*/
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

//    TODO: glide dependency issue, conflicts with dagger
//    @GlideModule
//    inner class MyAppGlideModule : AppGlideModule()



    /** So that I can spy on those trademe guys who's reading my code right now */

    class LogTree : DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, tag, message, t)
            Crashlytics.log("priority: $priority, tag: $tag, message: $message")
//            Crashlytics.logException(Throwable("Dummy Exception: $priority $tag"))
        }
    }
}
