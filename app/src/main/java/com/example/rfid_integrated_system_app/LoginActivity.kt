package com.example.rfid_integrated_system_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_integrated_system_app.databinding.ActivityLoginBinding
import com.example.rfid_integrated_system_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val banLoginObserver = Observer<Boolean>{banLogin ->
            if (banLogin){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.banLogin.observe(this,banLoginObserver)


        val errorMsgObserver = Observer<String>{errorMsg ->
            Snackbar.make(view,errorMsg,Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar"){}
                .show()
        }

        loginViewModel.errorMsg.observe(this,errorMsgObserver)

        val succesMsgObserver = Observer<String>{sucessMsg ->
            Toast.makeText(this,sucessMsg, Toast.LENGTH_SHORT).show()
        }

        loginViewModel.sucessMsg.observe(this,succesMsgObserver)

        loginBinding.buttonLogin.setOnClickListener {
            loginViewModel.validateLoginData(loginBinding.EditTextEmail.text.toString()
                ,loginBinding.EditTextPassword.text.toString())

        }

        loginBinding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}