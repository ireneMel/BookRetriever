package com.example.bookretriever.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookretriever.R
import com.example.bookretriever.databinding.ActivityMainBinding
import com.example.bookretriever.utils.internet_connectivity.ConnectivityObserver
import com.example.bookretriever.utils.internet_connectivity.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

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

        invalidateOptionsMenu()

        Log.d("MainActivity", "onCreate: ")

        observeConnection()

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

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private fun observeConnection() {

        connectivityObserver.observe().onEach {
            when (it) {
                Status.Available -> {
                    changeLayoutConnectionAvailable()
                }
                Status.Unavailable, Status.Lost, Status.Losing -> {
                    changeLayoutConnectionLost()
                }
//                Status.Losing -> {
//                    alertDialog.show()
//                }
//                Status.Lost -> {
//                    alertDialog.show()
//                }
            }
            //implement action on every change of status
        }.launchIn(lifecycleScope)
    }

    private fun changeLayoutConnectionAvailable() {
//        noConnection.visible()
    }

    private fun changeLayoutConnectionLost() {
//        noConnection.invisible()
        AlertDialog.Builder(this@MainActivity)
            .setTitle("No internet connection")
            .setMessage("The search will not show any results until the internet won't be turned on")
            .setNeutralButton("Ok") { _, _ -> }
            .create()
            .show()
    }
}

