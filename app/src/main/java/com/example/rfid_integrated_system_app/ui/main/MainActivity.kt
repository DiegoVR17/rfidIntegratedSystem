package com.example.rfid_integrated_system_app.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rfid_integrated_system_app.R
import com.example.rfid_integrated_system_app.User
import com.example.rfid_integrated_system_app.UserAdapter
import com.example.rfid_integrated_system_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {


    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var listUser = mutableListOf<User>()
    private var adapter: UserAdapter? = null
    private lateinit var firebaseDataBase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceID: DatabaseReference
    private var selectedId: String? = null
    private var cont: Int = 1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initRecyclerView()

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase.getReference("UsersData")
        databaseReferenceID = firebaseDataBase.getReference("UsersDataAux")

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


        val firstNameObserver = Observer<String>{firstName ->
            mainBinding.editTextFirstName.setText(firstName)
        }

        mainViewModel.firstName.observe(this,firstNameObserver)

        val lastNameObserver = Observer<String>{lastName ->
            mainBinding.editTextLastName.setText(lastName)
        }

        mainViewModel.lastName.observe(this,lastNameObserver)

        val idObserver = Observer<String>{id ->
            mainBinding.textViewIDUser.text = id
        }

        mainViewModel.id.observe(this,idObserver)

        val errorMsgObserver = Observer<String>{errorMsg ->
            Snackbar.make(mainBinding.root,errorMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar"){}
                .show()
        }

        mainViewModel.errorMsg.observe(this,errorMsgObserver)


        val cargoRol = resources.getStringArray(R.array.cargos)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cargoRol)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mainBinding.spinnerCargoRol.adapter = adapter

        mainViewModel.getData(databaseReference,databaseReferenceID)


        //getData()


        mainBinding.buttonSave.setOnClickListener {
            mainViewModel.saveData(mainBinding.editTextFirstName.text.toString(),
                mainBinding.editTextLastName.text.toString(),
                mainBinding.textViewIDUser.text.toString(),
                mainBinding.spinnerCargoRol.selectedItem.toString(),
                databaseReference)
        }
        mainBinding.buttonUpdate.setOnClickListener {
            mainViewModel.updateData(mainBinding.editTextFirstName.text.toString(),
                mainBinding.editTextLastName.text.toString(),
                mainBinding.spinnerCargoRol.selectedItem.toString(),databaseReference)
        }
        mainBinding.buttonClear.setOnClickListener {
            mainViewModel.clear()
        }

    }

    fun initRecyclerView() {
        adapter = UserAdapter()
        mainBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapter
        }

        adapter?.setOnClicView {
            //Toast.makeText(this, "Click Action View ${it.id}", Toast.LENGTH_SHORT).show()
            mainBinding.editTextFirstName.setText(it.firstName.toString())
            mainBinding.editTextLastName.setText(it.lastName.toString())
            selectedId = it.id
        }

        adapter?.setOnClicViewDelete {
            selectedId = it.id
            databaseReference.child(selectedId.orEmpty()).removeValue()
        }
    }
/*
    private fun clear() {
        mainBinding.editTextFirstName.text.clear()
        mainBinding.editTextLastName.text.clear()
        //mainBinding.editTextID.text.clear()
        mainBinding.textViewIDUser.text = ""
    }

    private fun updateData() {
        val firstName = mainBinding.editTextFirstName.text.toString()
        val lastName = mainBinding.editTextLastName.text.toString()
        val cargoRol = mainBinding.spinnerCargoRol.selectedItem.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty()){
            val user = User(firstName = firstName, lastName = lastName, cargoRol = cargoRol)

            databaseReference.child(selectedId.toString()).setValue(user)
        }
        else{
            Toast.makeText(this,"Campos incompletos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        adapter = UserAdapter()
        mainBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapter
        }

        adapter?.setOnClicView {
            //Toast.makeText(this, "Click Action View ${it.id}", Toast.LENGTH_SHORT).show()
            mainBinding.editTextFirstName.setText(it.firstName.toString())
            mainBinding.editTextLastName.setText(it.lastName.toString())
            selectedId = it.id
        }

        adapter?.setOnClicViewDelete {
            selectedId = it.id
            databaseReference.child(selectedId.orEmpty()).removeValue()
        }
    }


    private fun saveData() {

        val firstName = mainBinding.editTextFirstName.text.toString()
        val lastName = mainBinding.editTextLastName.text.toString()
        val ide_user = mainBinding.textViewIDUser.text.toString()
        //val ide_user = mainBinding.editTextID.text.toString()
        val cargoRol = mainBinding.spinnerCargoRol.selectedItem.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && ide_user.isNotEmpty()){
            val user = User(
                firstName = firstName,
                lastName = lastName,
                cargoRol = cargoRol
            )

            databaseReference.child(ide_user).setValue(user)
            databaseReference.child("lenDataBase").setValue(cont)
            cont += 1
        }
        else{
            Toast.makeText(this,"Campos incompletos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getData() {

        databaseReferenceID.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    mainBinding.textViewIDUser.text = ds.child("id_user").value.toString()
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

                    val user = User(id = id, firstName = firstName, lastName = lastName, cargoRol = cargoRol)
                    listUser.add(user)
                }


                Log.e("ok", "size: ${listUser.size}")
                adapter?.setItems(listUser)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "onCancelled: ${error.toException()}")
            }

        })
    }*/
}