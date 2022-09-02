package com.example.bookretriever.databases.shelf

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class ShelfConverters {
    @TypeConverter
    fun listToString(categories: List<String>): String {
        if (categories == null) return ""
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun stringToList(categories: String): List<String> {
        if (categories == null) return emptyList()
        return Gson().fromJson(categories, Array<String>::class.java).toList()
    }
}