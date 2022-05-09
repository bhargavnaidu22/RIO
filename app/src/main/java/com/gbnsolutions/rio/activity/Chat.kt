package com.gbnsolutions.rio.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gbnsolutions.rio.FcmNotificationsSender
import com.gbnsolutions.rio.R
import com.gbnsolutions.rio.adapter.MessagesAdapter
import com.gbnsolutions.rio.adapter.StatusRecyclerAdapter
import com.gbnsolutions.rio.model.Messages
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Chat : AppCompatActivity() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: StatusRecyclerAdapter
    lateinit var toolbar: Toolbar
    lateinit var send: ImageView
    lateinit var gbn: EditText
    lateinit var username: TextView
    lateinit var profile: CircleImageView
    lateinit var status1: TextView
    lateinit var att: ImageView
    var refUsers: DatabaseReference? = null
    var adapter: MessagesAdapter? = null
    var messages: ArrayList<Messages>? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var dialog: ProgressDialog? = null
    var senderUid: String? = null
    var receiverUid: String? = null
    var database: FirebaseDatabase? = null

    var storage: FirebaseStorage? = null
    var firebaseUser: FirebaseUser? = null
    private var t: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        t = intent.getStringExtra("name").toString()
        toolbar = findViewById(R.id.chattoolbar)
        username = findViewById(R.id.user_name)
        profile = findViewById(R.id.profile_image)
        send = findViewById(R.id.send)
        gbn = findViewById(R.id.gbn)
        att = findViewById(R.id.attachment)
        status1 = findViewById(R.id.status)
        recyclerHome = findViewById(R.id.recyclerView)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        FirebaseMessaging.getInstance().subscribeToTopic("all")
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)

        messages = ArrayList<Messages>()

        val profile1 = intent.getStringExtra("image")
        Picasso.get().load(profile1).placeholder(R.drawable.ic_profile).into(profile)
        username.setText(t)
        receiverUid = getIntent().getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().getUid()

        receiverUid?.let { it ->
            database!!.getReference().child("presence").child(it).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val status = snapshot.getValue(String::class.java)
                            if (!status!!.isEmpty()) {
                                if (status == "Offline") {
                                    status1.setText("Offline")
                                    status1.visibility = View.VISIBLE
                                } else {
                                    status1.setText(status)
                                    status1.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        adapter = MessagesAdapter(
            this,
            messages as ArrayList<Messages>,
            senderRoom as String,
            receiverRoom as String
        )
        layoutManager = LinearLayoutManager(this)
        recyclerHome.layoutManager = layoutManager
        recyclerHome.adapter = adapter
        database!!.getReference().child("chats")
            .child(senderRoom!!)
            .child("messages")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messages!!.clear()
                        for (snapshot1 in snapshot.children) {
                            val message: Messages? = snapshot1.getValue(Messages::class.java)
                            if (message != null) {
                                message.setMessageId(snapshot1.key)
                            }
                            if (message != null) {
                                messages!!.add(message)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        send.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    val messageTxt: String = gbn.getText().toString()
                    val date = Date()
                    val message = Messages(messageTxt, senderUid, date.time)
                    if (!messageTxt.isEmpty()) {
                        gbn.setText("")
                        val randomKey: String? = database!!.getReference().push().getKey()
                        val lastMsgObj = HashMap<String, Any>()
                        lastMsgObj["lastMsg"] = message
                        lastMsgObj["lastMsgTime"] = date.time
                        database!!.getReference().child("chats").child(senderRoom!!).updateChildren(
                            lastMsgObj
                        )
                        database!!.getReference().child("chats").child(receiverRoom!!)
                            .updateChildren(lastMsgObj)
                        if (randomKey != null) {
                            database!!.getReference().child("chats")
                                .child(senderRoom!!)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(OnSuccessListener<Void?> {
                                    database!!.getReference().child("chats")
                                        .child(receiverRoom!!)
                                        .child("messages")
                                        .child(randomKey)
                                        .setValue(message)
                                        .addOnSuccessListener(OnSuccessListener<Void?> { })
                                })
                            val notificationsSender = FcmNotificationsSender(
                                "/topics/all",
                                t,
                                messageTxt,
                                applicationContext,
                                this@Chat
                            )
                            notificationsSender.SendNotifications()
                        }
                    }
                }
            })

        att.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 25)
        }
        val handler = Handler()
        gbn.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    senderUid?.let {
                        database!!.getReference().child("presence").child(it).setValue("typing...")
                    }
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(userStoppedTyping, 1000)
                }

                var userStoppedTyping =
                    Runnable {
                        senderUid?.let {
                            database!!.getReference().child("presence").child(it).setValue(
                                "Online"
                            )
                        }
                    }
            })
        setUpToolbar()
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chatmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.view -> {
                val intent = Intent(this@Chat,ViewProfile::class.java)
                intent.putExtra("uid",receiverUid)
                startActivity(intent)
                true
            }
            R.id.clear -> {
                Toast.makeText(this@Chat, "Chat cleared!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.block -> {
                Toast.makeText(this@Chat, "Blocked!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid!!
        database!!.getReference().child("presence").child(currentId).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid!!
        database!!.getReference().child("presence").child(currentId).setValue("Offline")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (data != null) {
                if (data.data != null) {
                    val selectedImage = data.data
                    val calendar = Calendar.getInstance()
                    val reference: StorageReference = storage!!.getReference().child("chats")
                        .child(calendar.timeInMillis.toString() + "")
                    dialog?.show()
                    reference.putFile(selectedImage!!).addOnCompleteListener { task ->
                        dialog?.dismiss()
                        if (task.isSuccessful) {
                            reference.downloadUrl.addOnSuccessListener { uri ->
                                val filePath = uri.toString()
                                val messageTxt: String =
                                    gbn.getText().toString()
                                val date = Date()
                                val message = Messages(messageTxt, senderUid, date.time)
                                message.setMessage("photo")
                                message.setImageUrl(filePath)
                                gbn.setText("")
                                val randomKey: String? =
                                    database!!.getReference().push().getKey()
                                val lastMsgObj = HashMap<String, Any>()
                                lastMsgObj["lastMsg"] = message
                                lastMsgObj["lastMsgTime"] = date.time
                                senderRoom?.let {
                                    database!!.getReference().child("chats").child(it)
                                        .updateChildren(lastMsgObj)
                                }
                                receiverRoom?.let {
                                    database!!.getReference().child("chats").child(it)
                                        .updateChildren(lastMsgObj)
                                }
                                senderRoom?.let {
                                    if (randomKey != null) {
                                        database!!.getReference().child("chats")
                                            .child(it)
                                            .child("messages")
                                            .child(randomKey)
                                            .setValue(message)
                                            .addOnSuccessListener(OnSuccessListener<Void?> {
                                                receiverRoom?.let { it1 ->
                                                    database!!.getReference().child("chats")
                                                        .child(it1)
                                                        .child("messages")
                                                        .child(randomKey)
                                                        .setValue(message).addOnSuccessListener(
                                                            OnSuccessListener<Void?> { })
                                                }
                                            })
                                    }
                                }

                                //Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }
    }

}