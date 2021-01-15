package com.example.rdt.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rdt.MessageActivity
import com.example.rdt.Needed.User
import com.example.rdt.Needed.chat
import com.example.rdt.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public val MSG_TYPE_LEFT = 0
    public val MSG_TYPE_Right = 1
    private  var mContext: Context
    private  var mChat: List<chat>
    private var imagurl :String
    lateinit var  fbUser : FirebaseUser

    constructor(mContext: Context, mChat: List<chat> , imageurl :String) {
        Log.d("xx" , "in the adapter constructor")
        this.mChat = mChat
        this.mContext = mContext
        this.imagurl = imageurl

    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var show_message: TextView = itemView.findViewById(R.id.show_message)
        var profile_image: ImageView = itemView.findViewById(R.id.profile_image)
        var seen: TextView = itemView.findViewById(R.id.seen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        if (viewType == MSG_TYPE_Right ){
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.right_chat_item, parent, false)
        return MessageAdapter.ViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.left_chat_item, parent, false)
            return MessageAdapter.ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        Log.d("finding " , "in the onBindViewHolder ")
        var chat = mChat[position]
        holder.show_message.text = chat.getMessage()
        Log.d("finding " , "in before the image test ")
        if (imagurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher)
            Log.d("pic " , " default ")
        }else{
            Glide.with(mContext).load(imagurl).into(holder.profile_image)
            Log.d("pic " , " updated ")
        }
        if (position == mChat.size-1){
            if ( chat.getSeen()!=null && chat.getSeen()!! ){
                holder.seen.text= "Seen"
            }else{
                holder.seen.text= "Delivered"


            }
        } else{
            holder.seen.visibility =View.GONE
        }




    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    override fun getItemViewType(position: Int): Int {
        fbUser = FirebaseAuth.getInstance().currentUser!!
        if (mChat[position].getSender().equals(fbUser.uid)){
            Log.d("finding","${mChat[position].getSender().equals(fbUser.uid)}")
            return MSG_TYPE_Right

        }else{
            return MSG_TYPE_LEFT
        }
    }


}