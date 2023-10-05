package com.example.rfid_integrated_system_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserRepository
import emailValidator
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val userRepository = UserRepository()

    val banLogin : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    val _sucessMsg: MutableLiveData<String> = MutableLiveData()
    val sucessMsg: LiveData<String> = _sucessMsg
    fun validateLoginData(email: String, password: String){
        if ((email.isEmpty()) || (password.isEmpty())){
            _errorMsg.value = "Debe escribir los datos de usuario"
            banLogin.value = false
        }
        else if (password.length < 6){
            _errorMsg.value = "La contraseña debe tener más de seis digitos"
            banLogin.value = false

        }
        else if (!emailValidator(email)){
            _errorMsg.value = "El email está escrito en un formato incorrecto"
        }
        else{
            /*_sucessMsg.value = "Bienvenido: ${email}"
            banLogin.value = true*/
            viewModelScope.launch {
                val result = userRepository.loginUser(email,password)
                result.let { resourceRemote ->
                    when(resourceRemote){
                        is ResourceRemote.Success -> {
                            _errorMsg.postValue("Bienvenido: ${email}")
                            banLogin.postValue(true)
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