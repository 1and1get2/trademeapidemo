package com.example.derek.trademeapi.ui.listings

import com.example.derek.trademeapi.api.TradeMeApiService
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.Listing
import com.example.derek.trademeapi.util.checkMainThread
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
 * http://www.baeldung.com/rxjava-backpressure
 * Dealing with Backpressure with RxJava
 */
class ListingPresenterImpl @Inject constructor(override val view: ListingView) : ListingPresenter {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    @Inject
    lateinit var apiService: TradeMeApiService

    private var rootCategory: Category? = null
    private var currentCategory: Category? = null

    private val listingList = ArrayList<Listing>(ITEMS_PER_ROW * 3)

    private var currentListingPosition = 0
    private var currentPage: Int = 0 // page number starts at 1
    private var totalResultCount: Int = -1 //reached the end of the results

    private var currentSearch: String? = null

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()
//    private val paginatorDisposable: Disposable

    private var paginationSubscription: Subscription? = null


    private val searchSuggestionPublishProcessor: PublishProcessor<String> = PublishProcessor.create()


    private var tabLastTabTime: Long = 0

    companion object {
        const val INITIAL_LOAD_PAGES = 1
        const val ITEMS_PER_ROW = 60
        const val SCROLL_TO_TOP_DELAY_MS = 500
    }

    init {
        paginator
                .onBackpressureLatest()
//                .onBackpressureDrop()
                .doOnNext { view.showProgress() }
                .concatMap { Flowable.range(currentPage + 1, it) }
                .concatMap { Flowable.just(it) }
                .doOnNext { currentPage = it }
                .filter { newPage ->
                    // skip further request when already reached the end of the result
                    (totalResultCount == -1 || totalResultCount >= (newPage - 1) * ITEMS_PER_ROW)
                }
                .concatMap {
                    val message = "apiService listing query:$currentSearch, category:${currentCategory?.number}, page:$it"
                    Timber.d(message)
                    apiService.search(
                            query = currentSearch, category = currentCategory?.number,
                            page = it, rows = ITEMS_PER_ROW).subscribeOn(Schedulers.io())
                }
                .doOnNext {
                    currentPage++
                    totalResultCount = it.totalCount
                }
                // update current page number from the backend
                .doOnNext { it.page?.also { currentPage = it } }
                .doOnCancel { Timber.d("canceling old apiService.search request") }
                .observeOn(AndroidSchedulers.mainThread(), false, 1)
                .subscribe({
                    listingList.addAll(it.list)
                    view.hideProgress(listingList.size, totalResultCount)
                    view.updateListings(listingList.toList())

                }, {
                    view.hideProgress(listingList.size, totalResultCount)
                    view.showError("paginator: $it")
                    Timber.e("paginator: $it")
                }).also {
                    compositeDisposable.add(it)
                }

        searchSuggestionPublishProcessor
                .onBackpressureLatest()
//                .buffer(1)
                .filter{( it.isNotEmpty()).also { if (!it) {Timber.e("search str is empty")} }}
                .concatMap {
                    Timber.d("apiService.suggestions searchString: $it")
                    apiService.suggestions(
                            categoryId = 0, // TODO: ????? what is this parameter?
                            searchString = if (it.isEmpty()) null else it).subscribeOn(Schedulers.io()) }

                .doOnCancel { Timber.d("canceling old apiService.search suggestion") }
                .observeOn(AndroidSchedulers.mainThread(), false, 1)
                .subscribe({
                    val list: List<String> = it.categorySuggestions?.filter { it.name != null }?.
                            map { it.name!! }?.distinct()?.toList() ?: listOf()
                    Timber.d("updateSearchSuggestion: $list")
                    checkMainThread()
                    view.updateSearchSuggestion(list)

                }, {
                    view.showError("searchSuggestionPublisher: $it")
                    Timber.e("searchSuggestionPublisher: $it")
                }).also {
                    compositeDisposable.add(it)
                }
    }

    override fun onViewCreated() {
        super.onViewCreated()

        onSelectCategory(null) // load all listings
        loadCategories()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        compositeDisposable.clear()
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


    private fun reset() {
        listingList.clear()
        totalResultCount = -1
        currentListingPosition = 0
        currentPage = 0
    }

    /** listings */

    override fun scrollToTop() {
        currentListingPosition = 0
        view.scrollToPosition(0)
    }

    /**
     * updates listings
     * if category = null then load all listings
     * */
    override fun onSelectCategory(currentCategory: Category?) {
        if (this.currentCategory == null || this.currentCategory != currentCategory) {

            this.currentCategory = currentCategory
            reset()
            view.updateListings(listingList.toList())

            val newCategory = currentCategory ?: rootCategory
            if (newCategory != null) view.setCurrentCategory(newCategory)

            loadMoreListings(INITIAL_LOAD_PAGES)
        } else {
            Timber.d("onSelectCategory selecting the same category: ${currentCategory?.name}")
            val currentTime = System.currentTimeMillis()
            tabLastTabTime = when {
                currentTime - tabLastTabTime < SCROLL_TO_TOP_DELAY_MS -> {
                    scrollToTop(); 0
                }
                else -> { currentTime }
            }
        }
    }

    override fun loadMoreListings(page: Int): Boolean {
        if (totalResultCount != -1 && totalResultCount <= listingList.size) return false // reached the end of the results
        Timber.d("loadMoreListings: $page")
        paginator.onNext(page)
        return true
    }

    /** search */
    override fun onQueryTextChange(newText: String?) {

        if (newText != currentSearch /*|| currentPage == 0 || totalResultCount == -1*/) {
            reset()
            currentSearch = newText
            // TODO: uncomment this
            loadMoreListings(INITIAL_LOAD_PAGES)
            if (newText == null || newText.isEmpty()) {
                view.updateSearchSuggestion(emptyList<String>())
            } else {
                Timber.d("onQueryTextChange searchSuggestionPublishProcessor: $newText")
                searchSuggestionPublishProcessor.onNext(newText)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?) {
        // not worried
        Timber.d("onQueryTextSubmit: $query")
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