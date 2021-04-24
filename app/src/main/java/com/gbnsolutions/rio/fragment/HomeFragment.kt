package com.gbnsolutions.rio.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.HomeRecyclerAdapter
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class HomeFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    private var Lists :List<Users>?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerHome = view.findViewById(R.id.status)
        Lists = ArrayList()
        retriveAllUsers()
        layoutManager = LinearLayoutManager(activity)
        recyclerHome.layoutManager = layoutManager
        return view
    }

    private fun retriveAllUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (Lists as ArrayList<Users>).clear()
                for (data in snapshot.children){
                    val user: Users?= data.getValue(Users::class.java)
                    if (!(user!!.getUID()).equals(firebaseUserID)){
                        (Lists as ArrayList<Users>).add(user)
                    }
                }
                recyclerAdapter = HomeRecyclerAdapter(activity as Context, Lists as ArrayList<Users>)
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