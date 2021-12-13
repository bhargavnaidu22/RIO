package com.gbnsolutions.rio.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.pgreze.reactions.ReactionPopup
import com.github.pgreze.reactions.ReactionsConfigBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gbnsolutions.rio.model.Messages
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.databinding.DeleteDialogBinding
import com.gbnsolutions.rio.databinding.ItemReceiveBinding
import com.gbnsolutions.rio.databinding.ItemSentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(var context: Context,val messages: ArrayList<Messages>,val senderRoom: String,val receiverRoom: String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_SENT) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false)
            return SentViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message: Messages = messages[position]
        return if (FirebaseAuth.getInstance().uid == message.getSenderId()) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Messages = messages[position]
//        val reactions = intArrayOf(
//            R.drawable.ic_fb_like,
//            R.drawable.ic_fb_love,
//            R.drawable.ic_fb_laugh,
//            R.drawable.ic_fb_wow,
//            R.drawable.ic_fb_sad,
//            R.drawable.ic_fb_angry
//        )
//        val config = ReactionsConfigBuilder(context)
//            .withReactions(reactions)
//            .build()
//        val popup = ReactionPopup(context, config) { pos: Int? ->
//            if (holder.javaClass == SentViewHolder::class.java) {
//                val viewHolder: SentViewHolder =
//                    holder as SentViewHolder
//                viewHolder.binding.feeling.setImageResource(reactions[pos!!])
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE)
//            } else {
//                val viewHolder: ReceiverViewHolder =
//                    holder as ReceiverViewHolder
//                viewHolder.binding.feeling.setImageResource(reactions[pos!!])
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE)
//            }
//            message.setFeeling(pos)
//            message.getMessageId()?.let {
//                if (senderRoom != null) {
//                    FirebaseDatabase.getInstance().reference
//                        .child("chats")
//                        .child(senderRoom)
//                        .child("messages")
//                        .child(it).setValue(message)
//                }
//            }
//            message.getMessageId()?.let {
//                if (receiverRoom != null) {
//                    FirebaseDatabase.getInstance().reference
//                        .child("chats")
//                        .child(receiverRoom)
//                        .child("messages")
//                        .child(it).setValue(message)
//                }
//            }
//            true // true is closing popup, false is requesting a new selection
//        }
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder: SentViewHolder =
                holder as SentViewHolder
            if (message.getMessage().equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.time.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                val dateFormat = SimpleDateFormat("hh:mm a")
                viewHolder.binding.time.text = dateFormat.format(Date(message.getTimestamp()))
                Glide.with(context)
                    .load(message.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.getMessage()
            val dateFormat = SimpleDateFormat("hh:mm a")
            viewHolder.binding.time.text = dateFormat.format(Date(message.getTimestamp()))
//            if (message.getFeeling() >= 0) {
//                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()])
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE)
//            } else {
//                viewHolder.binding.feeling.setVisibility(View.GONE)
//            }
//            viewHolder.binding.message.setOnTouchListener(OnTouchListener { v, event ->
//                popup.onTouch(v, event)
//                false
//            })
//            viewHolder.binding.image.setOnTouchListener(OnTouchListener { v, event ->
//                popup.onTouch(v, event)
//                false
//            })
            viewHolder.itemView.setOnLongClickListener{
                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null)
                val binding: DeleteDialogBinding = DeleteDialogBinding.bind(view)
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(view.rootView)
                    .create()
                binding.everyone.setOnClickListener{
                    message.setMessage("This message is removed.")
                    message.setFeeling(-1)
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener{
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener({ dialog.dismiss() })
                dialog.show()
                false
            }
        } else {
            val viewHolder: ReceiverViewHolder =
                holder as ReceiverViewHolder
            if (message.getMessage().equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.time.visibility = View.VISIBLE
                val dateFormat = SimpleDateFormat("hh:mm a")
                viewHolder.binding.time.text = dateFormat.format(Date(message.getTimestamp()))
                Glide.with(context)
                    .load(message.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.setText(message.getMessage())
            val dateFormat = SimpleDateFormat("hh:mm a")
            viewHolder.binding.time.text = dateFormat.format(Date(message.getTimestamp()))
//            if (message.getFeeling() >= 0) {
//                //message.setFeeling(reactions[message.getFeeling()]);
//                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()])
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE)
//            } else {
//                viewHolder.binding.feeling.setVisibility(View.GONE)
//            }
//            viewHolder.binding.message.setOnTouchListener(OnTouchListener { v, event ->
//                popup.onTouch(v, event)
//                false
//            })
//            viewHolder.binding.image.setOnTouchListener(OnTouchListener { v, event ->
//                popup.onTouch(v, event)
//                false
//            })
            viewHolder.itemView.setOnLongClickListener(OnLongClickListener {
                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null)
                val binding: DeleteDialogBinding = DeleteDialogBinding.bind(view)
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.getRoot())
                    .create()
                binding.everyone.setOnClickListener(View.OnClickListener {
                    message.setMessage("This message is removed.")
                    message.setFeeling(-1)
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                })
                binding.delete.setOnClickListener{
                    message.getMessageId()?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener({ dialog.dismiss() })
                dialog.show()
                false
            })
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

   class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSentBinding
        init {
            binding = ItemSentBinding.bind(itemView)
        }
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemReceiveBinding
        init {
            binding = ItemReceiveBinding.bind(itemView)
        }
    }

}