package com.gbnsolutions.rio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ViewProfile : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var email: TextView
    lateinit var phone: TextView
    lateinit var profile: ImageView
    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val gbn = intent.getStringExtra("uid").toString()
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(gbn)
        textView = findViewById(R.id.a)
        email = findViewById(R.id.b)
        phone = findViewById(R.id.c)
        profile = findViewById(R.id.profile)
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user: Users?= snapshot.getValue(Users::class.java)
                    textView.text = user!!.getUserName()
                    email.text = user.getEmail()
                    phone.text = user.getPhone()
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}