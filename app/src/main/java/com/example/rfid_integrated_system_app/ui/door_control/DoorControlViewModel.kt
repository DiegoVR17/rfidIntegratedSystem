package com.example.rfid_integrated_system_app.ui.door_control

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.DoorControlRepository
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class DoorControlViewModel: ViewModel() {

    val db = Firebase.firestore
    val doorControlRepository = DoorControlRepository()
    val _open_close_Msg: MutableLiveData<String?> = MutableLiveData()
    val open_close_Msg: LiveData<String?> = _open_close_Msg

    fun open_close_fun(checked: Boolean) {
        val estadoSwitch = hashMapOf(
            "state_door" to checked
        )

        viewModelScope.launch {
            var result =  doorControlRepository.open_close_door(estadoSwitch)
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success -> {
                        if (checked){
                            _open_close_Msg.postValue("Puerta abierta")
                        }else{
                            _open_close_Msg.postValue("Puerta cerrada")
                        }
                    }
                    is ResourceRemote.Error -> {
                        var msg = result.message
                        when(msg){
                            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión a internet"
                        }
                        _open_close_Msg.postValue(msg)
                    }
                    else ->{

                    }
                }
            }
        }
    }


}
