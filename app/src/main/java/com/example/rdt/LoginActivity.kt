package com.example.rdt

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.Email
import kotlinx.android.synthetic.main.activity_register.Password


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val tool: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tool)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()




        forgot_password.setOnClickListener {

            val move = Intent(this, Reset_Activity::class.java)
            startActivity(move)


        }


        btn_Login.setOnClickListener {
            var password = Password.text.toString()
            var email = Email.text.toString()
            Log.d("inputin", password)
            Log.d("inputin", email)

//            var txt_userName = UserName.text.toString()
            inputCheck(password,email)



    }
}

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.getItemId() === android.R.id.home) {

                if ( FirebaseAuth.getInstance().currentUser != null){
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified != true){
                        val user = FirebaseAuth.getInstance().currentUser!!

                        user.delete().addOnCompleteListener {
                            if (it.isSuccessful) {

                                var referance: DatabaseReference = FirebaseDatabase.getInstance().getReference(
                                    "Users"
                                ).child(user.uid)
                                var referance1: DatabaseReference = FirebaseDatabase.getInstance().getReference(
                                    "ChatList"
                                ).child(user.uid)

                                referance1.removeValue()

                                referance.removeValue()


                            } }


                    }
                    val intent = Intent(this@LoginActivity, StartActivity::class.java)
                    startActivity(intent)
                    finishActivity(this.taskId)
                }
                    }


        return super.onOptionsItemSelected(menuItem)
    }


//    override fun onPause() {
//
//        if ( FirebaseAuth.getInstance().currentUser != null){
//            if (FirebaseAuth.getInstance().currentUser?.isEmailVerified != true){
//                val user = FirebaseAuth.getInstance().currentUser!!
//
//                user.delete().addOnCompleteListener {
//                    if (it.isSuccessful) {
//
//                        var referance: DatabaseReference = FirebaseDatabase.getInstance().getReference(
//                            "Users"
//                        ).child(user.uid)
//                        var referance1: DatabaseReference = FirebaseDatabase.getInstance().getReference(
//                            "ChatList"
//                        ).child(user.uid)
//
//                        referance1.removeValue()
//
//                        referance.removeValue()
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "The account has been deleted ",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    } }
//
//
//            }}
//        super.onPause()
//    }

    private fun inputCheck(password :String ,email:String){
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "all fields are required ", Toast.LENGTH_SHORT).show()

        }
        else auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {


            if (it.isSuccessful) {


                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified != true){

                    Toast.makeText(
                        this,
                        " !! Please check Your Email !! ",
                        Toast.LENGTH_SHORT
                    ).show()




                } else{ var intent = Intent(this, Profile_Activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()





                }


            } else {
                Toast.makeText(
                    this,
                    "Authentication Failed !!",
                    Toast.LENGTH_SHORT
                ).show()
            }



        }


    }
    override fun onDestroy() {

        if ( FirebaseAuth.getInstance().currentUser != null){
            if (FirebaseAuth.getInstance().currentUser?.isEmailVerified != true){
                val user = FirebaseAuth.getInstance().currentUser!!

                user.delete().addOnCompleteListener {
                    if (it.isSuccessful) {

                        var referance: DatabaseReference = FirebaseDatabase.getInstance().getReference(
                            "Users"
                        ).child(user.uid)
                        var referance1: DatabaseReference = FirebaseDatabase.getInstance().getReference(
                            "ChatList"
                        ).child(user.uid)

                        referance1.removeValue()

                        referance.removeValue()


                    } }


            }}
        super.onDestroy()
    }
}