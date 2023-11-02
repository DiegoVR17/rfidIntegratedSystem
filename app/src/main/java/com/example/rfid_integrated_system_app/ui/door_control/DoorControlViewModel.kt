package com.example.rfid_integrated_system_app.ui.door_control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DoorControlViewModel: ViewModel() {
    val _open_close_Msg: MutableLiveData<String?> = MutableLiveData()
    val open_close_Msg: LiveData<String?> = _open_close_Msg

    fun open_close_fun(checked: Boolean) {
        if (checked){
            _open_close_Msg.postValue("Puerta abierta")
        }else{
            _open_close_Msg.postValue("Puerta cerrada")
        }
    }


}
