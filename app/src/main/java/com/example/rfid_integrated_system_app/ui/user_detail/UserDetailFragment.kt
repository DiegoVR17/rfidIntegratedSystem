package com.example.rfid_integrated_system_app.ui.user_detail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.rfid_integrated_system_app.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment() {

    private lateinit var userDetailBinding: FragmentUserDetailBinding
    private val args : UserDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userDetailBinding = FragmentUserDetailBinding.inflate(inflater,container,false)
        userDetailBinding.apply{
            textViewFirstNameDetail.text = args.firstName + args.lastName
            textViewPositionDetail.text= args.position
            textViewIDDetail.text = args.id
            val decodedString = Base64.decode(args.photo, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            ImageViewPhotoUserDetail.setImageBitmap(bitmap)
        }
        return userDetailBinding.root
    }

}