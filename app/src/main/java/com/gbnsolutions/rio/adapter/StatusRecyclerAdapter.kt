package com.gbnsolutions.rio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.model.status

class StatusRecyclerAdapter(val context: Context,val mList: ArrayList<status>) :
    RecyclerView.Adapter<StatusRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uname: TextView = view.findViewById(R.id.names)
        val img: ImageView = view.findViewById(R.id.image)
        val time: TextView = view.findViewById(R.id.statustime)
        val state: TextView = view.findViewById(R.id.statusstate)
        val stack: RelativeLayout = view.findViewById(R.id.stack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.status_recycler_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val status = mList[position]
        holder.img.setImageResource(status.img)
        holder.time.text =status.time
        holder.state.text = status.state
        holder.uname.text = status.uname
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}