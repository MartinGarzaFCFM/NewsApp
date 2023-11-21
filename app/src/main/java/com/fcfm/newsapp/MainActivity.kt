package com.fcfm.newsapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(this, navController)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home  -> navController.navigateUp()
            R.id.nav_settings -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_settingsFragment)
            }
            R.id.nav_share  -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_shareFragment)
            }
            R.id.nav_about -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
            }
            //Nav ReporterTools
            R.id.nav_create_news -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_createNewsFragment)
            }
            R.id.nav_see_news -> {

            }
            //Nav AdminTools
            R.id.nav_see_users -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_usersFragment)
            }
            //Login & Logout
            R.id.nav_register -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_registerFragment)
            }
            R.id.nav_login -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_loginFragment)
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}