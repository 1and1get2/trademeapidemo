package com.example.derek.trademeapi

import android.app.Application
import com.example.derek.trademeapi.inject.scope.PerActivity
import com.example.derek.trademeapi.ui.main.MainActivity
import com.example.derek.trademeapi.ui.main.MainActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by derek on 6/03/18.
 *
 * Provides application-wide dependencies.
 */

@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideApplication(app: App): Application

    /**
     * Provides the injector for the [MainActivity], which has access to the dependencies provided
     * by this application instance (singleton scoped objects).
     */
    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity

/*    @Module
    companion object {

    }*/
}