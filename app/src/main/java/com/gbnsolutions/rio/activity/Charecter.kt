package com.gbnsolutions.rio.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.FragmentAdapter
import com.gbnsolutions.rio.fragment.HomeFragment
import com.gbnsolutions.rio.fragment.ProfileFragment
import com.gbnsolutions.rio.fragment.StatusFragment
import com.google.android.material.tabs.TabLayout


class Charecter : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tab: TabLayout
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charecter)
        viewPager = findViewById(R.id.viewpager)
        tab = findViewById(R.id.tab_Layout)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()
        setupTabs()
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "RIO"
    }

    private fun setupTabs() {
        val adapter = FragmentAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(ProfileFragment(), "Profile")
        adapter.addFragment(StatusFragment(), "Status")
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)
    }
}