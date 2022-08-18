package com.example.bookretriever.databases

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bookretriever.models.Book
import com.google.gson.Gson

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun bookToList(books: List<Book>): String {
        return Gson().toJson(books)
    }

    @TypeConverter
    fun listToBook(book: String): List<Book> {
        return listOf(Gson().fromJson(book, Book::class.java))
    }
//    fun bitmapToBytes(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return outputStream.toByteArray()
//    }

//    @TypeConverter
//    fun bytesToBitmap(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }
}