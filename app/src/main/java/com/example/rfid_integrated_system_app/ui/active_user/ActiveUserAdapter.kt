package com.example.rfid_integrated_system_app.ui.active_user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfid_integrated_system_app.R
import com.example.rfid_integrated_system_app.data.model.UserActive
import com.example.rfid_integrated_system_app.databinding.CardViewActiveUserBinding
import com.squareup.picasso.Picasso

class ActiveUserAdapter (

    private val userActiveList: MutableList<UserActive?>,
    private var onItemClicked: (UserActive?) -> Unit,
    private var onItemLongClicked: (UserActive?) -> Unit,
    ) : RecyclerView.Adapter<ActiveUserAdapter.ActiveUserViewHolder> (){
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ActiveUserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_active_user,parent,false)
            return ActiveUserViewHolder(view)
        }

        override fun onBindViewHolder(holder: ActiveUserViewHolder, position: Int) {
            val userActive = userActiveList[position]
            holder.bind(userActive)
            holder.itemView.setOnClickListener {
                onItemClicked(userActive)
            }
            holder.itemView.setOnLongClickListener {
                onItemLongClicked(userActive)
                true
            }
        }

        override fun getItemCount(): Int = userActiveList.size

        fun appendItems(newList: MutableList<UserActive?>){
            userActiveList.clear()
            userActiveList.addAll(newList)
            notifyDataSetChanged()
        }

        class ActiveUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            private val binding = CardViewActiveUserBinding.bind(itemView)

            fun bind(userActive: UserActive?){
                with(binding){
                    textViewIDUser.text = userActive?.id
                    textViewDate.text = userActive?.date
                    if (userActive?.photo == null){
                        ImageViewPhotoUser.setImageResource(R.drawable.user_registered_icon)
                    }else{
                        Picasso.get().load(userActive.photo).into(ImageViewPhotoUser)
                    }

                }
            }
        }
}