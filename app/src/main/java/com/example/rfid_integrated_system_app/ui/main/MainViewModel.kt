package com.example.rfid_integrated_system_app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rfid_integrated_system_app.data.model.User
import com.example.rfid_integrated_system_app.UserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel()  {

    private var listUser = mutableListOf<User>()
    private var adapter: UserAdapter? = null
    private var selectedId: String? = null
    private var cont: Int = 1;


    val firstName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val lastName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val id : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    fun clear() {
        firstName.value = ""
        lastName.value = ""
        id.value = ""
    }

    fun updateData(
        firstName: String,
        lastName: String,
        cargoRol: String,
        databaseReference: DatabaseReference) {

        if (firstName.isNotEmpty() && lastName.isNotEmpty()){
            val user = User(firstName = firstName, lastName = lastName, positionRole = cargoRol)

            databaseReference.child(selectedId.toString()).setValue(user)
        }
        else{
            _errorMsg.value = "Campos incompletos"
        }
    }

    fun saveData(
        firstName: String,
        lastName: String,
        ide_user: String,
        cargoRol: String,
        databaseReference: DatabaseReference
    ) {

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && ide_user.isNotEmpty()){
            val user = User(
                firstName = firstName,
                lastName = lastName,
                positionRole = cargoRol
            )

            databaseReference.child(ide_user).setValue(user)
            databaseReference.child("lenDataBase").setValue(cont)
            cont += 1
        }
        else{
            _errorMsg.value = "Campos incompletos"
        }

    }

    fun getData(databaseReference: DatabaseReference, databaseReferenceID: DatabaseReference) {

        databaseReferenceID.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    id.value = ds.child("id_user").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "onCancelled: ${error.toException()}")
            }

        })

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.e("ok", "onDataChange $snapshot")
                listUser.clear()
                for (ds in snapshot.children){
                    var id = ds.key
                    var firstName = ds.child("firstName").value.toString()
                    var lastName = ds.child("lastName").value.toString()
                    var cargoRol = ds.child("cargoRol").value.toString()

                    val user = User(id = id, firstName = firstName, lastName = lastName, positionRole = cargoRol)
                    listUser.add(user)
                }


                Log.e("ok", "size: ${listUser.size}")
                adapter?.setItems(listUser)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "onCancelled: ${error.toException()}")
            }

        })
    }
}