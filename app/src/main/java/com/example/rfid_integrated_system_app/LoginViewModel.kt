package com.example.rfid_integrated_system_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    val banLogin : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    val _sucessMsg: MutableLiveData<String> = MutableLiveData()
    val sucessMsg: LiveData<String> = _sucessMsg
    fun validateLoginData(email: String, password: String){
        if ((email.isEmpty()) || (password.isEmpty())){
            _errorMsg.value = "Debe escribir los datos de usuario"
            banLogin.value = false
        }
        else if (password.length <= 6){
            _errorMsg.value = "La contraseña debe tener más de seis digitos"
            banLogin.value = false

        }
        else{
            _sucessMsg.value = "Bienvenido: ${email}"
            banLogin.value = true
        }

        }
}