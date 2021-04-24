package com.gbnsolutions.rio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.gbnsolutions.rio.R

class Chat : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var send: Button
    lateinit var gbn: EditText
    var chat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toolbar = findViewById(R.id.chattoolbar)
        send = findViewById(R.id.send)
        gbn = findViewById(R.id.gbn)
        send.setOnClickListener {

        }
        chat = gbn.getText().toString().trim { it <= ' ' }

        setUpToolbar()
    }
    fun setUpToolbar() {
        val t = intent.getStringExtra("name")
        setSupportActionBar(toolbar)
        supportActionBar?.title = t
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chatmenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.view -> {
                Toast.makeText(this@Chat,"View Profile",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.clear->{
                Toast.makeText(this@Chat,"Chat cleared!",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.block->{
                Toast.makeText(this@Chat,"Blocked!",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}