package com.example.derek.trademeapi.model

import com.example.derek.trademeapi.api.moshiadapters.TradeMeDateTime
import com.squareup.moshi.Json

data class ListedItemDetail (
        @Json(name = "ListingId")
        var listingId: Int? = null,
        @Json(name = "Title")
        var title: String? = null,
        @Json(name = "Category")
        var category: String? = null,
        @Json(name = "StartPrice")
        var startPrice: Double? = null,
        @Json(name = "BuyNowPrice")
        var buyNowPrice: Double? = null,

        @Json(name = "StartDate")
        var startDate: TradeMeDateTime? = null,
        @Json(name = "EndDate")
        var endDate: TradeMeDateTime? = null,
        @Json(name = "ListingLength")
        var listingLength: Any? = null,
        @Json(name = "IsFeatured")
        var isFeatured: Boolean? = null,
        @Json(name = "HasGallery")
        var hasGallery: Boolean? = null,
        @Json(name = "IsBold")
        var isBold: Boolean? = null,
        @Json(name = "AsAt")
        var asAt: TradeMeDateTime? = null,
        @Json(name = "CategoryPath")
        var categoryPath: String? = null,
        @Json(name = "PhotoId")
        var photoId: Int? = null,
        @Json(name = "IsNew")
        var isNew: Boolean? = null,
        @Json(name = "RegionId")
        var regionId: Int? = null,
        @Json(name = "Region")
        var region: String? = null,
        @Json(name = "SuburbId")
        var suburbId: Int? = null,
        @Json(name = "Suburb")
        var suburb: String? = null,
        @Json(name = "HasReserve")
        var hasReserve: Boolean? = null,
        @Json(name = "HasBuyNow")
        var hasBuyNow: Boolean? = null,
        @Json(name = "NoteDate")
        var noteDate: TradeMeDateTime? = null,
        @Json(name = "CategoryName")
        var categoryName: String? = null,
        @Json(name = "ReserveState")
        var reserveState: Int? = null,
        @Json(name = "Attributes")
        var attributes: List<Any>? = null,
        @Json(name = "OpenHomes")
        var openHomes: List<Any>? = null,
        @Json(name = "Subtitle")
        var subtitle: String? = null,
        @Json(name = "MinimumNextBidAmount")
        var minimumNextBidAmount: Double? = null,
        @Json(name = "PriceDisplay")
        var priceDisplay: String? = null,
//        @Json(name = "AdditionalData")
//        var additionalData: AdditionalData? = null,
        @Json(name = "Member")
        var member: Member? = null,
        @Json(name = "Body")
        var body: String? = null,
        @Json(name = "Photos")
        var photos: List<Photo>? = null,
        @Json(name = "AllowsPickups")
        var allowsPickups: Int? = null,
//        @Json(name = "ShippingOptions")
//        var shippingOptions: List<ShippingOption>? = null,
        @Json(name = "PaymentOptions")
        var paymentOptions: String? = null,
        @Json(name = "CanAddToCart")
        var canAddToCart: Boolean? = null,
//        @Json(name = "EmbeddedContent")
//        var embeddedContent: EmbeddedContent? = null,
        @Json(name = "SupportsQuestionsAndAnswers")
        var supportsQuestionsAndAnswers: Boolean? = null,
//        @Json(name = "PaymentMethods")
//        var paymentMethods: List<PaymentMethod>? = null

        // local properties
        var saved: Boolean = false
)

// The seller of the listing.
data class Member (
        @Json(name = "MemberId")
        var memberId: Int? = null,
        @Json(name = "Nickname")
        var nickname: String? = null,
        @Json(name = "DateAddressVerified")
        var dateAddressVerified: TradeMeDateTime? = null,
        @Json(name = "DateJoined")
        var dateJoined: TradeMeDateTime? = null,
        @Json(name = "UniqueNegative")
        var uniqueNegative: Int? = null,
        @Json(name = "UniquePositive")
        var uniquePositive: Int? = null,
        @Json(name = "FeedbackCount")
        var feedbackCount: Int? = null,
        @Json(name = "IsAddressVerified")
        var isAddressVerified: Boolean? = null,
        @Json(name = "Suburb")
        var suburb: String? = null,
        @Json(name = "Region")
        var region: String? = null,
        @Json(name = "IsAuthenticated")
        var isAuthenticated: Boolean? = null,
        @Json(name = "Photo")
        var photo: Boolean? = null
)

data class Photo (
        @Json(name = "Key")
        var photoId: Int? = null,
        @Json(name = "Key")
        var value: PhotoUrl?
)

data class PhotoUrl (
        @Json(name = "Thumbnail")
        var thumbnail: String? = null,
        @Json(name = "List")
        var list: String? = null,
        @Json(name = "Medium")
        var medium: String? = null,
        @Json(name = "Gallery")
        var gallery: String? = null,
        @Json(name = "Large")
        var large: String? = null,
        @Json(name = "FullSize")
        var fullSize: String? = null,
        @Json(name = "PhotoId")
        var photoId: Int? = null,
        @Json(name = "OriginalWidth")
        var originalWidth: Int? = null,
        @Json(name = "OriginalHeight")
        var originalHeight: Int? = null
)