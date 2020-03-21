package com.example.utrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settingspage)
    }

    fun onUserSettingsButtonPressed(view: View){
        val intent = Intent(this, UserSettingsActivity::class.java)
        startActivity(intent)
    }

    fun onAccountButtonPressed(view: View){
        val intent = Intent(this, AccountSettingsActivity::class.java)
        startActivity(intent)
    }

    fun onBackSettingsButtonPressed(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onInformationButtonPressed(view: View) {
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)
    }


}