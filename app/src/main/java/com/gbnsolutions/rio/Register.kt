package com.gbnsolutions.rio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    lateinit var edtuserName: EditText
    lateinit var edtemailId: EditText
    lateinit var edtpassword: EditText
    lateinit var edtconfirmPassword: EditText
    lateinit var btnregister: Button
    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirmpassword: String
    lateinit var mAuth: FirebaseAuth
    private var firebaseUserID: String =""
    lateinit var refUsers: DatabaseReference
    lateinit var relogin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        edtuserName = findViewById(R.id.editTextUserNameField)
        edtemailId = findViewById(R.id.editTextEmailIdField)
        edtpassword = findViewById(R.id.editTextPasswordField)
        relogin = findViewById(R.id.login)
        edtconfirmPassword = findViewById(R.id.editTextConfirmPasswordField)
        mAuth = FirebaseAuth.getInstance()
        btnregister = findViewById(R.id.buttonRegister)
        relogin.setOnClickListener{
         val i = Intent(this@Register,MainActivity::class.java)
         startActivity(i)
        }
        btnregister.setOnClickListener(View.OnClickListener {
            name = edtuserName.getText().toString().trim { it <= ' ' }
            email = edtemailId.getText().toString().trim { it <= ' ' }
            password = edtpassword.getText().toString().trim { it <= ' ' }
            confirmpassword = edtconfirmPassword.getText().toString().trim { it <= ' ' }
            if (name.isEmpty()) {
                Toast.makeText(this@Register, "Please enter Name", Toast.LENGTH_SHORT)
                    .show()
            } else if (email.isEmpty()) {
                Toast.makeText(
                    this@Register,
                    "Please enter EMail ID",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.isEmpty() && password.length < 8) {
                Toast.makeText(
                    this@Register,
                    "Please enter a Valid password",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (confirmpassword.isEmpty()) {
                Toast.makeText(
                    this@Register,
                    "Please enter COnfirm pasword",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password != confirmpassword) {
                Toast.makeText(
                    this@Register,
                    "Password and Conf Password should same",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                next()
            }
        })
    }

    private operator fun next() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this@Register
        ) { task ->
            if (task.isSuccessful) {
                firebaseUserID = mAuth.currentUser!!.uid
                refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                val userHashMap = HashMap<String, Any>()
                userHashMap["uid"] = firebaseUserID
                userHashMap["email"] = edtemailId.getText().toString().trim { it <= ' ' }
//                userHashMap["password"] = edtpassword.getText().toString().trim { it <= ' ' }
                userHashMap["username"] = edtuserName.getText().toString().trim { it <= ' ' }
                userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/gbn-avengers.appspot.com/o/Login%2Flogo1.png?alt=media&token=4944df62-6d53-4a43-ad77-99581bcfce81"
                userHashMap["phone"] = ""
                userHashMap["status"] = "https://firebasestorage.googleapis.com/v0/b/gbn-avengers.appspot.com/o/Login%2Fic_profile.png?alt=media&token=7ca935a8-3409-48eb-a425-37aced50b88f"
                refUsers.updateChildren(userHashMap)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(
                                this@Register,
                                "User registration successful !",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@Register, MainActivity::class.java))
                        }
                    }
            } else {
                Toast.makeText(
                    this@Register,
                    "Something Wrong Occurred !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}