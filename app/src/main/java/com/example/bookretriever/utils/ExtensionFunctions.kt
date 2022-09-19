package com.example.bookretriever.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.fragment.app.FragmentActivity
import com.example.bookretriever.ui.MainActivity

object ExtensionFunctions {
    fun FragmentActivity.setActionBarText(title: String) {
        (this as MainActivity).supportActionBar?.title = title
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun Context.getThemeColor(@AttrRes color: Int) = TypedValue().run {
        theme.resolveAttribute(color, this, true)
        data
    }
}