package com.example.rdt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.ims.RegistrationManager
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        login.setOnClickListener {startActivity(Intent(this , LoginActivity::class.java))
            finishActivity(this.taskId)
        }
        Registration.setOnClickListener { startActivity(Intent(this , RegisterActivity::class.java))
        finish()}
    }

    override fun onStart() {
        super.onStart()

        var user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }


}