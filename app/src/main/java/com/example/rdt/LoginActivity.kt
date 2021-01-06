package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.Email
import kotlinx.android.synthetic.main.activity_register.Password
import kotlinx.android.synthetic.main.activity_register.toolbar


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)





        val tool  : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tool)
        supportActionBar?.title = "LOGIN"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()


        forgot_password.setOnClickListener {
            Toast.makeText(this, "U clicked it", Toast.LENGTH_SHORT).show()
            val move = Intent(this, Reset_Activity::class.java)
            startActivity(move)


        }

        btn_Login.setOnClickListener {
            Toast.makeText(this , "it is in " , Toast.LENGTH_SHORT).show()
//            var txt_userName = UserName.text.toString()
            var txt_password = Password.text.toString()
            var txt_Email = Email.text.toString()
            if ( TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_Email)) {
                Toast.makeText(this, "all fields are required ", Toast.LENGTH_SHORT).show()

            } else auth.signInWithEmailAndPassword(txt_Email,txt_password).addOnCompleteListener{

                    fun oncomplete() {
                        if (it.isSuccessful) {
                            var intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                             }else{
                            Toast.makeText(
                                this,
                                "Authentication Failed !!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }


            oncomplete()}
        }




    }


}