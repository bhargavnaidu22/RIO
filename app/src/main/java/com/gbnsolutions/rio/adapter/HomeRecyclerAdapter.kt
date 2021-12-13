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
import com.bumptech.glide.Glide
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.activity.Chat
import com.gbnsolutions.rio.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class HomeRecyclerAdapter(val context: Context, val mList: ArrayList<Users>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uname: TextView = view.findViewById(R.id.username)
        val img: ImageView = view.findViewById(R.id.profile)
        val lastMsg: TextView = view.findViewById(R.id.lastMsg)
        val msgTime: TextView = view.findViewById(R.id.msgTime)
        val stack: RelativeLayout = view.findViewById(R.id.hom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recycler_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val user:Users = mList[position]
        val senderId = FirebaseAuth.getInstance().uid
        val senderRoom = senderId + user.getUID()
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMsg = snapshot.child("lastMsg").child("message").getValue(
                            String::class.java
                        )
                        val time = snapshot.child("lastMsgTime").getValue(
                            Long::class.java
                        )!!
                        val dateFormat = SimpleDateFormat("hh:mm a")
                        holder.msgTime.text = dateFormat.format(Date(time))
                        holder.lastMsg.setText(lastMsg)
                    } else {
                        holder.lastMsg.setText("Tap to chat")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.uname.text = user.getUserName()
        Glide.with(context).load(user.getProfile())
            .placeholder(R.drawable.ic_profile)
            .into(holder.img)
        holder.img.setOnClickListener {
            val i = Intent(context,com.gbnsolutions.rio.activity.ImagesView::class.java)
            i.putExtra("url",user.getProfile())
            i.putExtra("usname",user.getUserName())
            context.startActivity(i)
        }
        holder.stack.setOnClickListener{
            val intent = Intent(context, Chat::class.java)
            intent.putExtra("name", user.getUserName())
            intent.putExtra("image", user.getProfile())
            intent.putExtra("uid", user.getUID())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}