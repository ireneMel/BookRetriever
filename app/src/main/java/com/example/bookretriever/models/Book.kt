package com.example.bookretriever.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//by default Covers API returns a blank image if the cover cannot be found.
// If you append ?default=false to the end of the URL, then it returns a 404 instead

@Entity(tableName = "books")
data class Book(
    val isbnId: String?,
    var title: String? = null,
    var author: String? = null,
    val coverI: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    // Get medium sized book cover from covers API
    val coverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-M.jpg?default=false"

    // Get large sized book cover from covers API
    val largeCoverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
}