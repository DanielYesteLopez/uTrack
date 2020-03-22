package com.example.utrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.nio.InvalidMarkException

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        setContentView(R.layout.settingspage)
        manageButtonsSettings()
    }

    private fun manageButtonsSettings() {
        val userSettinggsButton = findViewById<ImageButton>(R.id.imageButtonUserSettings_SettingsPage)
        val accountSettingsButton = findViewById<ImageButton>(R.id.accountButtonSettingsPage)
        val backSettingsButton = findViewById<ImageButton>(R.id.backButtonSettingsPage)
        val informationSettingsButton = findViewById<ImageButton>(R.id.informationButtonSettingsPage)
        userSettinggsButton.setOnClickListener{
            onUserSettingsButtonPressed()
        }
        accountSettingsButton.setOnClickListener{
            onAccountButtonPressed()
        }
        backSettingsButton.setOnClickListener{
            onBackSettingsButtonPressed()
        }
        informationSettingsButton.setOnClickListener{
            onInformationButtonPressed()
        }

    }

    fun onUserSettingsButtonPressed(){
        val intent = Intent(this, UserSettingsActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun onAccountButtonPressed(){
        val intent = Intent(this, AccountSettingsActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun onBackSettingsButtonPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun onInformationButtonPressed() {
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)
        this.finish()
    }


}