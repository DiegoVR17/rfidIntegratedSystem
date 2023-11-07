package com.example.rfid_integrated_system_app.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_integrated_system_app.R
import com.example.rfid_integrated_system_app.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

       val banRegisterObserver = Observer<Boolean>{banRegister ->
            if (banRegister){
                onBackPressedDispatcher.onBackPressed()
            }
        }

        registerViewModel.banRegister.observe(this,banRegisterObserver)

      /* registerViewModel.banRegister.observe(this){
            onBackPressedDispatcher.onBackPressed()
        }*/

        val errorMsgObserver = Observer<String?>{errorMsg ->
            error_msg(errorMsg,view)
        }

        registerViewModel.errorMsg.observe(this,errorMsgObserver)

        val succesMsgObserver = Observer<String>{sucessMsg ->
            Toast.makeText(this,sucessMsg, Toast.LENGTH_SHORT).show()
        }

        registerViewModel.sucessMsg.observe(this,succesMsgObserver)


        val cargoRol = resources.getStringArray(R.array.cargos1)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cargoRol)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        registerBinding.spinnerCargoRol.adapter = adapter

        registerBinding.buttonRegister.setOnClickListener {
            registerViewModel.validateRegisterData(registerBinding.EditTextEmail.text.toString(),
                registerBinding.EditTextPassword.text.toString()
                ,registerBinding.EditTextRepPassword.text.toString(),
                registerBinding.spinnerCargoRol.selectedItem.toString(),registerBinding.TextInputEditTextFirstName.text.toString(),
                registerBinding.TextInputEditTextLastName.text.toString())

        }

    }

    private fun error_msg(errorMsg: String?, view: ConstraintLayout) {
        Snackbar.make(view, errorMsg.toString(),Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }
}