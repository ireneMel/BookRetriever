package com.example.bookretriever.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.bookretriever.R
import com.example.bookretriever.ui.fragments.authorization.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

//String myApiKey = BuildConfig.API_KEY;

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            replace(R.id.fragment_container, LoginFragment())
        }
    }
}