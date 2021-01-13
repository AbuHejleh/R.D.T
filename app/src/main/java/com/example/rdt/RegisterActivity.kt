package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_reset_.*
import kotlinx.android.synthetic.main.bar_layout.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var tool: Toolbar = findViewById(toolbar.id)
        setSupportActionBar(tool)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")


        auth = FirebaseAuth.getInstance()
        btn_Register.setOnClickListener{
            var txt_userName = UserName.text.toString()
            var txt_password = Password.text.toString()
            var txt_Email = Email.text.toString()
            Log.d("inputin", txt_userName)
            Log.d("inputin", txt_password)
            Log.d("inputin", txt_Email)

            inputCheck(txt_userName,txt_password,txt_Email)



        }
        var fb = FirebaseAuth.getInstance()


    }
    private fun inputCheck (userName : String ,password: String,email: String){
        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "all fields are required ", Toast.LENGTH_SHORT).show()

        } else if (password.length < 6) {
            Toast.makeText(this, "the password must a least 6 characters ", Toast.LENGTH_SHORT)
                .show()
        } else {

            Register(userName, email, password)


        }

    }

    private fun Register(username: String, Email: String, Password: String) {
        auth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(OnCompleteListener {
                fun oncomplete() {
                    if (it.isSuccessful) {

                        var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener {

                                if (it.isSuccessful()) {


                                    val firebaseuser: FirebaseUser = auth.currentUser!!

                                    val userid: String = firebaseuser.uid.toString()
                                    val referance =
                                        FirebaseDatabase.getInstance().getReference("Users")
                                            .child(userid)

                                    val hashMap = hashMapOf<String, String>()

                                    hashMap.put("id", userid)
                                    hashMap.put("username", username)
                                    hashMap.put("imageURl", "default")
                                    hashMap.put("status", "offline")

                                    referance.setValue(hashMap).addOnCompleteListener(
                                        OnCompleteListener { it ->
                                            val intent = Intent(this, LoginActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            Toast.makeText(
                                                this,
                                                " !! check Your Email !! ",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            startActivity(intent)


                                        })
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Couldn't Send Verification Email",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                        }


                    } else {
                        Toast.makeText(
                            this,
                            "You can't Register with this Email or Password  ",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
                oncomplete()
            })


    }
}