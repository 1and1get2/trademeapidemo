package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.api.TradeMeApiService
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by derek on 1/05/18.
 *
 * TODO: persistent on configuration change
 *
 * Using Kotlin, RxJava 2 and Retrofit to consume REST API on Android
 * https://softwaremill.com/kotlin-rxjava2-retrofit-android/
 *
 *
 */
class ListingPresenterImpl @Inject constructor(override val view: ListingView) : ListingPresenter {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }


    @Inject lateinit var apiService: TradeMeApiService


    private var rootCategory: Category? = null
    private var currentCategory: Category? = null

    private val listingList = ArrayList<Listing>(30)
    private var currentListingIndex = 0

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()
    private val paginatorDisposable: Disposable
    private var loading: Boolean = false
    private var currentPage: Int = 0 // page number starts at 1
    private var totalResultCount : Int = -1 //reached the end of the results
    private var paginationSubscription : Subscription? = null


    private val searchPublishProcessor: PublishProcessor<String> = PublishProcessor.create()


    private var tabLastTabTime : Long = 0

    companion object {
        const val INITIAL_LOAD_PAGES = 2
        const val ITEMS_PER_ROW = 40
        const val SCROLL_TO_TOP_DELAY_MS = 500
    }

    init {
        paginatorDisposable = paginator.onBackpressureDrop()
                .doOnNext {
                    Timber.d("it is currently loading: $loading, paginationSubscription: $paginationSubscription")
                    if (loading) {
                        paginationSubscription?.cancel()
                        loading = false
                    }
                }
//                .filter { !loading }
                .doOnNext {
                    loading = true
                    view.showProgress()
                }
                .concatMap {
                    Flowable.range(currentPage + 1, it)
                }
                .concatMap {
//                    Timber.d("searching category no: ${currentCategory?.number} paginator current page: $it, currentPage: $currentPage")
                    Flowable.just(it)
                            .doOnNext { currentPage = it }
                            // skip further request when already reached the end of the result
                            .filter {newPage ->
                                (totalResultCount == -1 || totalResultCount >= (newPage - 1) * ITEMS_PER_ROW)
                            }
                            .concatMap {
                                apiService.search(
                                    query = null,
                                    category = currentCategory?.number,
                                    page = it,
                                    rows = ITEMS_PER_ROW)
                                    .doOnNext {
                                        currentPage++
                                        totalResultCount = it.totalCount
                                    }
                                    .subscribeOn(Schedulers.io())
                                    .doOnCancel {
                                        Timber.d("canceling old apiService.search request")
                                    } }

                            // update current page number from the backend
                            .doOnNext { it.page ?.also { currentPage = it } }

                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val currentSize = listingList.size
                    listingList.addAll(it.list)
                    loading = false
                    view.hideProgress(listingList.size, totalResultCount)
                    view.updateListings(listingList, currentSize, listingList.size, ListingView.Notify.INSERT)
                }, {
                    loading = false
                    view.hideProgress(listingList.size, totalResultCount)
                    view.showError(it.localizedMessage)
                }).also {
                    compositeDisposable.add(it)
                }

        searchPublishProcessor.onBackpressureDrop()
/*                .concatMap {
                    apiService.suggestions(
                            categoryId = currentCategory?.number?.toIntOrNull(),
                            searchString = if (it.isEmpty()) null else it )
                            .subscribeOn(Schedulers.io()) }*/

                .concatMap {
                    apiService.suggestions(
                            categoryId = 0,
                            searchString = "iphone" )
                            .subscribeOn(Schedulers.io()) }


                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
//                    view.updateSearchSuggestion(it.toList())
                    val list = it.categorySuggestions?.map { it.name }?.toList()
                    Timber.d("searchPublishProcessor list: $list")

                }, {
                    view.showError(it.localizedMessage)
                }).also {
                    compositeDisposable.add(it)
                }



/*        searchPublishProcessor.onBackpressureDrop()
                .concatMap {
                    apiService.suggestions(
                            categoryId = currentCategory?.number?.toIntOrNull(),
                            searchString = if (it.isEmpty()) null else it )
                            .subscribeOn(Schedulers.io()) }
                .observeOn(AndroidSchedulers.mainThread())
//                .concatMap { Flowable.fromArray(it.categorySuggestions) }
                .flatMap {
                    Flowable.just(it.categorySuggestions.asOptional())
                }
                .flatMap {
                    if (it.isPresent) {
                        Flowable.fromIterable(it.value as List<CategorySuggestion>)
                    } else {
                        Flowable.just(None)
                    }
                }
                .flatMap { it }
                .flatMapIterable { it.categorySuggestions }
                .concatMap { Flowable.just(it.name) }
                .toList()
                .subscribe({
                    view.updateSearchSuggestion(it.toList())
                }, {
                    view.showError(it.localizedMessage)
                })*/

//                .subscribe { view.updateListings(it.categorySuggestions?) }



    }

    override fun onViewCreated() {
        super.onViewCreated()

        onSelectCategory(null) // load all listings
        loadCategories()

        searchPublishProcessor.onNext("hi")
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
//            if (this.currentCategory != null) compositeDisposable.clear()

            this.currentCategory = currentCategory
            this.currentPage = 0
            this.totalResultCount = -1


            val to = listingList.size
            listingList.clear()
            view.updateListings(listingList, 0, to, ListingView.Notify.CLEAR)
            // loading indicator (can't be bothered)

            val newCategory = currentCategory ?: rootCategory
            if (newCategory != null) view.setCurrentCategory(newCategory)

            loadMoreListings(INITIAL_LOAD_PAGES)
        } else {
            Timber.d("onSelectCategory selecting the same category: ${currentCategory?.name}")
            val currentTime = System.currentTimeMillis()
            tabLastTabTime = when {
                currentTime - tabLastTabTime < SCROLL_TO_TOP_DELAY_MS -> { scrollToTop(); 0 }
                else -> { currentTime }
            }
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


    /** search */
    override fun onQueryTextChange(newText: String?) {
        TODO("not implemented")
    }

    override fun onQueryTextSubmit(query: String?) {
        TODO("not implemented")
    }
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