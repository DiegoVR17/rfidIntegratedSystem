package com.example.rfid_integrated_system_app.ui.add_user

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.example.rfid_integrated_system_app.databinding.FragmentAddUserBinding
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class AddUserFragment : Fragment() {

    private lateinit var addUserviewModel: AddUserViewModel
    private lateinit var addUserBinding: FragmentAddUserBinding
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addUserBinding = FragmentAddUserBinding.inflate(inflater,container,false)
        addUserviewModel = ViewModelProvider(this)[AddUserViewModel::class.java]

        val idObserver = Observer<String>{id ->
            if (id.isNullOrEmpty()){
                addUserBinding.editTextID.setText("0")
                error_msg("Recargue la vista",addUserBinding.root)
            }
            else{addUserBinding.editTextID.setText(id)}
        }

        addUserviewModel.id.observe(viewLifecycleOwner,idObserver)


        addUserviewModel.errorMsg.observe(viewLifecycleOwner){msg->
            error_msg(msg,addUserBinding.root)
        }

        addUserviewModel.sucessMsg.observe(viewLifecycleOwner){msg->
            succes_msg(msg,addUserBinding.root)
        }

        addUserviewModel.loadID()

        with(addUserBinding){
            buttonAddUser.setOnClickListener {
                addUserviewModel.validateAddUserData(TextInputEditTextFirstName.text.toString(),
                    TextInputEditTextLastName.text.toString(),editTextID.text.toString(),
                    spinnerCargoRol.selectedItem.toString(),imageViewUserPhoto.drawable)
            }

            imageViewUserPhoto.setOnClickListener {
                //addUserviewModel.dispatchTakePictureIntent()
                dispatchTakePictureIntent()
            }
        }

        val banAddUserObserver = Observer<Boolean>{banAddUser ->
            if (banAddUser){
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        addUserviewModel.banAddUser.observe(viewLifecycleOwner,banAddUserObserver)
        return addUserBinding.root
    }



    private fun succes_msg(succesMsg: String?, view: LinearLayout) {
        Snackbar.make(view, succesMsg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }

    private fun error_msg(errorMsg: String?, view: LinearLayout) {
        Snackbar.make(view, errorMsg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // Manejar la excepción si la cámara no está disponible en el dispositivo
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            addUserBinding.imageViewUserPhoto.setImageBitmap(imageBitmap)
        }
    }


}