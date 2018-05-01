package com.example.derek.trademeapi.model

import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateTime
import com.squareup.moshi.Json

data class Listing(
        @Json(name = "ListingId") val listingId: Int,
        @Json(name = "Title") val title: String,
        @Json(name = "Category") val category: String,
        @Json(name = "StartPrice") val startPrice: Float,
        @Json(name = "BuyNowPrice") val buyNowPrice: Float? = null,
        @Json(name = "StartDate") val startDate: TradeMeDateTime? = null,
        @Json(name = "EndDate") val endDate: TradeMeDateTime? = null,
        @Json(name = "ListingLength") var listingLength: Any? = null,
        @Json(name = "IsFeatured") val isFeatured: Boolean? = null,
        @Json(name = "HasGallery") val hasGallery: Boolean? = null,
        @Json(name = "IsBold") val isBold: Boolean? = null,
        @Json(name = "AsAt") val asAt: TradeMeDateTime? = null,
        @Json(name = "CategoryPath") val categoryPath: String? = null,
        @Json(name = "PictureHref") val pictureHref: String? = null,
        @Json(name = "IsNew") val isNew: Boolean? = null,
        @Json(name = "Region") val region: String? = null,
        @Json(name = "Suburb") val suburb: String? = null,
        @Json(name = "HasReserve") val hasReserve: Boolean? = null,
        @Json(name = "HasBuyNow") val hasBuyNow: Boolean? = null,
        @Json(name = "NoteDate") val noteDate: TradeMeDateTime? = null,
        @Json(name = "ReserveState") val reserveState: Int? = null,
        @Json(name = "Subtitle") var subtitle: String? = null,
        @Json(name = "PriceDisplay") var priceDisplay: String? = null,
        @Json(name = "PromotionId") val promotionId: Int? = null,
        @Json(name = "PhotoUrls") var photoUrls: List<String>? = null,
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