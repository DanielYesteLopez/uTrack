package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        manageSignInButtons()
    }

    private fun manageSignInButtons() {
        val signInButton = findViewById<Button>(R.id.buttonSingIn_InSingIn)
        val singOutButton = findViewById<Button>(R.id.signInToSignUpButton)
        signInButton.setOnClickListener{
            onToSignInButtonPressed()
        }
        singOutButton.setOnClickListener{
            onToSignUpButtonPressed()
        }
    }

    fun onToSignInButtonPressed(){
        startActivity(Intent(application,MainActivity().javaClass))
        this.finish()
    }

    fun onToSignUpButtonPressed(){
        startActivity(Intent(application,SignUpActivity().javaClass))
        this.finish()

    }

}
