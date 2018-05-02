package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.api.TradeMeApiService
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import com.example.derek.trademeapi.util.checkMainThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by derek on 1/05/18.
 */
class ListingPresenterImpl @Inject constructor(override val view: ListingView) : ListingPresenter {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }


    @Inject
    lateinit var apiService: TradeMeApiService
    private var rootCategory: Category? = null
    private var currentCategory: Category? = null


    private val listingList = ArrayList<Listing>(30)
    private var currentListingIndex = 0 // TODO: persistent of configuration change

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()
    private val paginatorDisposable : Disposable
    private var loading: Boolean = false
    private var currentPage: Int = 0

    companion object {
        const val INITIAL_LOAD_PAGES = 2
        const val ITEMS_PER_ROW = 20
    }

    init {
        Timber.d("ListingsPresenterImpl view set: $view")
        paginatorDisposable = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext {
                    loading = true
                    view.showProgress()
                }
                .concatMap {
                    Timber.d("paginator page: $it, currentPage: $currentPage")
                    apiService.search(currentCategory?.number, page = currentPage, rows = ITEMS_PER_ROW * it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("paginator is on main thread: ${checkMainThread()}")
                    val currentSize = listingList.size
                    listingList.addAll(it.list)
                    currentPage++
                    loading = false
                    view.hideProgress()
                    view.updateListings(listingList, currentSize, listingList.size, ListingView.Notify.INSERT)
                }, {
                    Timber.d("paginator is on main thread: ${checkMainThread()}")
                    loading = false
                    view.hideProgress()
                    view.showError(it.localizedMessage)
                })
    }


    override fun onViewCreated() {
        super.onViewCreated()
        Timber.d(" apiService: $apiService")

        onSelectCategory(null) // load all listings
        loadCategories()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        compositeDisposable.clear()
        paginatorDisposable.dispose()
    }

    /** load all categories */
    private fun loadCategories(category: Category? = null) {
        if (rootCategory == null) {
            apiService.category(category?.number ?: "0") // initial depth
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        rootCategory = it
                        Timber.d("rootCategory set: $rootCategory")
                    }
                    .doOnNext {
                        view.setCurrentCategory(it)
                    }
                    .subscribe()
                    .also { compositeDisposable.add(it) }
        }
    }


    /** listings */

    override fun scrollToTop() {
        currentListingIndex = 0
        view.scrollToPosition(0)
    }

    /**
     * updates listings
     * if category = null then load all listings
     * */
    override fun onSelectCategory(currentCategory: Category?) {
        if (this.currentCategory == null || this.currentCategory != currentCategory) {
            if (this.currentCategory != null) compositeDisposable.clear()

            this.currentCategory = currentCategory
            this.currentPage = 0
            loadMoreListings(INITIAL_LOAD_PAGES)

            val to = listingList.size
            listingList.clear()
            view.updateListings(listingList, 0, to, ListingView.Notify.CLEAR)
            // loading indicator


        } else {
            Timber.e("onSelectCategory selecting the same category: ${currentCategory?.name}")
        }
    }

    override fun loadMoreListings(count: Int) : Boolean {
        if (loading) return false
        Timber.d("loadMoreListings: $count")
        paginator.onNext(count)
        return true
    }

    override fun getListingSize(): Int = listingList.size

    override fun getListingAtIndex(index: Int): Listing = listingList[index]

    /** methods that not currently in use */

    /** load more category on demand */
    // https://stackoverflow.com/questions/43364077/rxjava-load-items-on-demand
    // https://stackoverflow.com/questions/28176072/in-rxjava-how-to-pass-a-variable-along-when-chaining-observables
    // https://stackoverflow.com/questions/31246088/how-to-do-recursive-observable-call-in-rxjava
    // https://medium.com/@stevenlow1983/rx-java-and-recursion-719f8ee1977a
/*    fun preFetchCategory(parent: Category): Observable<Category> {
        return when {
            parent.isLeaf -> {
                Observable.just(parent)
            }
            parent.subcategories == null -> {
                apiService.category(parent.number ?: "0", depth = 5).doOnNext {
                    parent.subcategories = it.subcategories
                }
            }
            else -> {
                Observable.just(parent).flatMap {
                    Observable.fromIterable(it.subcategories)
                }.flatMap { preFetchCategory(it) }
            }
        }
    }*/
}