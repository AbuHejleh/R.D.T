package com.example.rdt.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rdt.Needed.User
import com.example.rdt.R
import com.example.rdt.adapters.UserAdapter
//import com.example.rdt.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlin.math.log


class UsersFragment : Fragment() {



  private lateinit var recyclerView: RecyclerView
   private lateinit var userAdapter: UserAdapter
  private lateinit var mUser: ArrayList<User>


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =inflater.inflate(R.layout.fragment_users, container, false)
        Log.d("xx", "palce 1")
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.setHasFixedSize(true)
//        val LLM =  LinearLayoutManager(context)
//        recyclerView.layoutManager(LLM)
        recyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        Log.d("xx", "palce 2")
        mUser = ArrayList<User>()
        readUsers()
        Log.d("xx" , "in the UsersFragments")



        return  view
    }

    private fun readUsers() {

        val firebaseUsers = FirebaseAuth.getInstance().currentUser
        val refrance = FirebaseDatabase.getInstance().getReference("Users")



        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mUser.clear()
                for (snap: DataSnapshot in snapshot.children) {
                    var user:User = snap.getValue(User::class.java)!!
                    Log.d("lele", firebaseUsers!!.uid)






                        if (!user.getId().equals(firebaseUsers.uid)) {
                            mUser.add(user)

                    }
                }
                userAdapter = UserAdapter(context!!,mUser)
                recyclerView.adapter=userAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        refrance.addValueEventListener(postListener)


    }


}