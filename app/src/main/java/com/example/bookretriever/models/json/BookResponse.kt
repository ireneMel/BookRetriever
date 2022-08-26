package com.example.bookretriever.models.json

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val works: List<DocsItemBook>,
)

data class DocsItemBook(
    val isbn: String?,
    @SerializedName("author_name")
    val authorName: List<String>,
    val title: String,
    @SerializedName("first_publish_year")
    val firstPublishedYear: Int,
    val cover_i: String?,
    @SerializedName("has_fulltext")
    val hasFullText: Boolean,
    //for filter - genres
    val subject: List<String>?
)

