package com.example.utrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accountsetting)
        val backButton = findViewById<ImageButton>(R.id.backButtonAccountSettingsPage)
        backButton.setOnClickListener {
            onBackAccountButtonSettingsPressed()
        }
        val saveButtonUserSettings = findViewById<ImageButton>(R.id.saveButtonAccountSettings)
        saveButtonUserSettings.setOnClickListener{
            onSaveButtonAccountSettingsPressed()
        }
    }

    private fun onSaveButtonAccountSettingsPressed() {
        Toast.makeText(applicationContext,"Saved!",Toast.LENGTH_SHORT).show()
    }

    fun onBackAccountButtonSettingsPressed(){
        startActivity(Intent(application,SettingsActivity().javaClass))
        this.finish()
    }
}
