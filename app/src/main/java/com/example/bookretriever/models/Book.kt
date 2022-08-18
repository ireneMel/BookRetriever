package com.example.bookretriever.models

//TODO make different fields in Book and UIBook

data class Book(
    val isbnId: String?,
    var title: String? = null,
    var author: String? = null,
    val coverI: String? = null
) {
    // Get medium sized book cover from covers API
    val coverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-M.jpg?default=false"

    // Get large sized book cover from covers API
    val largeCoverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
}