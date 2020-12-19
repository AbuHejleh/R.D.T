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
import com.example.rdt.Fragments.UsersFragment
import com.example.rdt.Needed.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tool: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tool)
        supportActionBar?.setTitle("")


        var circleImage: CircleImageView


        val firebaseuser = FirebaseAuth.getInstance().currentUser!!
        val refrance = FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.uid)


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
//                if (user != null) {


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

                    Glide.with(this@MainActivity).load(user?.getImageURl()).into(profile_image)


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
            Log.d("xx", "done with the supportFragmentManager ")

            viewPagerAdapter.addFragments(ChatsFragment(), "Chats")
            Log.d("xx", "done with the CHATS FRAGMENT ")

            viewPagerAdapter.addFragments(UsersFragment(), "Users")
            Log.d("xx", "done with the USERS FRAGMENT ")


            view_pager.adapter = viewPagerAdapter
            Log.d("xx", "done with the ADAPTER")
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
                startActivity(Intent(this, StartActivity::class.java))
                finish()
                return true
            }
        }

        return false
    }
//






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

}
internal class MyviewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    private val fragments : ArrayList<Fragment>
    private val titles : ArrayList<String>
    init{
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




