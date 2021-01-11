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
import kotlin.math.log

class MessageActivity : AppCompatActivity() {


    lateinit var  Chat_list : ArrayList<chat>
    lateinit var messageAdapter: MessageAdapter

    lateinit var recyclerView : RecyclerView

   var fbUser: FirebaseUser ?=null
    lateinit var dbReference: DatabaseReference
    lateinit var userid: String
    lateinit var seen : ValueEventListener
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
               startActivity(Intent(this@MessageActivity ,MainActivity::class.java ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
            }


        })

        recyclerView = message_recycler_view
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(applicationContext)
        llm.stackFromEnd=true
        recyclerView.layoutManager = llm







        intent = getIntent()

        if (intent.getStringExtra("userid") != null){
         userid = intent.getStringExtra("userid")!!
        Log.d("extra", userid)
        fbUser = FirebaseAuth.getInstance().currentUser!!
        Log.d("extra", " the fb users  : ${fbUser}")
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userid)}


        send_btn.setOnClickListener {
            var msg :String = text_message.text.toString()

            if (!msg.equals("")){

                sendMessage(fbUser!!.uid , userid, msg)
            }else{
                Toast.makeText(this,"IT IS EMPTY ",Toast.LENGTH_SHORT).show()

            }
            text_message.text.clear()


        }




        if (intent.getStringExtra("userid") != null) {

            dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userid)

            dbReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user: User? = snapshot.getValue(User::class.java)

                    message_username.text = user?.getUserName().toString()



                    if (user?.getImageURl().equals("default")) {
                        message_profile_image.setImageResource(R.mipmap.ic_launcher)

                    } else {

                        Glide.with(applicationContext).load(user?.getImageURl())
                            .into(message_profile_image)


                    }
if (user?.getImageURl() != null) {
    readMessages(
        fbUser!!.uid, userid, user.getImageURl()!!
    )

}
                }

                override fun onCancelled(error: DatabaseError) {


                }

            })

        }

    }

    private fun sendMessage(sender: String, receiver: String, message: String) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val hashmap: HashMap<String, Any> = HashMap<String, Any>()
        hashmap.put("sender", sender)
        hashmap.put("receiver", receiver)
        hashmap.put("message", message)
        ref.child("Chats").push().setValue(hashmap)


        var chatRef :DatabaseReference =FirebaseDatabase.getInstance().getReference("ChatList")
            .child(fbUser!!.uid).child(userid)

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {


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

        dbReference.addValueEventListener(object : ValueEventListener{
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

        })




    }
    private fun status(status :String){
        if (fbUser != null) {
            var ref = FirebaseDatabase.getInstance().getReference("Users").child(fbUser!!.uid!!)
            var hashMap: HashMap<String, Any> = HashMap()
            hashMap.put("status", status)
            ref.updateChildren(hashMap)

        }
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }



}