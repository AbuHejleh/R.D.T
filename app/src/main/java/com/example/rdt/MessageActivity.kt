package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rdt.Needed.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message.toolbar
import java.util.*
import kotlin.collections.HashMap

class MessageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("extra", " in it")


        val fbUser: FirebaseUser
        val dbReference: DatabaseReference
        val intent: Intent
        Log.d("extra", " done with it")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                finish()
            }


        })
        intent = getIntent()
        var userid: String = intent.getStringExtra("userid")!!
        Log.d("extra", userid)
        fbUser = FirebaseAuth.getInstance().currentUser!!
        Log.d("extra", " the fb users  : ${fbUser}")
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userid)


        send_btn.setOnClickListener {
            var msg :String = text_message.text.toString()
            Log.d("messages", " the text is : $msg")
            if (!msg.equals("")){
                Log.d("messages", "the sender is :${fbUser.uid} and the reciver is $userid  the text is : $msg")
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


            }

            override fun onCancelled(error: DatabaseError) {

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


    }


}