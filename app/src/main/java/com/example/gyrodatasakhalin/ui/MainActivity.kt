package com.example.gyrodatasakhalin.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.fragments.battery.AddBatteryFragment
import com.example.gyrodatasakhalin.fragments.battery.BatteryFragment
import com.example.gyrodatasakhalin.fragments.dashboard.DashboardFragment
import com.example.gyrodatasakhalin.fragments.job.AddJobFragment
import com.example.gyrodatasakhalin.fragments.job.JobFragment
import com.example.gyrodatasakhalin.fragments.tool.AddToolFragment
import com.example.gyrodatasakhalin.fragments.tool.ToolFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fab_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Job


class MainActivity : AppCompatActivity() {

    private val rotationOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotationClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fbAddMain: FloatingActionButton = findViewById(R.id.fbAddMain)

//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        setupBottomNavMenu(navController)
//        setupActionBar(navController)



        fbAddMain.setOnClickListener {
            onAddMainButtonClicked()
        }

        fbAddToolButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {

                replace(R.id.fragment_container, AddToolFragment())
                    .commit()
            }
        }
        fbAddBatteryButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {

                replace(R.id.fragment_container, AddBatteryFragment())
                    .commit()
            }
        }
        fbAddJobButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, AddJobFragment())
                    .commit()
            }
        }

        val dashboardFragment = DashboardFragment()
        val toolFragment = ToolFragment()
        val batteryFragment = BatteryFragment()
        val jobFragment = JobFragment()

        makeCurrentFragment(dashboardFragment)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.destination_dashboard -> makeCurrentFragment(dashboardFragment)
                R.id.destination_tool -> makeCurrentFragment(toolFragment)
                R.id.destination_battery -> makeCurrentFragment(batteryFragment)
                R.id.destination_job -> makeCurrentFragment(jobFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
            bottom_nav?.let {
                NavigationUI.setupWithNavController(it, navController)
            }
        }

        private fun setupActionBar(navController: NavController) {
            NavigationUI.setupActionBarWithNavController(this, navController)
        }


//        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//            val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
//            Log.i("NAVIGATED: ", navigated.toString())
//            return navigated || super.onOptionsItemSelected(item)
//        }

        private fun onAddMainButtonClicked() {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked              // if(!clicked) clicked = true else clicked = false
        }

        private fun setAnimation(clicked: Boolean) {
            if (!clicked) {
                fbAddToolButton.startAnimation(fromBottom)
                fbAddBatteryButton.startAnimation(fromBottom)
                fbAddJobButton.startAnimation(fromBottom)
                fbAddMain.startAnimation(rotationOpen)
            } else {
                fbAddToolButton.startAnimation(toBottom)
                fbAddBatteryButton.startAnimation(toBottom)
                fbAddJobButton.startAnimation(toBottom)
                fbAddMain.startAnimation(rotationClose)
            }
        }

        private fun setVisibility(clicked: Boolean) {
            if (!clicked) {
                fbAddToolButton.visibility = View.VISIBLE
                fbAddBatteryButton.visibility = View.VISIBLE
                fbAddJobButton.visibility = View.VISIBLE
            } else {
                fbAddToolButton.visibility = View.INVISIBLE
                fbAddBatteryButton.visibility = View.INVISIBLE
                fbAddJobButton.visibility = View.INVISIBLE
            }
        }
    }

