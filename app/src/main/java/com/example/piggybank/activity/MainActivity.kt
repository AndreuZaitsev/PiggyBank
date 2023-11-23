package com.example.piggybank.activity

import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.piggybank.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.mainFragment,
                R.id.editIncomeFragment,
                R.id.editExpensesFragment,
            ),
            drawerLayout = drawerLayout,
        )
    }

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navHostFragment.navController
    }

    val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            else -> onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setUpActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupNavigation()
    }

    private fun setupNavigation() {
        navigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(
            activity = this,
            navController = navController,
            configuration = appBarConfiguration
        )
    }
}

fun Fragment.attachToolbarToMainActivity(toolbar: Toolbar, @DrawableRes icon: Int) {
    (requireActivity() as? MainActivity)?.setUpActionBar(toolbar)
    toolbar.setNavigationIcon(icon)
}

val user = hashMapOf(
    "first" to "Ada",
    "last" to "Lovelace",
    "born" to 1815
)
val data = hashMapOf("city" to "Minsk")

const val TAG = "Users"