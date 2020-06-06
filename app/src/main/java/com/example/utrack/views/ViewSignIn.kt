package com.example.utrack.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.utrack.presenters.PresenterLogin
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewSignIn : SecondViewClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signin)
        PresenterLogin.getInstance(this)
        manageSignInButtons()
    }

    private fun manageSignInButtons() {
        val signInButton = findViewById<Button>(R.id.buttonSingIn_InSingIn)
        val singOutButton = findViewById<Button>(R.id.signInToSignUpButton)
        signInButton.setOnClickListener{
            val signInUsernameLogin = findViewById<EditText>(R.id.signInUsernameLogin).text.toString()
            val signInPasswordLogin = findViewById<EditText>(R.id.signInPasswordLogin).text.toString()
            PresenterLogin.getInstance(this).onToSignInButtonPressed(this.applicationContext,signInUsernameLogin,signInPasswordLogin)
        }
        singOutButton.setOnClickListener{
            PresenterLogin.getInstance(this).onToSignUpFromSignInButtonPressed(applicationContext)
        }
    }
}
