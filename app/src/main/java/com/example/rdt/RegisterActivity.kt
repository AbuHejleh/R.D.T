package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.constraintlayout.solver.state.Reference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.bar_layout.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var tool  : Toolbar = findViewById(toolbar.id)
        setSupportActionBar(tool)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("REGISTER")


        auth = FirebaseAuth.getInstance()
        btn_Register.setOnClickListener {

            val txt_userName = UserName.text.toString()
            val txt_password = Password.text.toString()
            val txt_Email = Email.text.toString()
            if (TextUtils.isEmpty(txt_userName) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(
                    txt_Email
                )
            ) {
                Toast.makeText(this, "all fields are required ", Toast.LENGTH_SHORT).show()

            } else if (txt_password.length < 6) {
                Toast.makeText(this, "the password must a least 6 characters ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Register(txt_userName , txt_Email, txt_password)

            }
        }

    }

    private fun Register(username: String, Email: String, Password: String) {
        auth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(OnCompleteListener {
                fun oncomplete() {
                    if (it.isSuccessful) {
                        val firebaseuser: FirebaseUser = auth.currentUser!!

                        val userid: String = firebaseuser.uid.toString()
                        val referance = FirebaseDatabase.getInstance().getReference("Users").child(userid)

                        val hashMap = hashMapOf<String, String>()

                        hashMap.put("id", userid)
                        hashMap.put("username", username)
                        hashMap.put("imageURl", "default")
                        hashMap.put("status", "offline")

                        referance.setValue(hashMap)
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (it.isSuccessful) {
                                    var intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    Toast.makeText(this , " You Did it !!" , Toast.LENGTH_SHORT ).show()
                                    startActivity(intent)
                                    finishActivity(this.taskId)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "AUTHENTICATION FAILED",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })


                    } else {
                        Toast.makeText(
                            this,
                            "You can't Register with this Email or Password  ",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
             oncomplete()})


    }
}