package com.example.bookretriever.di

import com.example.bookretriever.net.IBookClient
import com.example.bookretriever.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideClient(): IBookClient {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .build()
        return retrofit.create(IBookClient::class.java)
    }

}