package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
    }

    fun onToSignUpButtonPressed(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}
