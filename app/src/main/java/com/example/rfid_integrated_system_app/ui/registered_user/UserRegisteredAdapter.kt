package com.example.rfid_integrated_system_app.ui.registered_user

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfid_integrated_system_app.R
import com.example.rfid_integrated_system_app.data.model.User
import com.example.rfid_integrated_system_app.databinding.CardViewUserRegisteredBinding
import com.squareup.picasso.Picasso

class UserRegisteredAdapter(
    private val userRegisteredList: MutableList<User?>,
    private var onItemClicked: (User?) -> Unit,
    private var onItemLongClicked: (User?) -> Unit,
) : RecyclerView.Adapter<UserRegisteredAdapter.UserRegisteredViewHolder> (){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserRegisteredViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_user_registered,parent,false)
        return UserRegisteredViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserRegisteredAdapter.UserRegisteredViewHolder, position: Int) {
        val userRegistered = userRegisteredList[position]
        holder.bind(userRegistered)
        holder.itemView.setOnClickListener {
            onItemClicked(userRegistered)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(userRegistered)
            true
        }
    }

    override fun getItemCount(): Int = userRegisteredList.size

    fun appendItems(newList: MutableList<User?>){
        userRegisteredList.clear()
        userRegisteredList.addAll(newList)
        notifyDataSetChanged()
    }

    class UserRegisteredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = CardViewUserRegisteredBinding.bind(itemView)

        fun bind(user: User?){
            with(binding){
                textViewFirstName.text = user?.firstName
                textViewLastName.text = user?.lastName
                textViewPosition.text = user?.positionRole
                if (user?.photo == null){
                    ImageViewPhotoUser.setImageResource(R.drawable.user_registered_icon)
                }else{
                    val decodedString = Base64.decode(user.photo, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    ImageViewPhotoUser.setImageBitmap(bitmap)
                    //Picasso.get().load(user.photo).into(ImageViewPhotoUser)
                    //Picasso.get().load("data:image/png;base64,${user.photo}").into(ImageViewPhotoUser)
                }


            }
        }
    }


}