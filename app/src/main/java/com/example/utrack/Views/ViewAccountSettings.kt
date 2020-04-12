package com.example.utrack.Views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass

class ViewAccountSettings : SecondViewClass() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide navigation bar
        onCreateHideNavBar()

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

    private fun onBackAccountButtonSettingsPressed(){
        val intent = Intent(application, ViewSettings().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
