package com.example.derek.trademeapi.api.moshiadapters


/*
class RealmListAdapter<T : RealmModel>(
        val elementAdapter: JsonAdapter<T>) : JsonAdapter<RealmList<T>>() {

    companion object {

        val FACTORY: Factory = Factory { type, annotations, moshi ->
            val rawType: Class<*> = Types.getRawType(type)

            if (rawType == RealmList::class.java) {
                val elementType = Types.collectionElementType(type, RealmList::class.java)
                val elementAdapter = moshi.adapter<RealmModel>(elementType)
                return@Factory RealmListAdapter(elementAdapter).nullSafe()
            }
            null
        }
    }

    override fun fromJson(reader: JsonReader): RealmList<T> {

        val result = RealmList<T>()
        reader.beginArray()
        while (reader.hasNext()) {
            result.add(elementAdapter.fromJson(reader))
        }
        reader.endArray()
        return result
    }

    override fun toJson(writer: JsonWriter?, value: RealmList<T>?) {
        if (writer != null && value != null) {
            writer.beginArray()
            for (element in value) {
                elementAdapter.toJson(writer, element)
            }
            writer.endArray()
        }
    }
}*/
