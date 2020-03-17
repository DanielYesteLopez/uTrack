package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class UserSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usersettings)
    }

    fun onBackButtonUserSettingsPressed(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
