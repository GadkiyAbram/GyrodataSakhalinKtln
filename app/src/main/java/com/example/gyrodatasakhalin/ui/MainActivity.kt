package com.example.gyrodatasakhalin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import com.example.gyrodatasakhalin.fragments.BatteryFragment
import com.example.gyrodatasakhalin.fragments.DashboardFragment
import com.example.gyrodatasakhalin.fragments.JobFragment
import com.example.gyrodatasakhalin.fragments.ToolFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupBottomNavMenu(navController)
        setupActionBar(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        Log.i("NAVIGATED: ", navigated.toString())
        return navigated || super.onOptionsItemSelected(item)
    }
}