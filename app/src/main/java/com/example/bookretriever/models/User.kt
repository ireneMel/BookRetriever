package com.example.bookretriever.models

data class User(
    val name: String?,
    val email: String?,
    val password: String?
    //TODO save encoded password
)