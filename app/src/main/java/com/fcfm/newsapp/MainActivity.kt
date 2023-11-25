package com.fcfm.newsapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.fcfm.newsapp.data.SettingsDataStore
import com.fcfm.newsapp.data.dataStore
import com.fcfm.newsapp.network.UserProfile
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var settingsDataStore: SettingsDataStore

    companion object{
        var userProfile: UserProfile? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicializar LoggedInDataStore
        settingsDataStore = SettingsDataStore(applicationContext)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Modificar el HeaderNav para Desplegar que estas conectado
        val headerView: View = navigationView.getHeaderView(0)
        val displayLoggedUsername = headerView.findViewById<TextView>(R.id.user_username)
        val displayLoggedEmail = headerView.findViewById<TextView>(R.id.user_email)
        val displayLoggedPicture = headerView.findViewById<ImageView>(R.id.user_image)

        lifecycleScope.launch(Dispatchers.IO) {
            getUserProfile().collect(){
                withContext(Dispatchers.Main){
                    userProfile = it

                    displayLoggedEmail.text = it.email
                    displayLoggedUsername.text = it.username
                    val decodedString: ByteArray = Base64.decode(it.image, Base64.DEFAULT)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    displayLoggedPicture.setImageBitmap(decodedByte)

                    Log.d("LOGIN", userProfile.toString())

                }
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(this, navController)



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                navController.popBackStack()
            }


            R.id.nav_settings -> {
                navController.navigateUp()
                navController.navigate(R.id.action_homeFragment_to_userProfileFragment)
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
                navController.navigateUp()


                lifecycleScope.launch {
                    settingsDataStore.clearUserToPreferencesStore(applicationContext)

                }
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getUserProfile() = dataStore.data.map { preferences ->
        UserProfile(
            preferences[booleanPreferencesKey("is_user_logged_in")] ?: false,
            preferences[stringPreferencesKey("user_id")].orEmpty(),
            preferences[stringPreferencesKey("user_names")].orEmpty(),
            preferences[stringPreferencesKey("user_lastnames")].orEmpty(),
            preferences[stringPreferencesKey("user_email")].orEmpty(),
            preferences[stringPreferencesKey("user_username")].orEmpty(),
            preferences[stringPreferencesKey("user_password")].orEmpty(),
            preferences[stringPreferencesKey("user_image")].orEmpty(),
            preferences[stringPreferencesKey("user_role")].orEmpty()
        )
    }


    override fun onBackPressed() {
        super.onBackPressed()
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