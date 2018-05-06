package com.example.derek.trademeapi

import com.example.derek.trademeapi.api.NetModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by derek on 30/04/18.
 * Injects application dependencies.
 */


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, NetModule::class, AppModule::class])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
