package com.example.rdt.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rdt.MessageActivity
import com.example.rdt.Needed.User
import com.example.rdt.R

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private var mContext: Context
    private var mUsers: List<User>
    private var is_Active: Boolean


    constructor(mContext: Context, mUsers: List<User>, active: Boolean) {
        Log.d("xx", "in the adapter constructor")
        this.mUsers = mUsers
        this.mContext = mContext
        is_Active = active

    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var status_on: ImageView = itemView.findViewById(R.id.status_on)
        var status_off: ImageView = itemView.findViewById(R.id.status_off)

        var user_name: TextView = itemView.findViewById(R.id.my_username)
        var profile_image: ImageView = itemView.findViewById(R.id.my_profile_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers.get(position)

        holder.user_name.text = user.getUserName()

        if (user?.getImageURl().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher)


        } else {
            Glide.with(mContext).load(user.getImageURl()).into(holder.profile_image)}
            if (is_Active) {
                if (user.getStatus().equals("online")) {
                    holder.status_on.visibility = View.VISIBLE
                    holder.status_off.visibility = View.GONE
                } else {
                    holder.status_on.visibility = View.GONE
                    holder.status_off.visibility = View.VISIBLE

                }
            }else{ holder.status_on.visibility = View.GONE
                holder.status_off.visibility = View.GONE}



        holder.itemView.setOnClickListener { it ->
            val intent = Intent(mContext, MessageActivity::class.java)
            intent.putExtra("userid", user.getId())
            mContext.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }


}