package com.example.utrack.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.utrack.presenters.PresenterLogin
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass


class ViewSignUp : SecondViewClass() {
    //private var presenterLogin = PresenterLogin()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

        setTheme(R.style.AppTheme)
        setContentView(R.layout.signup)
        PresenterLogin.getInstance(this)
        val singInButton = findViewById<Button>(R.id.signUpButton)
        val signUpToSignInbutton = findViewById<Button>(R.id.signUpToSignInbutton)
        singInButton.setOnClickListener{
            var userName = findViewById<EditText>(R.id.signUpUsername).text.toString()
            var userEmail = findViewById<EditText>(R.id.signUpEmail).text.toString()
            var userPassword = findViewById<EditText>(R.id.signUpPassword).text.toString()
            var userConfirmPassword = findViewById<EditText>(R.id.signUpConfirmPassword).text.toString()
            var userRealName = findViewById<EditText>(R.id.signUpRealName).text.toString()
            PresenterLogin.getInstance(this).onSignUpToSignInButtonPressed(this.applicationContext,userName,userEmail,userPassword,userConfirmPassword,userRealName)
        }
        signUpToSignInbutton.setOnClickListener {
            PresenterLogin.getInstance(this).signUpToSignInbutton(this.applicationContext)
        }
    }
}
