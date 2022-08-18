package com.example.bookretriever.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookretriever.BuildConfig
import com.example.bookretriever.R
import com.example.bookretriever.ui.fragments.MainFragment
import com.example.bookretriever.ui.fragments.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

//String myApiKey = BuildConfig.API_KEY;

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//       val a = BuildConfig.API_KEY

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()

    }
}