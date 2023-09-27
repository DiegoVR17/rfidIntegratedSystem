package com.example.rfid_integrated_system_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_integrated_system_app.databinding.ActivityLoginBinding
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.banRegister.observe(this,banRegisterObserver)


        val errorMsgObserver = Observer<String>{errorMsg ->
            Snackbar.make(view,errorMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar"){}
                .show()
        }

        registerViewModel.errorMsg.observe(this,errorMsgObserver)

        val succesMsgObserver = Observer<String>{sucessMsg ->
            Toast.makeText(this,sucessMsg, Toast.LENGTH_SHORT).show()
        }

        registerViewModel.sucessMsg.observe(this,succesMsgObserver)

        registerBinding.buttonRegister.setOnClickListener {
            registerViewModel.validateRegisterData(registerBinding.EditTextEmail.text.toString(),
                registerBinding.EditTextPassword.text.toString()
                ,registerBinding.EditTextRepPassword.text.toString())

        }
    }
}