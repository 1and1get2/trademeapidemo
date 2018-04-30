package com.example.derek.trademeapi.api.moshiadapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.util.*


/**
 * Formats dates like {@code /Date(1517668772283)/}.
 */
class TradeMeDateTime(dateTime: Long){
    var dateTime : Long = dateTime
    constructor(dateTime: Date) : this(dateTime.time)

    override fun toString(): String {
        return "/Date($dateTime)/"
    }
    fun getDate(): Date {
        return Date(dateTime)
    }
}

class TradeMeDateJsonAdapter : JsonAdapter<TradeMeDateTime>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader?): TradeMeDateTime? {
        // TODO("implement unit test for cases like null <=> Date(0)")
        val dateTime = reader?.nextString()?.replace(("\\D+").toRegex(), "")?.toLong()
        dateTime?.let { return TradeMeDateTime(it) }
        return null
    }
    
    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter?, value: TradeMeDateTime?) {
        if (writer != null && value != null) {
            writer.value(value.toString())
        }
    }
}