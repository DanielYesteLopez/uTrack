package com.example.utrack.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewSignIn : SecondViewClass() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

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

    private fun onToSignInButtonPressed(){
        val intent = Intent(application, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onToSignUpButtonPressed(){
        val intent = Intent(application, ViewSignUp().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
