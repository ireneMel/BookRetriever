package com.example.bookretriever.di

import android.content.Context
import androidx.room.Room
import com.example.bookretriever.databases.BooksDatabase
import com.example.bookretriever.databases.Converters
import com.example.bookretriever.databases.dao.BooksDao
import com.example.bookretriever.net.IBookClient
import com.example.bookretriever.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //TODO ctrl shift j merge next line

    @Provides
    @Singleton
    fun provideDao(database: BooksDatabase): BooksDao = database.getDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BooksDatabase::class.java,
        "books_database"
    ).addTypeConverter(Converters())
        .build()

    @Provides
    @Singleton
    fun provideClient(): IBookClient {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .build()
        return retrofit.create(IBookClient::class.java)
    }

}