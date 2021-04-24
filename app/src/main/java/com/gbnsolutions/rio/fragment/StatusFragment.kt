package com.gbnsolutions.rio.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.StatusRecyclerAdapter
import com.gbnsolutions.rio.model.status

class StatusFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: StatusRecyclerAdapter
    val List = arrayListOf<status>(
        status("Iron Man","12:00 AM","Online",R.drawable.tony),
        status("Ant Man","12:30 AM","Online",R.drawable.bunny),
        status("Spyder Man","01:00 AM","Offline",R.drawable.murthy),
        status("Captain America","01:30 AM","Online",R.drawable.rakesh),
        status("Captain Marvel","02:00 AM","Online",R.drawable.logo),
        status("Thor","02:30 AM","Offline",R.drawable.sudhir),
        status("Hulk","03:30 AM","Offline",R.drawable.hulk),
        status("Black Panther","04:00 AM","Online",R.drawable.blackpanther)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_status, container, false)
        recyclerHome = view.findViewById(R.id.status)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = StatusRecyclerAdapter(activity as Context, List)
        recyclerHome.adapter = recyclerAdapter
        recyclerHome.layoutManager = layoutManager
        recyclerHome.addItemDecoration(
            DividerItemDecoration(
                recyclerHome.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )
        return view
    }

}