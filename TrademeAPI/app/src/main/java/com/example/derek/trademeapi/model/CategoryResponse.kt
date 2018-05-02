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

        var parent: Category? = null
) {
    override fun toString(): String {
        return "Category number: $number, Name: $name"//, parent: ${parent?.let { parent[0]?.name }} "
    }
}


data class CategoryJson(
        val Number: String? = null,
        var CanBeSecondCategory: Boolean? = null,
        var CanHaveSecondCategory: Boolean? = null,
        var HasClassifieds: Boolean? = null,
        var IsLeaf: Boolean = false,
        var Name: String? = null,
        var Path: String? = null,
        var Subcategories: List<Category>? = null
)