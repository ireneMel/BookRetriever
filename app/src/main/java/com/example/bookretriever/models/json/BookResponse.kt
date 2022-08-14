package com.example.bookretriever.models.json

import com.google.gson.annotations.SerializedName

data class BookResponse(
//    val Q: String,
    val works: List<DocsItemBook>,
//    val numFound: Int,
//    val start: Int,
//    val numFoundExact: Boolean
)

data class DocsItemBook(
    val title: String,
    @SerializedName("author_name")
    val authorName: List<String>,
//    @SerializedName("edition_key")
//    val editionKey: List<String>,
    val isbn: String?,
    val cover_i: String?
)

