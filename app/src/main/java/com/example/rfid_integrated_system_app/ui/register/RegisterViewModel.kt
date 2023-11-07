package com.example.rfid_integrated_system_app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserRepository
import com.example.rfid_integrated_system_app.data.model.UserRegistered
import emailValidator
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel(){

    private val userRepository = UserRepository()
    val banRegister : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    val _sucessMsg: MutableLiveData<String> = MutableLiveData()
    val sucessMsg: LiveData<String> = _sucessMsg
    fun validateRegisterData(
        email: String,
        password: String,
        repPassword: String,
        cargoRol: String,
        firstName: String,
        lastName: String
    ){
        if ((email.isEmpty()) || (password.isEmpty()) || (repPassword.isEmpty()) || (firstName.isEmpty()) ||(lastName.isEmpty())){
            _errorMsg.value = "Debe escribir los datos de registro"
            banRegister.postValue(false)
        }
        else if (password.length < 6){
            _errorMsg.value = "La contraseña debe tener más de seis digitos"
            banRegister.postValue(false)

        }
        else if (!emailValidator(email)){
            _errorMsg.value = "El email está escrito en un formato incorrecto"
            banRegister.postValue(false)
        }

        else if(password != repPassword){
            _errorMsg.value = "Las contraseñas no coinciden"
            banRegister.postValue(false)
        }
        else{
            /*_sucessMsg.value = "Registro exitoso"
            banRegister.value = true*/
            viewModelScope.launch {
                var result = userRepository.registerUser(email,password)
                result.let { resourceRemote ->
                    when(resourceRemote){
                        is ResourceRemote.Success -> {
                            val uid = result.data
                            val userRegistered = UserRegistered(uid, email, cargoRol,firstName,lastName)
                            createUser(userRegistered)
                            /*_sucessMsg.postValue("Usuario creado exitosamente")
                            banRegister.postValue(true)*/

                        }
                        is ResourceRemote.Error -> {
                            var msg = result.message
                            when(msg){
                                "The email address is already in use by another account." -> msg = "El email ya está en uso"
                                "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión a internet"
                            }
                            _errorMsg.postValue(msg)
                        }
                        else ->{

                        }
                    }
                }
            }
        }

    }

    private fun createUser(userRegistered: UserRegistered) {
        viewModelScope.launch {
            val result = userRepository.createUser(userRegistered)
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success -> {
                        banRegister.postValue(true)
                        _sucessMsg.postValue("Usuario creado exitosamente")
                    }
                    is ResourceRemote.Error -> {
                        var msg = result.message
                        when(msg){
                            "The email address is already in use by another account." -> msg = "El email ya está en uso"
                            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión a internet"
                        }
                        _errorMsg.postValue(msg)
                    }
                    else ->{
                        //don´t use
                    }
                }
            }
        }
    }
}