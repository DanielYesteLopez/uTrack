package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.signup)
        val singInButton = findViewById<Button>(R.id.signUpToSignInbutton)
        singInButton.setOnClickListener{
            onSignUpToSignInButtonPressed()
        }
    }

    fun onSignUpToSignInButtonPressed(){
        startActivity(Intent(application,SignInActivity().javaClass))
        this.finish()
    }
}
