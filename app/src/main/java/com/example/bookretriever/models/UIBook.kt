package com.example.bookretriever.models

import kotlinx.coroutines.flow.StateFlow

data class UIBook(
    var title: String? = null,
    var author: String? = null,
    val coverI: String? = null,
    val onClick: () -> Unit,
    val onLongClick: () -> Unit,
    var isLiked: () -> Boolean
) {
    // Get medium sized book cover from covers API
    val coverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-M.jpg?default=false"

    // Get large sized book cover from covers API
    val largeCoverUrl: String
        get() = "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
}