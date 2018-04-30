package com.example.derek.trademeapi.model

import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateTime
import com.squareup.moshi.Json

data class Listing(
        @Json(name = "ListingId") val listingId: Int,
        @Json(name = "Title") val title: String,
        @Json(name = "Category") val category: String,
        @Json(name = "StartPrice") val startPrice: Float,
        @Json(name = "BuyNowPrice") val buyNowPrice: Float?,
        @Json(name = "StartDate") val startDate: TradeMeDateTime?,
        @Json(name = "EndDate") val endDate: TradeMeDateTime?,
        @Json(name = "ListingLength") var listingLength: Any? = null,
        @Json(name = "IsFeatured") val isFeatured: Boolean?,
        @Json(name = "HasGallery") val hasGallery: Boolean?,
        @Json(name = "IsBold") val isBold: Boolean?,
        @Json(name = "AsAt") val asAt: TradeMeDateTime?,
        @Json(name = "CategoryPath") val categoryPath: String?,
        @Json(name = "PictureHref") val pictureHref: String?,
        @Json(name = "IsNew") val isNew: Boolean?,
        @Json(name = "Subtitle") var subtitle: String? = null,
        @Json(name = "PriceDisplay") var priceDisplay: String? = null,
        @Json(name = "PhotoUrls") var photoUrls: List<String>? = null,
        @Json(name = "Region") val region: String?,
        @Json(name = "Suburb") val suburb: String?,
        @Json(name = "HasReserve") val hasReserve: Boolean?,
        @Json(name = "HasBuyNow") val hasBuyNow: Boolean?,
        @Json(name = "NoteDate") val noteDate: TradeMeDateTime?,
        @Json(name = "ReserveState") val reserveState: Int?,
        @Json(name = "PromotionId") val promotionId: Int?,
        @Json(name = "__type") var type: String? = null,
        @Json(name = "AdditionalData") var additionalData: AdditionalData? = null
)

data class FoundCategory(
        @Json(name = "Count") val count: Int,
        @Json(name = "Category") val category: String?,
        @Json(name = "CategoryId") val categoryId: Int?,
        @Json(name = "Name") val name: String? = null
)

data class SearchResult(
        @Json(name = "TotalCount") val totalCount: Int,
        @Json(name = "TotalCountTruncated") val totalCountTruncated: Boolean?,
        @Json(name = "Page") val page: Int?,
        @Json(name = "PageSize") val pageSize: Int?,
        @Json(name = "List") val list: List<Listing>,
        @Json(name = "DidYouMean") val didYouMean: String?,
        @Json(name = "FoundCategories") val foundCategory: List<FoundCategory>?
)


data class AdditionalData(
        @Json(name = "BulletPoints") var bulletPoints: List<String>? = null,
        @Json(name = "Tags") var tags: List<Tag>? = null
)

data class Tag(
        @Json(name = "Name") val name: String
)