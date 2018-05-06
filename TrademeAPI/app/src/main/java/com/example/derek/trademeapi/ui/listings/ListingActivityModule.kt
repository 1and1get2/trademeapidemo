package com.example.derek.trademeapi.ui.listings

import android.support.v7.app.AppCompatActivity
import com.example.derek.trademeapi.api.TradeMeApiService
import com.example.derek.trademeapi.base.BaseActivity
import com.example.derek.trademeapi.base.BaseActivityModule
import com.example.derek.trademeapi.inject.scope.PerActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import timber.log.Timber

/**
 * Created by derek on 30/04/18.
 */
/**
 * Provides main activity dependencies.
 */
@Module(includes = [BaseActivityModule::class])
abstract class ListingActivityModule {

    @Binds
    @PerActivity
    abstract fun appCompatActivity(listingActivity: ListingActivity): AppCompatActivity

    @Binds
    @PerActivity
    abstract fun baseActivity(listingActivity: ListingActivity): BaseActivity


    @Binds
    @PerActivity
    abstract fun listingView(listingActivity: ListingActivity): ListingView

/*    @Binds
    @PerActivity
    abstract fun listingPresenter(listingPresenter: ListingPresenterImpl): ListingPresenter*/

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerActivity
        fun listingPresenter(listingView: ListingActivity, apiService: TradeMeApiService): ListingPresenter {
            Timber.d("listingPresenter listingView: $listingView, apiService: $apiService")
            return /*listingView.getPersistentPresenter()?.also { it.setListingView(listingView) }
                    ?: */ListingPresenterImpl(listingView, apiService)
        }
    }
/*    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerActivity
        fun listingPresenter(listingView: ListingView): ListingPresenter {
            return *//*listingView.getPersistentPresenter()?.also { it.setListingView(listingView) }
                    ?: *//*ListingPresenterImpl(listingView)
        }
    }*/
}