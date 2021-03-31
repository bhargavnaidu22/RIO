package com.gbnsolutions.rio

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Charecter : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var details: TextView
    lateinit var imageView: ImageView
    private var mAuth: FirebaseAuth? = null
    private var fstore: FirebaseStorage?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charecter)
        textView = findViewById(R.id.charecter)
        imageView = findViewById(R.id.photo)
        details = findViewById(R.id.details)
        mAuth = FirebaseAuth.getInstance()
        fstore = FirebaseStorage.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val photo = fstore!!.getReference("Login")
        
    }
}