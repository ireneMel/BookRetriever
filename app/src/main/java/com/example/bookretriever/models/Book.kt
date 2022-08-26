package com.example.bookretriever.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO make different fields in Book and UIBook

@Entity(tableName = "books")
data class Book(
    val isbnId: String?,
    var author: String? = null,
    @PrimaryKey
    val title: String,
    val firstPublishedYear: Int,
    var coverI: String? = null,
    val hasFullText: Boolean?,
    var categories: String? = null
//    var categories: List<String>? = null
) {
    // Get medium sized book cover from covers API
    val coverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-M.jpg?default=false"

    // Get large sized book cover from covers API
    val largeCoverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
}