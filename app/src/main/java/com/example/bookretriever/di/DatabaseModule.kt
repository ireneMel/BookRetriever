package com.example.bookretriever.di

import android.content.Context
import androidx.room.Room
import com.example.bookretriever.databases.Converters
import com.example.bookretriever.databases.shelf.ShelfConverters
import com.example.bookretriever.databases.shelf.ShelfDao
import com.example.bookretriever.databases.shelf.ShelfDatabase
import com.example.bookretriever.databases.trending.BooksDao
import com.example.bookretriever.databases.trending.BooksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //TODO ctrl shift j merge next line

    @Provides
    @Singleton
    fun provideBooksDao(database: BooksDatabase): BooksDao = database.getDao()

    @Provides
    @Singleton
    fun provideBooksDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BooksDatabase::class.java,
        "books_database"
    ).addTypeConverter(Converters()).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideShelfDao(database: ShelfDatabase): ShelfDao = database.getDao()

    @Provides
    @Singleton
    fun provideShelfDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ShelfDatabase::class.java,
        "shelf_database"
    ).fallbackToDestructiveMigration()
//        .addTypeConverter(ShelfConverters())
        .build()
}