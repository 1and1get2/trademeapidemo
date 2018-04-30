package com.example.derek.trademeapi.ui.main

import android.support.v7.app.AppCompatActivity
import com.example.derek.trademeapi.base.BaseActivity
import com.example.derek.trademeapi.base.BaseActivityModule
import com.example.derek.trademeapi.inject.scope.PerActivity
import dagger.Binds
import dagger.Module

/**
 * Created by derek on 30/04/18.
 */
/**
 * Provides main activity dependencies.
 */
@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {

    @Binds
    @PerActivity
    abstract fun appCompatActivity(mainActivity: MainActivity): AppCompatActivity

    @Binds
    @PerActivity
    abstract fun baseActivity(mainActivity: MainActivity): BaseActivity


//    @PerFragment
//    @ContributesAndroidInjector(modules = [ListingsFragmentModule::class])
//    abstract fun listingsFragmentInjector(): ListingsFragment

}