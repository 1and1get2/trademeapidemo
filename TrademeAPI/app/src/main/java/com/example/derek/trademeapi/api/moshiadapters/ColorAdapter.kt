package com.example.derek.trademeapi.api.moshiadapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson



/** Converts strings like #ff0000 to the corresponding color ints.  */
internal class ColorAdapter {
    @Retention(AnnotationRetention.RUNTIME)
    @JsonQualifier
    annotation class HexColor

    @ToJson
    fun toJson(@HexColor rgb: Int): String {
        return String.format("#%06x", rgb)
    }

    @FromJson
    @HexColor
    fun fromJson(rgb: String): Int {
        return Integer.parseInt(rgb.substring(1), 16)
    }
}



