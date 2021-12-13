package com.gbnsolutions.rio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.gbnsolutions.rio.R
import com.squareup.picasso.Picasso
import com.zolad.zoominimageview.ZoomInImageView

class ImagesView : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var img: ZoomInImageView
    lateinit var head: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        toolbar = findViewById(R.id.imgtoolbar)
        img = findViewById(R.id.zimage)
        img.setImageResource(R.drawable.logo)
        head = findViewById(R.id.head)
        val p = intent.getStringExtra("url")
        val q = intent.getStringExtra("usname")
        Picasso.get().load(p).placeholder(R.drawable.ic_profile).into(img)
        head.setText(q)
        setUpToolbar()
    }
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}