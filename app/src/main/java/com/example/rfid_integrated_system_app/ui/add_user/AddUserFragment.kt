package com.example.rfid_integrated_system_app.ui.add_user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.example.rfid_integrated_system_app.databinding.FragmentAddUserBinding
import com.google.android.material.snackbar.Snackbar

class AddUserFragment : Fragment() {


    private lateinit var addUserviewModel: AddUserViewModel
    private lateinit var addUserBinding: FragmentAddUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addUserBinding = FragmentAddUserBinding.inflate(inflater,container,false)
        addUserviewModel = ViewModelProvider(this)[AddUserViewModel::class.java]


        addUserviewModel.errorMsg.observe(viewLifecycleOwner){msg->
            error_msg(msg,addUserBinding.root)
        }

        addUserviewModel.sucessMsg.observe(viewLifecycleOwner){msg->
            succes_msg(msg,addUserBinding.root)
        }


        with(addUserBinding){
            buttonAddUser.setOnClickListener {
                addUserviewModel.validateAddUserData(TextInputEditTextFirstName.text.toString(),
                    TextInputEditTextLastName.text.toString(),editTextID.text.toString(),
                    spinnerCargoRol.selectedItem.toString())
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


}