package com.example.derek.trademeapi.model

import com.squareup.moshi.Json


open class Category(
        @Json(name = "Number") var number: String? = null,
        @Json(name = "CanBeSecondCategory") var canBeSecondCategory: Boolean? = null,
        @Json(name = "CanHaveSecondCategory") var canHaveSecondCategory: Boolean? = null,
        @Json(name = "HasClassifieds") var hasClassifieds: Boolean? = null,
        @Json(name = "IsLeaf") var isLeaf: Boolean = false,
        @Json(name = "Name") var name: String? = null,
        @Json(name = "Path") var path: String? = null,
        @Json(name = "Subcategories") var subcategories: List<Category>? = null,

        val parent: List<Category>? = null, // for automatic linking
        var parentInMemory: Category? = null
) {
    override fun toString(): String {
        return "Category number: $number, Name: $name"//, parent: ${parent?.let { parent[0]?.name }} "
    }
}

