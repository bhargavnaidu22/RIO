package com.gbnsolutions.rio.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.gbnsolutions.rio.MainActivity
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.FragmentAdapter
import com.gbnsolutions.rio.fragment.HomeFragment
import com.gbnsolutions.rio.fragment.ProfileFragment
import com.gbnsolutions.rio.fragment.StatusFragment
import com.gbnsolutions.rio.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class Charecter : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tab: TabLayout
    lateinit var toolbar: Toolbar
    lateinit var username: TextView
    lateinit var profile: ImageView
    var refUsers: DatabaseReference?= null
    var firebaseUser: FirebaseUser?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charecter)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        viewPager = findViewById(R.id.viewpager)
        username = findViewById(R.id.user_name)
        profile = findViewById(R.id.profile_image)
        tab = findViewById(R.id.tab_Layout)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()
        setupTabs()

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user: Users?= snapshot.getValue(Users::class.java)
                    username.text = user!!.getUserName()
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            R.id.edit_profile -> {
                editprofile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun logout() {
        val p = "a"
        val i = Intent(this@Charecter, MainActivity::class.java)
        i.putExtra("logout", p)
        startActivity(i)
        finish()
    }
    private fun editprofile(){
        val i = Intent(this@Charecter,EditProfile::class.java)
        startActivity(i)
    }

    private fun setupTabs() {
        val adapter = FragmentAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Chat")
        adapter.addFragment(StatusFragment(), "Status")
        adapter.addFragment(ProfileFragment(), "Profile")
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)
    }
}