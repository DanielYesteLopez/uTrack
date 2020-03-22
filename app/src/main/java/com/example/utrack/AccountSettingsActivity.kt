package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsetting)
        val backButton = findViewById<ImageButton>(R.id.backButtonAccountSettingsPage)
        backButton.setOnClickListener {
            onBackAccountButtonSettingsPressed()
        }
    }

    fun onBackAccountButtonSettingsPressed(){
        startActivity(Intent(application,SettingsActivity().javaClass))
        this.finish()
    }
}
