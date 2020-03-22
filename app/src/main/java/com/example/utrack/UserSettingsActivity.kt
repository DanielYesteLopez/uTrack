package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class UserSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usersettings)
        val backButton = findViewById<ImageButton>(R.id.backButtonUserSettingsPage)
        backButton.setOnClickListener {
            onBackUserSettingsButtonPressed()

        }
    }

    fun onBackUserSettingsButtonPressed(){
        startActivity(Intent(application,SettingsActivity().javaClass))
        this.finish()
    }
}
