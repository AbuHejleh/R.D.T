package com.example.rdt.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rdt.Needed.Chatlist
import com.example.rdt.Needed.User
import com.example.rdt.R
import com.example.rdt.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatsFragment : Fragment() {


private lateinit var userAdapter: UserAdapter
private lateinit var mUser :ArrayList<User>
    private lateinit var fUser :FirebaseUser
    private lateinit var dbRef :DatabaseReference
    private lateinit var usersList : ArrayList<Chatlist>

lateinit var recyclerView : RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View =inflater.inflate(R.layout.fragment_chats, container, false)
        recyclerView= view.findViewById(R.id.chat_recycler_view)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fUser = FirebaseAuth.getInstance().currentUser!!
        usersList = ArrayList()
        dbRef = FirebaseDatabase.getInstance().getReference("ChatList").child(fUser.uid)
        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            usersList.clear()
                var chatlist :Chatlist
                for (snap :DataSnapshot in snapshot.children){
                    chatlist =snap.getValue(Chatlist::class.java)!!
                    usersList.add(chatlist)

            }
                chatList()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Toast.makeText(context,"$error", Toast.LENGTH_SHORT).show()
            }

        }
        dbRef.addValueEventListener(listener)


        return view
    }

    private fun chatList() {
        mUser = ArrayList()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        var user :User
        val listen = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

              mUser.clear()
                for (snap :DataSnapshot in snapshot.children){

               user= snap.getValue(User::class.java)!!
                for (chatList:Chatlist in usersList){
                    if (user.getId().equals(chatList.getId())){
                        mUser.add(user)
                    }

                }
                }
                if (context != null){

            userAdapter = UserAdapter(context!! , mUser ,true)
                recyclerView.adapter = userAdapter
            }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"$error", Toast.LENGTH_SHORT).show()

            }
        }
        dbRef.addValueEventListener(listen)
    }


}