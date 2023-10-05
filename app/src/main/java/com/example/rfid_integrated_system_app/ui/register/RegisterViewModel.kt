package com.example.rfid_integrated_system_app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserRepository
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
    fun validateRegisterData(email: String, password: String, repPassword: String){
        if ((email.isEmpty()) || (password.isEmpty()) || (repPassword.isEmpty())){
            _errorMsg.value = "Debe escribir los datos de registro"
            banRegister.value = false
        }
        else if (password.length < 6){
            _errorMsg.value = "La contraseña debe tener más de seis digitos"
            banRegister.value = false

        }
        else if (!emailValidator(email)){
            _errorMsg.value = "El email está escrito en un formato incorrecto"
        }

        else if(password != repPassword){
            _errorMsg.value = "Las contraseñas no coinciden"
            banRegister.value = false
        }
        else{
            /*_sucessMsg.value = "Registro exitoso"
            banRegister.value = true*/
            viewModelScope.launch {
                var result = userRepository.registerUser(email,password)
                result.let { resourceRemote ->
                    when(resourceRemote){
                        is ResourceRemote.Success -> {
                            _errorMsg.postValue("Usuario creado exitosamente")
                            banRegister.postValue(true)
                        }
                        is ResourceRemote.Error -> {
                            var msg = result.message
                            _errorMsg.postValue(msg)
                            when(msg){
                                "msginglesUserAlready" -> "msgespañol"
                                "msginglesNetwork" -> "msgespañol"
                            }
                        }
                        else ->{

                        }
                    }
                }
            }
        }

    }
}