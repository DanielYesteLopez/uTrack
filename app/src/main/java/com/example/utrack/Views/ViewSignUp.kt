package com.example.utrack.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewSignUp : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setTheme(R.style.AppTheme)
        setContentView(R.layout.signup)
        val singInButton = findViewById<Button>(R.id.signUpToSignInbutton)
        singInButton.setOnClickListener{
            onSignUpToSignInButtonPressed()
        }
    }

    private fun onSignUpToSignInButtonPressed(){
        val intent = Intent(application, ViewSignIn().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
