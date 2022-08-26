package com.example.bookretriever.databases

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bookretriever.models.Book
import com.google.gson.Gson

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun listToString(books: List<Book>): String {
        return Gson().toJson(books)
    }

    @TypeConverter
    fun stringToList(book: String): List<Book> {
        return Gson().fromJson(book, Array<Book>::class.java).toList()
    }
}