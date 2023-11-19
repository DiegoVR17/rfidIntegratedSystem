package com.example.rfid_integrated_system_app.ui.active_user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfid_integrated_system_app.data.model.UserActive
import com.example.rfid_integrated_system_app.databinding.FragmentActiveUserBinding
import com.example.rfid_integrated_system_app.ui.navigation.NavigationActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class ActiveUserFragment : Fragment() {


    private lateinit var activeUserBinding: FragmentActiveUserBinding
    private lateinit var activeUserViewModel: ActiveUserViewModel
    private lateinit var userActiveAdapter: ActiveUserAdapter
    private var auth = FirebaseAuth.getInstance()
    private var userActiveList = mutableListOf<UserActive?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activeUserViewModel = ViewModelProvider(this)[ActiveUserViewModel::class.java]
        activeUserBinding = FragmentActiveUserBinding.inflate(inflater, container, false)

        activeUserViewModel.loadActiveUsers()

        activeUserBinding.fabSignOut.setOnClickListener {
            if (auth.currentUser != null){
                auth.signOut()
                findNavController().navigate(ActiveUserFragmentDirections.actionNavigationActiveUserToLoginActivity())

            }
        }

        activeUserViewModel.errorMsg.observe(viewLifecycleOwner){msg ->
            show_msg(msg,activeUserBinding.root)
        }


        activeUserViewModel.userActiveList.observe(viewLifecycleOwner){userActiveList ->

            userActiveAdapter.appendItems(userActiveList)

        }

        userActiveAdapter = ActiveUserAdapter(userActiveList, onItemClicked = { onActiveUserItemClicked(it)},
            onItemLongClicked = { onActiveUserLongItemClicked(it)})

        activeUserBinding.RecyclerViewUserActive.apply {
            layoutManager = LinearLayoutManager(this@ActiveUserFragment.requireContext())
            adapter = userActiveAdapter
            setHasFixedSize(false)
        }
        return  activeUserBinding.root

    }


    private fun onActiveUserLongItemClicked(userRegistered: UserActive?) {
        //activeUserViewModel.delete(userRegistered)
    }

    private fun onActiveUserItemClicked(userRegistered: com.example.rfid_integrated_system_app.data.model.UserActive ?) {

    }

    private fun show_msg(show_Msg: String?, view: ConstraintLayout) {
        Snackbar.make(view, show_Msg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }
}