package com.example.rdt.Fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rdt.Needed.User
import com.example.rdt.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import com.google.android.gms.tasks.Continuation as Continuation

class ProfileFragment : Fragment() {

    lateinit var imageProfile: CircleImageView
    lateinit var username: TextView
    lateinit var reference: DatabaseReference
    lateinit var fbUser: FirebaseUser


    lateinit var storageRef: StorageReference
    private  var imageUri: Uri ?= null
    private  var uploadTask: StorageTask<*> ?= null
    private val IMAGE_REQUEST = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        imageProfile = view.findViewById(R.id.the_profile_image)
        username = view.findViewById(R.id.profile_username)
        storageRef = FirebaseStorage.getInstance().getReference("uploads")
        fbUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.uid)
        Log.d("Profile_test", "before the listener")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(User::class.java) != null){
                var user : User ?= snapshot.getValue(User::class.java)!!
                Log.d("Profile_test", "in the snapshot")
                username.text = user?.getUserName()
                if (user?.getImageURl().equals("default")) {
                    imageProfile.setImageResource(R.mipmap.ic_launcher)
                Log.d("pic", "default")
                } else {
                    if (context != null) {
                        Glide.with(context!!).load(user?.getImageURl()).into(imageProfile)
                        Log.d("pic", "updated")
                    }

                }

            }}

            override fun onCancelled(error: DatabaseError) {
                Log.d("Profile_test", "in the error")
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        }
        reference.addValueEventListener(listener)
        imageProfile.setOnClickListener {
            openImage() }
        return view
    }

    private fun openImage() {
        Log.d("testing", "the openImage ")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)

    }

    private fun getFileExtension(uri: Uri): String {
        Log.d("testing", "the getFileExtension ")
        val contentResolver: ContentResolver = requireContext().contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))!!


    }

    private fun uploadImage() {
        Log.d("testing", "the upload image ")
        val dialog = ProgressDialog(context)
        dialog.setMessage("Uploading")
        dialog.show()

        if (imageUri != null) {
            val fileRef: StorageReference = storageRef.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            )
            uploadTask = fileRef.putFile(imageUri!!)
            Log.d("testing", "the uploadTask  : $uploadTask")

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.d("testing", "the continue with task")
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl

            }?.addOnCompleteListener {
                if (it.isSuccessful) {
                    var downloadUri = it.result
                    var mUri: String = downloadUri.toString()
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.uid)
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap.put("imageURl" , mUri)
                    reference.updateChildren(hashMap)
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }


            }?.addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()

            }
        } else {
            Toast.makeText(context, " No Image Selected ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            Log.d("testing", "${requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null}")
            imageUri = data.data!!
            Log.d("testing", "$imageUri")

            if (uploadTask != null && uploadTask?.isInProgress!!) {
                Toast.makeText(context, "Upload in Progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }

        }
    }
}