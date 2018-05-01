package com.example.derek.trademeapi

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().create(this).inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        RxJavaPlugins.setErrorHandler({ Timber.e(it) })

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }

        RxJavaPlugins.setErrorHandler({ Timber.e(it) })

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
}
