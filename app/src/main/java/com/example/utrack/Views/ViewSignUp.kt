package com.example.utrack.Views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.utrack.Presenters.PresenterLogin
import com.example.utrack.R

class ViewSignUp : AppCompatActivity() {
    private var presenterLogin = PresenterLogin()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.signup)
        val singInButton = findViewById<Button>(R.id.signUpButton)
        val signUpToSignInbutton = findViewById<Button>(R.id.signUpToSignInbutton)
        singInButton.setOnClickListener{
            var userName = findViewById<EditText>(R.id.signUpUsername).text.toString()
            var userEmail = findViewById<EditText>(R.id.signUpEmail).text.toString()
            var userPassword = findViewById<EditText>(R.id.signUpPassword).text.toString()
            var userConfirmPassword = findViewById<EditText>(R.id.signUpConfirmPassword).text.toString()
            var userRealName = findViewById<EditText>(R.id.signUpRealName).text.toString()
            presenterLogin.onSignUpToSignInButtonPressed(this.applicationContext,userName,userEmail,userPassword,userConfirmPassword,userRealName)
        }
        signUpToSignInbutton.setOnClickListener {
            presenterLogin.signUpToSignInbutton(this.applicationContext)
        }
    }


    private fun hideNav() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }else
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }
}
