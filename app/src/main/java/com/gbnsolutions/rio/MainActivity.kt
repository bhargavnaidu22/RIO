package com.gbnsolutions.rio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gbnsolutions.rio.activity.Charecter
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    lateinit var Login: Button
    lateinit var user: EditText
    lateinit var password: EditText
    lateinit var register: TextView
    var userName: String? = null
    var userPassword: String? = null
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        setContentView(R.layout.activity_main)
        if (isLoggedIn){
            val i = Intent(this@MainActivity, Charecter::class.java)
            startActivity(i)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        Login = findViewById(R.id.login)
        user = findViewById(R.id.name)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register)
        register.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        })
        Login.setOnClickListener(View.OnClickListener {
            userName = user.getText().toString().trim { it <= ' ' }
            userPassword = password.getText().toString().trim { it <= ' ' }
            if (userName!!.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter Email ID", Toast.LENGTH_SHORT)
                    .show()
            } else if (userPassword!!.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter Password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                next()
            }
        })
    }

    private operator fun next() {
        mAuth!!.signInWithEmailAndPassword(userName!!, userPassword!!).addOnCompleteListener(
            this@MainActivity
        ){ task ->
            if (task.isSuccessful) {
                Toast.makeText(this@MainActivity, "Logged in Successfully !", Toast.LENGTH_SHORT)
                    .show()
                savePreferences()
                val intent = Intent(this@MainActivity, Charecter::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Invalid Credentials !", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    fun savePreferences(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }
}