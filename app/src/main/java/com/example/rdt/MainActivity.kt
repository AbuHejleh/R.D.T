package com.example.rdt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.example.rdt.Fragments.ChatsFragment
import com.example.rdt.Fragments.ProfileFragment
import com.example.rdt.Fragments.UsersFragment
import com.example.rdt.Needed.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val firebaseuser:FirebaseUser = FirebaseAuth.getInstance().currentUser!!
 val refrance = FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.uid)
    var detection = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tool: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tool)
        supportActionBar?.setTitle("")


        var circleImage: CircleImageView





        //    Log.e("rer", "${refrance.key}")


        val value = object : ValueEventListener {


            override fun onDataChange(ss: DataSnapshot) {
//                Log.e("TESTING", "IN THE ON DATA CHANGE")
//                                                                                          val map = ss!!.getValue(User::class.java) as Map<String, String>

//

                val user: User? = ss.getValue(User::class.java)
//                user?.setId(ss.child("id").value.toString())
                user?.setId(refrance.key!!)
//                user?.setImageURl(ss.child("imageURl").key.toString())
//                Log.d("lele", " THE ID IS : ${user?.getId()}")
//                Log.d("lele", " THE URL IS : ${user?.getImageURl()}")


//                                                                                User(firebaseuser.uid,user?.getUserName().toString(),"")

//                Log.e("TESTING", "${user?.getUserName()} " + "this")
//                Log.e(
//                    "TESTING",
//                    "${user?.setImageURl(ss.child("imageURL").value.toString())} " + "this"
//                )
//                Log.e("TESTING", "${user?.getImageURl()} " + "this")
//                if (username.text != null) {


                    username.text = user?.getUserName().toString()

//                } else {
//                    Toast.makeText(this@MainActivity, "WHO ARE U", Toast.LENGTH_SHORT).show()
////                    invoke<Any, Unit>({ salle() }, 10)
//
//
//                }

                if (user?.getImageURl().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher)

                } else {

                    Glide.with(applicationContext).load(user?.getImageURl()).into(profile_image)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("lele", "$error")
                TODO("Not yet implemented")


            }

        }

        refrance.addValueEventListener(value)

        try {
            var viewPagerAdapter = MyviewPagerAdapter(supportFragmentManager)


            viewPagerAdapter.addFragments(ChatsFragment(), "Chats")
            viewPagerAdapter.addFragments(UsersFragment(), "Users")
            viewPagerAdapter.addFragments(ProfileFragment(), "Profile")



            view_pager.adapter = viewPagerAdapter

            tab_layout.setupWithViewPager(view_pager)
            Log.d("xx", "done with the setup")


        } catch (e: Exception) {
            Log.d("xx", "$e is an error at   ${e.printStackTrace()}")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        Log.e("TESTING","IN THE ON CREATE OPTIONSMENU")
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.e("TESTING","IN THE ON OPTION ITEM SELECTED")
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, StartActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                startActivity(Intent(this, StartActivity::class.java))
//                finish()

                return true
            }
            R.id.delete -> {
                val user = FirebaseAuth.getInstance().currentUser!!
                user.delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, StartActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finishActivity(this.taskId)
                      var referance: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.uid)
                        detection = true

                       referance.removeValue()






                    } else {

                        Toast.makeText(this, " !!! Please Login And Try Again !!! ", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, StartActivity::class.java))
                        finishActivity(this.taskId)
                    }}


                }

            }


        return false
    }







    // just some homemade methods
//    fun <any,Unit> invoke(call:()-> any, time : Long){
//        Log.e("TESTING","the invoke has started")
//       val timer = object : CountDownTimer(time,0) {
//
//            override fun onTick(p0: Long) {
//                Log.e("TESTING","$p0 sec")
//
//            }
//
//            override fun onFinish() {
//                Log.e("TESTING","Finished")
////             call.invoke()
//            }
//
//        }.start()


//    }
//    fun sall(){
//        Log.e("TESTING","Signing out from the 1st method")
//        FirebaseAuth.getInstance().signOut()
//        startActivity(Intent(this, StartActivity::class.java))
//        finish()

//    }
//    fun salle(){
//        Log.e("TESTING","Signing out from the 2nd method")
//        FirebaseAuth.getInstance().signOut()
//        startActivity(Intent(this, StartActivity::class.java))
//        finish()


class MyviewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    private val fragments: ArrayList<Fragment>
    private val titles: ArrayList<String>

    init {
        fragments = ArrayList<Fragment>()
        titles = ArrayList<String>()
    }


    override fun getCount(): Int {
        return fragments.size

    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    public fun addFragments(frag: Fragment, Title: String) {
        fragments.add(frag)
        titles.add(Title)
    }

    override fun getPageTitle(i: Int): CharSequence? {
        return titles[i]
    }


}
    private fun status(status :String){
      if (detection != true){
        val ref =FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.uid)
        val hashMap :HashMap<String, Any> =  HashMap()
        hashMap.put("status" , status)
        ref.updateChildren(hashMap)


    }}

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }


}





















