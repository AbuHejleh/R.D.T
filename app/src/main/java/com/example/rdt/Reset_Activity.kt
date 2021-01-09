package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_reset_.*
import kotlinx.android.synthetic.main.activity_reset_.toolbar
import kotlinx.android.synthetic.main.bar_layout.*

class Reset_Activity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_)
        val tool: Toolbar = findViewById(toolbar.id)
        setSupportActionBar(tool)
        supportActionBar?.title = " Forget Password "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        reset_btn.setOnClickListener {
            val email: String = send_email.text.toString()
            Log.d("email", "$email")
            if (email.equals("")) {
                Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show()
            } else {
                fbAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, " !! Check Your Email !! ", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this , LoginActivity::class.java))


                    } else {
                        var error  = it.exception?.message.toString()
                        Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }


    }
}