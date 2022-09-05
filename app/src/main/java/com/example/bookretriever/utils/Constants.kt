package com.example.bookretriever.utils

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookretriever.R

object Constants {
    const val BASE_URL = "https://openlibrary.org"

    //refresh delta
    const val DELTA: Long = 86400000 //1 day

    //paging
    const val QUERY_LIMIT = 20

    fun getGlide(view: View, url: String) = Glide.with(view.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .placeholder(R.drawable.ic_launcher_foreground)

}
