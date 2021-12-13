package com.gbnsolutions.rio.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.model.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class StatusRecyclerAdapter(val context: Context, val mList: ArrayList<Users>) :
    RecyclerView.Adapter<StatusRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uname: TextView = view.findViewById(R.id.username)
        val img: CircleImageView = view.findViewById(R.id.profile)
//        val time: TextView = view.findViewById(R.id.statustime)
//        val state: TextView = view.findViewById(R.id.statusstate)
        val stack: RelativeLayout = view.findViewById(R.id.status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.status_recycler_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val status:Users = mList[position]
        holder.uname.text = status.getUserName()
        Picasso.get().load(status.getStatus()).placeholder(R.drawable.ic_profile).into(holder.img)
        holder.stack.setOnClickListener {
            val i = Intent(context,com.gbnsolutions.rio.activity.ImagesView::class.java)
            i.putExtra("url",status.getStatus())
            i.putExtra("usname",status.getUserName())
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}