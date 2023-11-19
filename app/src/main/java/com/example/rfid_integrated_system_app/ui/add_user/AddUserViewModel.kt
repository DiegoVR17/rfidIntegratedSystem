package com.example.rfid_integrated_system_app.ui.add_user

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserAddRepository
import com.example.rfid_integrated_system_app.data.model.User
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddUserViewModel : ViewModel() {

    val userAddRepository = UserAddRepository()
    val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg
    val _sucessMsg: MutableLiveData<String> = MutableLiveData()
    val sucessMsg: LiveData<String> = _sucessMsg

    val banAddUser : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val id : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun loadID(){
        viewModelScope.launch {
            var result = userAddRepository.loadReadID()
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success -> {
                        id.postValue(result.data?.documents?.get(0)?.id)
                    }
                    is ResourceRemote.Error -> {
                        var msg = result.message
                        when(msg){
                           "error" -> msg = "Error"
                        }
                        _errorMsg.postValue(msg)
                    }
                    else ->{

                    }
                }
            }
        }
    }
    fun validateAddUserData(
        firstName: String,
        lastName: String,
        id: String,
        positionRole: String,
        drawable: Drawable
    ) {
        if (firstName.isEmpty() && lastName.isEmpty()){
            _errorMsg.value = "Debe escribir los datos de registro"
        }else{
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)


            val user = User(firstName = firstName, lastName = lastName, id = id, positionRole = positionRole, photo = encodedImage)


            //val user = User(firstName = firstName, lastName = lastName, id = id, positionRole = positionRole)
            viewModelScope.launch {
                var result =  userAddRepository.createUser(user,id)
                result.let { resourceRemote ->
                    when(resourceRemote){
                        is ResourceRemote.Success -> {
                            _sucessMsg.postValue("Usuario creado exitosamente")
                            banAddUser.postValue(true)
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



}