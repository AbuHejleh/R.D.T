package com.example.rdt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.solver.state.Reference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit  var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
      var userName=UserName.text.toString()
        var password=Password.text.toString()
        var Email=Email.text.toString()
       auth = FirebaseAuth.getInstance()

    }
    private fun Register( Username:String , Email:String , Password:String ) {
        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(OnCompleteListener {
             fun oncomplete(){
                 if(it.isSuccessful){
                    var  firebaseuser : FirebaseUser = auth.currentUser!!

                     var userid : String
                             = firebaseuser.uid.toString()
                     var referance = FirebaseDatabase.getInstance().getReference("Users").child(userid)

                     var hashMap= hashMapOf<String , String>()

                     hashMap.put("id",userid)
                     hashMap.put("Username",Username)
                     hashMap.put("imageURL","default")






                 }

            }
        })


    }
}