package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rdt.Adapter.MessageAdapter
import com.example.rdt.Needed.User
import com.example.rdt.Needed.chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message.toolbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessageActivity : AppCompatActivity() {


    lateinit var  Chat_list : ArrayList<chat>
    lateinit var messageAdapter: MessageAdapter

    lateinit var recyclerView : RecyclerView

    lateinit var fbUser: FirebaseUser
    lateinit var dbReference: DatabaseReference
    lateinit var userid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("extra", " in it")


        Log.d("extra", " done with it")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                finish()
            }


        })

        recyclerView = message_recycler_view
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(applicationContext)
        llm.stackFromEnd=true
        recyclerView.layoutManager = llm







        intent = getIntent()
         userid = intent.getStringExtra("userid")!!
        Log.d("extra", userid)
        fbUser = FirebaseAuth.getInstance().currentUser!!
        Log.d("extra", " the fb users  : ${fbUser}")
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userid)


        send_btn.setOnClickListener {
            var msg :String = text_message.text.toString()
            Log.d("finding", " the text is : $msg")
            if (!msg.equals("")){
                Log.d("finding", "the sender is :${fbUser.uid} and the reciver is $userid  the text is : $msg")
                sendMessage(fbUser.uid , userid, msg)
            }else{
                Toast.makeText(this,"IT IS EMPTY ",Toast.LENGTH_SHORT).show()

            }
            text_message.text.clear()


        }









        val event = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User? = snapshot.getValue(User::class.java)
                Log.d("extra", " the fb reference  : ${user!!.getUserName()}")
                message_username.text = user?.getUserName().toString()



                if (user.getImageURl().equals("default")) {
                    message_profile_image.setImageResource(R.mipmap.ic_launcher)

                } else {

                    Glide.with(this@MessageActivity).load(user.getImageURl())
                        .into(message_profile_image)


                }

                Log.d("sending", "before the read message method")
                readMessages(fbUser.uid,userid, user.getImageURl().toString())



            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity,"$error", Toast.LENGTH_SHORT).show()

            }
        }
        dbReference.addValueEventListener(event)


    }

    private fun sendMessage(sender: String, receiver: String, message: String) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val hashmap: HashMap<String, Any> = HashMap<String, Any>()
        hashmap.put("sender", sender)
        hashmap.put("receiver", receiver)
        hashmap.put("message", message)
        ref.child("Chats").push().setValue(hashmap)
        var chatRef :DatabaseReference =FirebaseDatabase.getInstance().getReference("ChatList")
            .child(fbUser.uid).child(userid)
        Log.d("sending","before the listener ")
        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()){
                Log.d("sending","done with the exist ")
                chatRef.child("id").setValue(userid)

            }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Toast.makeText(this@MessageActivity,"$error", Toast.LENGTH_SHORT).show()
            }

        }
        chatRef.addListenerForSingleValueEvent(listener)



    }
    private fun readMessages(myid:String , userid :String , imageurl :String) {
        Chat_list = ArrayList()
        Log.d("finding", "in the read messages")
        dbReference = FirebaseDatabase.getInstance().getReference("Chats")
        var vel = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Chat_list.clear()
                Log.d("finding", "geting to the for loop")
                for (snap: DataSnapshot in snapshot.children) {

                    var chat :chat = snap.getValue(chat::class.java)!!
                if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                    chat.getReceiver().equals(userid) && chat.getSender().equals(myid) ){
                    Log.d("finding", "in the if inside the for loop")
                    Chat_list.add(chat)
                }
                messageAdapter = MessageAdapter(this@MessageActivity ,Chat_list , imageurl)
                    recyclerView.adapter = messageAdapter
                }



            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MessageActivity,"$error", Toast.LENGTH_SHORT).show()

            }

        }
        dbReference.addValueEventListener(vel)




    }


}