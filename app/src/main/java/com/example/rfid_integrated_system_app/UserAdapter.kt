package com.example.rfid_integrated_system_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rfid_integrated_system_app.data.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = mutableListOf<User>()
    private var onClicView: ((User) -> Unit)? = null
    private var onClicViewDelete: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.user_item_holder,parent,false)
        return UserViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.setItem(user)
        holder.setOnClicView {
            onClicView?.invoke(it)
        }
        holder.setOnClicViewDelete {
            onClicViewDelete?.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setItems(list: MutableList<User>){
        this.userList = list
        notifyDataSetChanged()
    }

    fun setOnClicView(callback: ((User) -> Unit)){
        this.onClicView = callback
    }

    fun setOnClicViewDelete(callback: ((User) -> Unit)){
        this.onClicViewDelete = callback
    }



    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var tvFirstName: TextView? = null
        private var tvLastName: TextView? = null
        private var tvCargoRol: TextView? = null
        private var actionView: ImageView? = null
        private var actionViewDelete: ImageView? = null

        private var onClicView: ((User) -> Unit)? = null
        private var onClicViewDelete: ((User) -> Unit)? = null


        fun setItem(data: User){
            tvFirstName = itemView.findViewById(R.id.tv_firstName)
            tvLastName = itemView.findViewById(R.id.tv_lastName)
            tvCargoRol = itemView.findViewById(R.id.tv_cargoRol)
            actionView = itemView.findViewById(R.id.ic_view)
            actionViewDelete = itemView.findViewById(R.id.ic_delete)

            tvFirstName?.text = data.firstName
            tvLastName?.text = data.lastName
            tvCargoRol?.text = data.positionRole

            actionView?.setOnClickListener {
                onClicView?.invoke(data)
            }

            actionViewDelete?.setOnClickListener {
                onClicViewDelete?.invoke(data)
            }
        }

        fun setOnClicView(callback: ((User) -> Unit)){
            this.onClicView = callback
        }

        fun setOnClicViewDelete(callback: ((User) -> Unit)){
            this.onClicViewDelete = callback
        }
    }
}