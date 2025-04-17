package com.bookxpert.assignment.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bookxpert.assignment.R
import com.bookxpert.assignment.database.User

class UserAdapter(private val userList: MutableList<User>,private val listener: ProductEditInterface) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder class to hold the views for each item

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.text_user_name)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.name // Set the user name
        holder.userName.setOnClickListener {
            listener.productShow(user)
        }
    }


    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return userList.size
    }

    // Method to update the user list
    fun updateUserList(newUserList: List<User>) {
        userList.clear() // Clear the existing list
        userList.addAll(newUserList) // Add all new users
        notifyDataSetChanged() // Notify the adapter of the change
    }

}