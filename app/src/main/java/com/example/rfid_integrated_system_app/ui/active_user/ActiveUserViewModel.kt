package com.example.rfid_integrated_system_app.ui.active_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfid_integrated_system_app.data.ResourceRemote
import com.example.rfid_integrated_system_app.data.UserActiveRepository
import com.example.rfid_integrated_system_app.data.model.UserActive
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class ActiveUserViewModel: ViewModel()  {

    val userActiveRepository = UserActiveRepository()
    private var userActiveListLocal = mutableListOf<UserActive?>()

    val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _userActiveList: MutableLiveData<MutableList<UserActive?>> = MutableLiveData()
    val userActiveList : LiveData<MutableList<UserActive?>> = _userActiveList

    fun loadActiveUsers() {
        userActiveListLocal.clear()
        viewModelScope.launch {
            val result = userActiveRepository.loadActiveUsers()
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success -> {
                        result.data?.documents?.forEach{ document ->
                            val userActive = document.toObject<UserActive>()
                            userActiveListLocal.add(userActive)
                        }
                        _userActiveList.postValue(userActiveListLocal)
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