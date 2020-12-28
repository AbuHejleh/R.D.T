package com.example.rdt.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rdt.Needed.User
import com.example.rdt.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    lateinit var imageProfile :CircleImageView
    lateinit var username :TextView
    lateinit var reference: DatabaseReference
    lateinit var fbUser :FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View = inflater.inflate(R.layout.fragment_profile, container, false)
        imageProfile = view.findViewById(R.id.the_profile_image)
        username = view.findViewById(R.id.profile_username)

        fbUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.uid)
        Log.d("Profile_test","before the listener")
        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user :User = snapshot.getValue(User::class.java)!!
                Log.d("Profile_test","in the snapshot")
                username.text = user.getUserName()
                if (user.getImageURl().equals("default")){
                    imageProfile.setImageResource(R.mipmap.ic_launcher)
                }else{
                    Glide.with(context!!).load(user.getImageURl()).into(imageProfile)
                    

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Profile_test","in the error")
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        }
        reference.addValueEventListener(listener)





        return view
    }

}