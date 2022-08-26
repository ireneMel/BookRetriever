package com.example.bookretriever.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookretriever.R
import com.example.bookretriever.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

//TODO on long or double click add to the shelf

//String myApiKey = BuildConfig.API_KEY;
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.fragment_navigation_container)
        navView.setupWithNavController(navController) //bottom navigation
        navView.setOnItemReselectedListener { /* NO-OP */ }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment,
                R.id.shelfFragment,
                R.id.profileFragment -> navView.visibility = View.VISIBLE
                R.id.loginFragment,
                R.id.registerFragment -> navView.visibility = View.GONE
            }
        }
    }

}

