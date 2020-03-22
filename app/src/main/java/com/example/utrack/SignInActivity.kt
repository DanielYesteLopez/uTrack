package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button

class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        hideNav()
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

    fun hideNav() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNav()
        }
    }


}
