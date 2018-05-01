package com.example.derek.trademeapi.api

import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateTime
import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.SearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import trademe.demo.derek.trademedemo.model.ListedItemDetail

interface TradeMeApiService {
    // todo: could be simplified using @QueryMap?
    @GET("Search/General.json")
    fun search(@Query("search_string") query: String? = null,
               @Query("category") category: String? = null,
               @Query("buy") buy: TradeMeEnum.Search.Buy? = null,
               @Query("clearance") clearance: TradeMeEnum.Search.Clearance? = null,
               @Query("condition") condition: TradeMeEnum.Search.Condition? = null,
               @Query("date_from") dateFrom: TradeMeDateTime? = null,
               @Query("listed_as") listedAs: TradeMeEnum.Search.ListedAs? = null,
               @Query("page") page: Int? = null,
               @Query("rows") rows: Int? = null,
               @Query("return_did_you_mean") returnDidYouMean: Boolean? = true,
               @Query("sort_order") sortOrder: TradeMeEnum.Search.SortOrder? = null
    ): Observable<SearchResult>


    @GET("Categories/{number}.json")
    fun category(@Path(value = "number", encoded = true) rootCategory: String = "0",
                  @Query("depth") depth: Int? = null,
                  @Query("region") page: Int? = null,
                  @Query("with_counts") withCounts: Boolean? = false): Observable<Category>

    @GET("Listings/{listingId}.json")
    fun listing(@Path(value = "listingId", encoded = true) listingId: Int
    ): Observable<ListedItemDetail>


}


enum class TradeMeSearch(val value: String) {
    ALL("All"),
    BUY_NOW("BuyNow"),
}

class TradeMeEnum {
    class Search {
        enum class Buy(val value: String) {
            ALL("All"),
            BUY_NOW("BuyNow"),
        }

        enum class Clearance(val value: String) {
            ALL("All"),
            CLEARANCE("Clearance"),
            ON_SALE("OnSale"),
        }

        enum class Condition(val value: String) {
            ALL("All"),
            NEW("New"),
            USED("Used"),
        }

        enum class Pay(val value: String) {
            ALL("All"),
            PAY_NOW("PayNow")
        }

        enum class PhotoSize(val value: String) {
            THUMBNAIL("Thumbnail"),
            LIST("List"),
            MEDIUM("Medium"),
            GALLERY("Gallery"),
            LARGE("Large"),
            FULL_SIZE("FullSize")
        }

        enum class ListedAs(val value: String) {
            ALL("All"),
            AUCTIONS("Auctions"),
            CLASSIFIEDS("Classifieds")
        }

        enum class SortOrder(val value: String) {
            Default("Default"),
            FeaturedFirst("FeaturedFirst"),
            SuperGridFeaturedFirst("SuperGridFeaturedFirst"),
            TitleAsc("TitleAsc"),
            ExpiryAsc("ExpiryAsc"),
            ExpiryDesc("ExpiryDesc"),
            PriceAsc("PriceAsc"),
            PriceDesc("PriceDesc"),
            BidsMost("BidsMost"),
            BuyNowAsc("BuyNowAsc"),
            BuyNowDesc("BuyNowDesc"),
            ReviewsDesc("ReviewsDesc"), //services listings only
            LargestDiscount("LargestDiscount"),
            // jobs only
            BestMatch("BestMatch"),
            HighestSalary("HighestSalary"),
            LowestSalary("LowestSalary"),
            // motors only
            LowestKilometres("LowestKilometres"),
            HighestKilometres("HighestKilometres"),
            NewestVehicle("NewestVehicle"),
            OldestVehicle("OldestVehicle")
        }

    }


}
