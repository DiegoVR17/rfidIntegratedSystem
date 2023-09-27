package com.example.rfid_integrated_system_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel(){
    val banRegister : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    val _sucessMsg: MutableLiveData<String> = MutableLiveData()
    val sucessMsg: LiveData<String> = _sucessMsg
    fun validateRegisterData(email: String, password: String, repPassword: String){
        if ((email.isEmpty()) || (password.isEmpty()) || (repPassword.isEmpty())){
            _errorMsg.value = "Debe escribir los datos de registro"
            banRegister.value = false
        }
        else if (password.length <= 6){
            _errorMsg.value = "La contraseña debe tener más de seis digitos"
            banRegister.value = false

        }

        else if(password != repPassword){
            _errorMsg.value = "Las contraseñas no coinciden"
            banRegister.value = false
        }
        else{
            _sucessMsg.value = "Registro exitoso"
            banRegister.value = true
        }

    }
}