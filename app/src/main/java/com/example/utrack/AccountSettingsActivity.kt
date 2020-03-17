package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsetting)
    }

    fun onBackButtonAccountSettingsPressed(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}
