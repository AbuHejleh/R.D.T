package com.example.rdt.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rdt.Needed.User
import com.example.rdt.R
import com.example.rdt.adapters.UserAdapter
//import com.example.rdt.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlin.math.log


class UsersFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var mUser: ArrayList<User>
    lateinit var search_users : EditText


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_users, container, false)
        Log.d("xx", "palce 1")
        recyclerView = view.findViewById(R.id.recycler_view)
        search_users = view.findViewById(R.id.search_for_users)

        recyclerView.setHasFixedSize(true)
//        val LLM =  LinearLayoutManager(context)
//        recyclerView.layoutManager(LLM)
        recyclerView.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        Log.d("xx", "palce 2")
        mUser = ArrayList<User>()
        readUsers()
        Log.d("xx", "in the UsersFragments")

        search_users.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        return view
    }

    private fun search(s: String) {
        var fbuser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var query: Query =
            FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s)
                .endAt(s + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mUser.clear()
                for (data: DataSnapshot in snapshot.children) {
                    var user: User = data.getValue(User::class.java)!!
                    if (!user.getId().equals(fbuser.uid)) {
                        mUser.add(user)
                    }
                }
                if (context != null) {
                userAdapter = UserAdapter(context!!, mUser, false)
                recyclerView.adapter = userAdapter}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun readUsers() {

        val firebaseUsers = FirebaseAuth.getInstance().currentUser
        val refrance = FirebaseDatabase.getInstance().getReference("Users")


        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (search_users.text.toString().equals("")){


                mUser.clear()
                for (snap: DataSnapshot in snapshot.children) {
                    var user: User = snap.getValue(User::class.java)!!
                    Log.d("lele", firebaseUsers!!.uid)






                    if (!user.getId().equals(firebaseUsers.uid)) {
                     
                        mUser.add(user)

                    }
                }
                if (context != null) {
                    userAdapter = UserAdapter(context!!, mUser, false)

                    recyclerView.adapter = userAdapter
                }

            }}

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }
        refrance.addValueEventListener(postListener)


    }


}