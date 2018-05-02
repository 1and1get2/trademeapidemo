package com.example.derek.trademeapi.api.moshiadapters

import com.example.derek.trademeapi.model.Category
import com.example.derek.trademeapi.model.CategoryJson
import com.squareup.moshi.FromJson
import timber.log.Timber

/**
 * Created by derek on 2/05/18.
 */

class CategoryAdapter {
    @FromJson fun fromJson(categoryJson: CategoryJson) : Category{
        Timber.d("CategoryAdapter fromJson: $categoryJson")
        val category = Category()
        category.number = categoryJson.Number
        category.canBeSecondCategory = categoryJson.CanBeSecondCategory
        category.canHaveSecondCategory = categoryJson.CanHaveSecondCategory
        category.hasClassifieds = categoryJson.HasClassifieds
        category.isLeaf = categoryJson.IsLeaf
        category.name = categoryJson.Name
        category.path = categoryJson.Path

        category.subcategories = categoryJson.Subcategories

        category.subcategories?.let {
            for(c : Category in it) {
                c.parent = category
            }
        }

        return category
    }
/*    @ToJson fun toJson(category: Category) : CategoryJson {
        Timber.d("CategoryAdapter fromJson: $category")
        val categoryJson = CategoryJson()
        categoryJson.Name = category.name
        return categoryJson
    }*/
}