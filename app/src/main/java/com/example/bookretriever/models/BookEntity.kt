package com.example.bookretriever.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    val timeInMillis: Long,
    val response: List<Book>,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)