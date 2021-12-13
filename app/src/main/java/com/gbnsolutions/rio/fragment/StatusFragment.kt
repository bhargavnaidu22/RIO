package com.gbnsolutions.rio.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.StatusRecyclerAdapter
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class StatusFragment : Fragment() {
    lateinit var recyclerHome: ShimmerRecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: StatusRecyclerAdapter
    private var Lists :List<Users>?= null
    lateinit var textView: TextView
    lateinit var add: Button
    lateinit var upload: Button
    lateinit var uri: Uri
    private var REQUEST_SELECT_IMAGE = 101
    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_status, container, false)
        recyclerHome = view.findViewById(R.id.statusrecyclerView)
        Lists = ArrayList()
        add = view.findViewById(R.id.add)
        textView = view.findViewById(R.id.user_name)
        upload = view.findViewById(R.id.upload)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        val t ="Upload Status"
        textView.setText(t)
        retriveStatus()
        add.setOnClickListener {
            if (ContextCompat.checkSelfPermission(context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        42)
                }
            } else {
                // Permission has already been granted
                takePicture()
            }
        }
        upload.setOnClickListener {
            upate(uri)
        }
        layoutManager = LinearLayoutManager(activity)
        recyclerHome.layoutManager = layoutManager
        return view
    }
    private fun takePicture() {
        val i = Intent(Intent.ACTION_PICK).setType("image/*")
        startActivityForResult(i,REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_SELECT_IMAGE&&resultCode== AppCompatActivity.RESULT_OK){
            uri = data?.data!!
        }
    }
    private fun upate(uri: Uri) {
        val firebaseStorage = FirebaseStorage.getInstance().getReference().child("status").child(refUsers.toString())
        firebaseStorage.putFile(uri).addOnSuccessListener {
            firebaseStorage.downloadUrl.addOnSuccessListener {i->
                refUsers!!.child("status").setValue(i.toString())
                refUsers!!.child("time").setValue(Date().time)
            }
        }
    }
    private fun retriveStatus() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (Lists as ArrayList<Users>).clear()
                for (data in snapshot.children){
                    val user: Users?= data.getValue(Users::class.java)
                    val status =  "https://firebasestorage.googleapis.com/v0/b/gbn-avengers.appspot.com/o/Login%2Fic_profile.png?alt=media&token=7ca935a8-3409-48eb-a425-37aced50b88f"
                    if (!(user!!.getUID()).equals(firebaseUserID)){
                        if (!(user.getStatus()).equals(status)){
                            (Lists as ArrayList<Users>).add(user)
                        }
                    }
                }
                recyclerAdapter = StatusRecyclerAdapter(activity as Context, Lists as ArrayList<Users>)
                recyclerHome.adapter = recyclerAdapter
                recyclerHome.addItemDecoration(
                    DividerItemDecoration(
                        recyclerHome.context,
                        (layoutManager as LinearLayoutManager).orientation
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}