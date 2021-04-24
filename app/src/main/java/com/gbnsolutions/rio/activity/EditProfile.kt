package com.gbnsolutions.rio.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditProfile : AppCompatActivity() {
    lateinit var textView: EditText
    lateinit var phone: EditText
    lateinit var profile: ImageView
    lateinit var update: Button
    private var REQUEST_SELECT_IMAGE = 101
    lateinit var uri: Uri
    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        textView = findViewById(R.id.a)
        profile = findViewById(R.id.profile)
        phone = findViewById(R.id.phone)
        update = findViewById(R.id.update)
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user: Users?= snapshot.getValue(Users::class.java)
                    textView.setText(user!!.getUserName().toString())
                    phone.setText(user.getPhone().toString())
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(profile)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        update.setOnClickListener {
            refUsers!!.child("phone").setValue(phone.getText().toString().trim { it <= ' ' })
            refUsers!!.child("username").setValue(textView.getText().toString().trim { it <= ' ' })
            Upload(uri)
            Toast.makeText(
                this@EditProfile,
                "Profile Updated successful!",
                Toast.LENGTH_SHORT
            ).show()
        }
        profile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        42)
                }
            } else {
                // Permission has already been granted
                takePicture()
            }
        }
    }

    private fun takePicture() {
        val i = Intent(Intent.ACTION_PICK).setType("image/*")
        startActivityForResult(i,REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_SELECT_IMAGE&&resultCode== RESULT_OK){
            uri = data?.data!!
            profile.setImageURI(uri)
        }
    }

    private fun Upload(uri: Uri) {
        val firebaseStorage = FirebaseStorage.getInstance().getReference().child("pics").child(
            refUsers.toString()
        )
        firebaseStorage.putFile(uri).addOnSuccessListener {
            firebaseStorage.downloadUrl.addOnSuccessListener {i->
                refUsers!!.child("profile").setValue(i.toString())
            }
        }
    }
}

