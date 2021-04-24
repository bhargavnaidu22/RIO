package com.gbnsolutions.rio.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    lateinit var textView: TextView
    lateinit var email: TextView
    lateinit var phone: TextView
    lateinit var profile: ImageView
    var refUsers: DatabaseReference?=null
    var firebaseUser: FirebaseUser?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        textView = view.findViewById(R.id.a)
        email = view.findViewById(R.id.b)
        phone = view.findViewById(R.id.c)
        profile = view.findViewById(R.id.profile)
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
        return view
    }
}