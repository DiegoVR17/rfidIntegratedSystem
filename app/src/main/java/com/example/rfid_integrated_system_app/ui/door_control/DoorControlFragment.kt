package com.example.rfid_integrated_system_app.ui.door_control

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_integrated_system_app.databinding.FragmentDoorControlBinding
import com.google.android.material.snackbar.Snackbar


class DoorControlFragment : Fragment() {

    private lateinit var doorControlBinding: FragmentDoorControlBinding
    private lateinit var doorControlViewModel: DoorControlViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        doorControlViewModel = ViewModelProvider(this)[DoorControlViewModel::class.java]
        doorControlBinding = FragmentDoorControlBinding.inflate(inflater, container, false)

        doorControlViewModel.open_close_Msg.observe(viewLifecycleOwner){msg ->
            open_close_Msg(msg,doorControlBinding.root)
        }

        with(doorControlBinding){
            switchDoor.setOnClickListener {
                doorControlViewModel.open_close_fun(switchDoor.isChecked)
            }
        }


        return  doorControlBinding.root
    }

    private fun open_close_Msg(open_close_Msg: String?, view: ConstraintLayout) {
        Snackbar.make(view, open_close_Msg.toString(), Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }
}