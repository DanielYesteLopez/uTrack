package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.signup)
    }

    fun onSignUpToSignInButtonPressed(view:View){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
