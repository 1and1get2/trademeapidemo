package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.api.TradeMeApiService
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by derek on 1/05/18.
 *
 * Using Kotlin, RxJava 2 and Retrofit to consume REST API on Android
 * https://softwaremill.com/kotlin-rxjava2-retrofit-android/
 *
 *
 */
class ListingPresenterImpl @Inject constructor(override val view: ListingView) : ListingPresenter {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }


    @Inject
    lateinit var apiService: TradeMeApiService
    private var rootCategory: Category? = null
    private var currentCategory: Category? = null

    // TODO: persistent of configuration change
    private val listingList = ArrayList<Listing>(30)
    private var currentListingIndex = 0

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()
    private val paginatorDisposable: Disposable
    private var loading: Boolean = false
    private var currentPage: Int = 0
    private var totalResultCount : Int = -1 //reached the end of the results
//    private set

//    override fun getTotalResultCount(): Int {
//        TODO("not implemented")
//    }

    companion object {
        const val INITIAL_LOAD_PAGES = 2
        const val ITEMS_PER_ROW = 20
    }

    init {
        Timber.d("ListingsPresenterImpl view set: $view")
        paginatorDisposable = paginator.onBackpressureDrop()
                .doOnNext {
                    if (loading) {
                        Timber.e("it is currently loading")
                    }
                }
//                .filter { !loading }
                .doOnNext {
                    loading = true
                    view.showProgress()
                }
                .concatMap {
                    Timber.d("searching category no: ${currentCategory?.number} paginator page: $it, currentPage: $currentPage")
                    apiService.search(
                            query = null,
                            category = currentCategory?.number,
                            page = currentPage,
                            rows = ITEMS_PER_ROW * it)
                            .subscribeOn(Schedulers.io())

                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    totalResultCount = it.totalCount
                    if (totalResultCount <= listingList.size + it.list.size) {
                        Timber.d("reached the end of the results, total: ${it.totalCount}, current: ${listingList.size + it.list.size}")
                    }
                }
                .subscribe({
                    val currentSize = listingList.size
                    listingList.addAll(it.list)
                    currentPage++
                    loading = false
                    view.hideProgress(listingList.size, totalResultCount)
                    view.updateListings(listingList, currentSize, listingList.size, ListingView.Notify.INSERT)
                }, {
                    loading = false
                    view.hideProgress(listingList.size, totalResultCount)
                    view.showError(it.localizedMessage)
                })
    }


    override fun onViewCreated() {
        super.onViewCreated()

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
            this.totalResultCount = -1


            val to = listingList.size
            listingList.clear()
            view.updateListings(listingList, 0, to, ListingView.Notify.CLEAR)
            // loading indicator


            loadMoreListings(INITIAL_LOAD_PAGES)
        } else {
            Timber.e("onSelectCategory selecting the same category: ${currentCategory?.name}")
        }
    }

    override fun loadMoreListings(page: Int): Boolean {
        if (loading) return false
        if (totalResultCount != -1 && totalResultCount <= listingList.size) return false // reached the end of the results
        Timber.d("loadMoreListings: $page")
        paginator.onNext(page)
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