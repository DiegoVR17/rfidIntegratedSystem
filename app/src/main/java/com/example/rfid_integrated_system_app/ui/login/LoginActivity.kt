package com.example.rfid_integrated_system_app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rfid_integrated_system_app.ui.navigation.NavigationActivity
import com.example.rfid_integrated_system_app.ui.register.RegisterActivity
import com.example.rfid_integrated_system_app.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        if (currentUser != null){
            goToRegisterActivity()
        }

        val banLoginObserver = Observer<Boolean>{banLogin ->
            if (banLogin){
                goToRegisterActivity()
            }
        }
        loginViewModel.banLogin.observe(this,banLoginObserver)


        val errorMsgObserver = Observer<String?>{errorMsg ->
            error_msg(errorMsg,view)
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

    private fun goToRegisterActivity() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun error_msg(errorMsg: String?, view: ConstraintLayout) {
        Snackbar.make(view, errorMsg.toString(),Snackbar.LENGTH_INDEFINITE)
            .setAction("Continuar"){}
            .show()
    }
}