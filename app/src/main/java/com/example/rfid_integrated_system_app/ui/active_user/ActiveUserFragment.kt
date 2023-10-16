package com.example.rfid_integrated_system_app.ui.active_user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfid_integrated_system_app.data.model.UserActive
import com.example.rfid_integrated_system_app.databinding.FragmentActiveUserBinding
import com.google.android.material.snackbar.Snackbar


class ActiveUserFragment : Fragment() {


    private lateinit var activeUserBinding: FragmentActiveUserBinding
    private lateinit var activeUserViewModel: ActiveUserViewModel
    private lateinit var userActiveAdapter: ActiveUserAdapter
    private var userActiveList = mutableListOf<UserActive?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activeUserViewModel = ViewModelProvider(this)[ActiveUserViewModel::class.java]
        activeUserBinding = FragmentActiveUserBinding.inflate(inflater, container, false)

        activeUserViewModel.loadActiveUsers()

        activeUserViewModel.errorMsg.observe(viewLifecycleOwner){msg ->
            error_msg(msg,activeUserBinding.root)
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

    private fun error_msg(errorMsg: String?, view: ConstraintLayout) {
        Snackbar.make(view, errorMsg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }
}