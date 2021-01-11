package com.example.rdt

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.ims.RegistrationManager
import android.view.View
import androidx.appcompat.app.AlertDialog
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
        finishActivity(this.taskId)}
    }

    override fun onStart() {
        super.onStart()

        var user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    override fun onBackPressed() {
//        super.onBackPressed()
       var alert =  android.app.AlertDialog.Builder(this).setIcon(R.drawable.warrning_icon)
           .setTitle(" Exit ").setMessage(" DO YOU WANT TO EXIT ? ").setPositiveButton("YES" , object : DialogInterface.OnClickListener{
               override fun onClick(p0: DialogInterface?, p1: Int) {
               finish()
               }
           }).setNegativeButton("NO" , null).show()
alert
    }


}