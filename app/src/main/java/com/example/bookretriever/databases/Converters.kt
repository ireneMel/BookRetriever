package com.example.bookretriever.databases

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bookretriever.models.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun bookToList(books: List<Book>): String {
        return Gson().toJson(books)
    }

    @TypeConverter
    fun listToBook(book: String): List<Book> {
        return Gson().fromJson(book, Array<Book>::class.java).toList()
    }

}