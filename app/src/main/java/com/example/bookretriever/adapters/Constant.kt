package com.example.bookretriever.adapters

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookretriever.R

class Constant {
    companion object {
        fun getGlide(view: View, url: String) = Glide.with(view.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_launcher_foreground)
    }
}