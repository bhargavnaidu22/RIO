package com.gbnsolutions.rio.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.activity.Chat
import com.gbnsolutions.rio.model.Users
import com.squareup.picasso.Picasso
import java.util.ArrayList

class HomeRecyclerAdapter(val context: Context, val mList: ArrayList<Users>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uname: TextView = view.findViewById(R.id.names)
        val img: ImageView = view.findViewById(R.id.image)
        val stack: RelativeLayout = view.findViewById(R.id.hom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recycler_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val user:Users = mList[position]
        holder.uname.text = user.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(holder.img)
        holder.stack.setOnClickListener {
            val i = Intent(context,Chat::class.java)
            i.putExtra("name",holder.uname.text)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}