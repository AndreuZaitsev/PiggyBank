package com.example.piggybank

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.piggybank.fragments.AboutApp
import com.example.piggybank.fragments.EditExpensesFragment
import com.example.piggybank.fragments.EditIncomeFragment
import com.example.piggybank.fragments.FAQ
import com.example.piggybank.fragments.MainFragment
import com.example.piggybank.fragments.SplashFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val navController by lazy{
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
    }

  /*  override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    } */

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)

    }

     fun setupDrawerLayout() {
            navigationView.setupWithNavController(navController)
            NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }
    fun setUpActionBar(toolbar: Toolbar) {
       setSupportActionBar(toolbar)
            appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment, R.id.splashFragment))
            setupActionBarWithNavController(navController, appBarConfiguration)
        setupDrawerLayout()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}