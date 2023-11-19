package com.example.rfid_integrated_system_app.ui.registered_user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfid_integrated_system_app.R
import com.example.rfid_integrated_system_app.data.model.User
import com.example.rfid_integrated_system_app.databinding.FragmentRegisteredUserBinding
import com.example.rfid_integrated_system_app.ui.user_detail.UserDetailFragment
import com.google.android.material.snackbar.Snackbar

class RegisteredUserFragment : Fragment() {

    private lateinit var registeredUserBinding: FragmentRegisteredUserBinding
    private lateinit var registeredUserViewModel: UserRegisteredViewModel
    private lateinit var userRegisteredAdapter: UserRegisteredAdapter
    private var userRegisteredList = mutableListOf<User?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registeredUserViewModel = ViewModelProvider(this)[UserRegisteredViewModel::class.java]
        registeredUserBinding = FragmentRegisteredUserBinding.inflate(inflater, container, false)

        registeredUserViewModel.loadRegisteredUsers()

        registeredUserViewModel.errorMsg.observe(viewLifecycleOwner){msg ->
            error_msg(msg,registeredUserBinding.root)
        }

      registeredUserViewModel.succesDeleteMsg.observe(viewLifecycleOwner){msg ->
            succesDelete_msg(msg,registeredUserBinding.root)
        }

        registeredUserViewModel.userRegisteredDelete.observe(viewLifecycleOwner){
            registeredUserViewModel.loadRegisteredUsers()
        }

        registeredUserViewModel.userRegisteredList.observe(viewLifecycleOwner){userRegisteredList ->

            userRegisteredAdapter.appendItems(userRegisteredList)

        }

        userRegisteredAdapter = UserRegisteredAdapter(userRegisteredList, onItemClicked = { onRegisteredUserItemClicked(it)},
            onItemLongClicked = { onRegisteredUserLongItemClicked(it)})

        registeredUserBinding.RecyclerViewUserRegistered.apply {
            layoutManager = LinearLayoutManager(this@RegisteredUserFragment.requireContext())
            adapter = userRegisteredAdapter
            setHasFixedSize(false)
        }
        registeredUserBinding.fabAddUser.setOnClickListener {
            findNavController().navigate(RegisteredUserFragmentDirections.actionNavigationRegisteredUserToNavigationAddUser())

        }

        return  registeredUserBinding.root

    }

    private fun onRegisteredUserLongItemClicked(userRegistered: User?) {
        registeredUserViewModel.delete(userRegistered)
    }

    private fun onRegisteredUserItemClicked(userRegistered: User?) {
        findNavController().navigate(RegisteredUserFragmentDirections.actionNavigationRegisteredUserToUserDetailFragment(firstName = userRegistered?.firstName.toString(),
            lastName = userRegistered?.lastName.toString(), position = userRegistered?.positionRole.toString(),
            id = userRegistered?.id.toString(), photo = userRegistered?.photo.toString()))
    }

    private fun error_msg(errorMsg: String?, view: ConstraintLayout) {
        Snackbar.make(view, errorMsg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }

    private fun succesDelete_msg(succesDelete_msg: String?, view: ConstraintLayout) {
        Snackbar.make(view, succesDelete_msg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }

}