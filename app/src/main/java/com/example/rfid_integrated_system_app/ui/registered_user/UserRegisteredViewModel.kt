package com.example.rfid_integrated_system_app.ui.registered_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserAddRepository
import com.example.rfid_integrated_system_app.data.model.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class UserRegisteredViewModel: ViewModel()  {

    val userAddRepository = UserAddRepository()
    private var userRegisteredListLocal = mutableListOf<User?>()

    val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    val _succesDeleteMsg: MutableLiveData<String?> = MutableLiveData()
    val succesDeleteMsg: LiveData<String?> = _succesDeleteMsg

    private val _userRegisteredList: MutableLiveData<MutableList<User?>> = MutableLiveData()
    val userRegisteredList : LiveData<MutableList<User?>> = _userRegisteredList

    private val _userRegisteredDelete: MutableLiveData<Boolean> = MutableLiveData()
    val userRegisteredDelete : LiveData<Boolean> = _userRegisteredDelete

    fun loadRegisteredUsers() {
        userRegisteredListLocal.clear()
        viewModelScope.launch {
            val result = userAddRepository.loadRegisteredUsers()
            result.let { resourceRemote ->
                when(resourceRemote){

                    is ResourceRemote.Success -> {
                        result.data?.documents?.forEach{ document ->
                            val userRegistered = document.toObject<User>()
                            userRegisteredListLocal.add(userRegistered)
                        }

                        _userRegisteredList.postValue(userRegisteredListLocal)
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)

                    }
                    else ->{
                        //don´t use
                    }
                }
            }
        }
    }

    fun delete(user: User?) {
        viewModelScope.launch {
            val result = userAddRepository.deleteUser(user)
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success -> {
                        _userRegisteredDelete.postValue(true)
                        _succesDeleteMsg.postValue("Usuario eliminado exitosamente")
                    }

                    is ResourceRemote.Error -> {
                        val msg = result.message
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
