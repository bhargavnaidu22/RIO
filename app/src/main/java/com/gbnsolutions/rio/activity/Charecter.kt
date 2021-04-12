package com.gbnsolutions.rio.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.fragment.HomeFragment
import com.google.android.material.navigation.NavigationView


class Charecter : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    var previousMenuItem: MenuItem?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charecter)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        frameLayout = findViewById(R.id.frame)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()
        openHome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@Charecter,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
//                R.id.profile -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, ProfileFragment())
//                        .commit()
//                    supportActionBar?.title = "Profile"
//                    drawerLayout.closeDrawers()
//                }
//                R.id.favorites -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, FavouritesFragment())
//                        .commit()
//                    supportActionBar?.title = "Favourites"
//                    drawerLayout.closeDrawers()
//                }
//                R.id.test -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, TestFragment())
//                        .commit()
//                    supportActionBar?.title = "Test"
//                    drawerLayout.closeDrawers()
//                }
//                R.id.about -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, AboutFragment())
//                        .commit()
//                    supportActionBar?.title = "About"
//                    drawerLayout.closeDrawers()
//                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Description"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun openHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.home)
    }
}